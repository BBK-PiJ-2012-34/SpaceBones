package dominoes.players;

import dominoes.*;
import java.util.ArrayList;

/**
 *
 * @author tom
 * 
 * This AIPLayer implements a set of rules gathered from guides on how to play
 * dominoes. The rules exist as specific methods that add a score to each possible
 * play that the AIPlayer can make. After all rules have been applied the play
 * with the best score is selected.
 */
public class RuleBaseAIPlayer implements DominoPlayer {
    private int[] oldTable = new int[2];
    private ArrayList<Integer> passedSuits = new ArrayList<>();
    private ArrayList<Bone> playerHand = new ArrayList<>();
    private String playerName = "";
    private int playerPoints = 0;
    private Bone playBone = new Bone(-1, -1);
    private Play newPlay = new Play(playBone, Play.RIGHT);
    private ArrayList<Play> possiblePlays;
    private int[] playScore;
    
    public RuleBaseAIPlayer() {
        oldTable[0] = -1;
        oldTable[1] = -1;
    }
    
    @Override
    public Play makePlay(Table table) throws CantPlayException {
        if(canPlay(table)) {
            possiblePlays = getPotentialPlays(table);
            // Initalise playScore to the correct length.
            int noOfPlays = possiblePlays.size();
            playScore = new int[noOfPlays];
                    
            rankPlaysOnScore();
            scoreToKeepSidesEqual(table);
            scoreToAllowMorePlays(table);
            scoreForPassed(table);
            
            // Use the play that has the highest score.
            int currentBestScore = -1;
            for(int i = 0; i < noOfPlays; i++) {
                if(playScore[i] > currentBestScore) {
                    currentBestScore = playScore[i];
                    newPlay = possiblePlays.get(i);
                }
            }
            //showPlayScores();
            removeBone(newPlay.bone());
            // Update the oldTable Array to catch a pass by opponent.
            oldTable = getPlayResult(table, newPlay);
            return newPlay;
        } else {
            throw new CantPlayException();
        }
    }
    
    
    
    /*
     * Checks to see if the opponent has passed and records what suits they
     * passed on.
     */
    private void checkForPass(Table table) {
        if(table.left() == oldTable[0] && table.right() == oldTable[1]) {
            passedSuits.add(table.left());
            passedSuits.add(table.right());
        }
    }
    
    /*
     * Adds a score for plays that result in a table that the opponent has
     * passed on.
     */
    private void scoreForPassed(Table table) {
        int[] result = new int[2];
        checkForPass(table);
        int i = 0;
        for(Play eachPlay : possiblePlays) {
            result = getPlayResult(table, eachPlay);
            for(int passed : passedSuits) {
                if(result[0] == passed) {
                    playScore[i] += 10;
                }
                if(result[1] == passed) {
                    playScore[i] += 10;
                }
            }
            i++;
        }
    }
    
    /*
     * returns an integer array representing the result of a play on the table.
     */
    private int[] getPlayResult(Table table, Play play) {
        int[] result = new int[2];
        if(table.left() == play.bone().left()) {
            result[0] = play.bone().right();
            result[1] = table.right();
        } else if(table.right() == play.bone().left()) {
            result[0] = table.left();
            result[1] = play.bone().right();
        } else if(table.left() == play.bone().right()) {
            result[0] = play.bone().left();
            result[1] = table.right();
        } else if(table.right() == play.bone().right()) {
            result[0] = table.left();
            result[1] = play.bone().left();
        }
        return result;
    }
    
    /*
     * Adds a number of points to each possible plays score if it would result in a table
     * that allows another of the players bones to be played on a side of the table.
     * If it could be played on either side of the new table the play gets 4 points.
     */
    private void scoreToAllowMorePlays(Table table) throws CantPlayException {
        int i = 0;
        int[] result;
        for(Play eachPlay : possiblePlays) {
            result = getPlayResult(table, eachPlay);
            for(Bone eachBone : playerHand) {
                if(eachBone.left() == result[0] | eachBone.left() == result[1]) {
                    //System.out.println("Option "+i+" gets a more play bonus!");
                    playScore[i] += 2;
                }
                if(eachBone.right() == result[0] | eachBone.right() == result[1]) {
                    //System.out.println("Option "+i+" gets a more play bonus!");
                    playScore[i] += 2;
                }
            }
            i++;
        }
    }
    
    /*
     * Give each play that results in equal sides on the table an extra 10 to
     * its score.
     */
    private void scoreToKeepSidesEqual(Table table) {
        int i = 0;
        for(Play eachPlay : possiblePlays) {
            if(table.right() == table.left()) {
                if(eachPlay.bone().right() == eachPlay.bone().left()) {
                        //System.out.println("Play will keep sides equal so add 10");
                        playScore[i] += 10;
                }
            } else {
                if(eachPlay.end() == Play.LEFT) {
                    if(table.right() == eachPlay.bone().right() | table.right() == eachPlay.bone().left()) {
                        //System.out.println("Play will keep sides equal so add 10");
                        playScore[i] += 10;
                    }
                } else if(eachPlay.end() == Play.RIGHT) {
                    if(table.left() == eachPlay.bone().right() | table.left() == eachPlay.bone().left()) {
                        //System.out.println("Play will keep sides equal so add 10");
                        playScore[i] += 10;
                    }
                }
            }

            i++;
        }
    }
    
    private ArrayList<Play> getPotentialPlays(Table table) {
        // Clear possiblePlays of last rounds options.
        possiblePlays = new ArrayList<>();
        for(Bone eachBone : playerHand) {
            if(eachBone.left() == table.left() | eachBone.right() == table.left()) {
                possiblePlays.add(new Play(eachBone, Play.LEFT));
            } else if (eachBone.left() == table.right() | eachBone.right() == table.right()) {
                possiblePlays.add(new Play(eachBone, Play.RIGHT));
            }
        }
        return possiblePlays;
    }
    
    /*
     * give each possible play a score by adding the point value of each side
     * together.
     */
    private void rankPlaysOnScore() {
        int i = 0;
        for(Play eachPlay : possiblePlays) {
            Bone eachBone = eachPlay.bone();
            playScore[i] = eachBone.left() + eachBone.right();
            i++;
        }
    }
    
    @Override
    public void takeBack(Bone bone) {
        playerHand.add(bone);
    }
    
    @Override
    public void draw(BoneYard boneyard) {
        Bone newBone = boneyard.draw();
        if(newBone != null) {
            playerHand.add(newBone);
        }
    }
    
    @Override
    public int numInHand() {
        return playerHand.size();
    }
    
    @Override
    public Bone[] bonesInHand() {
        Bone[] boneArray = playerHand.toArray(new Bone[playerHand.size()]);
        return boneArray;
    }
    
    @Override
    public void newRound() {
        playerHand.clear();
        // Reset oldTable array.
        oldTable[0] = -1;
        oldTable[1] = -1;
        // Get rid of old information in passed suits.
        passedSuits.clear();
    }
    
    @Override
    public void setPoints(int points) {
        playerPoints = points;
    }
    
    @Override
    public int getPoints() {
        return playerPoints;
    }
    
    @Override
    public void setName(String name) {
        playerName = name;
    }
    
    @Override
    public String getName() {
        return playerName;
    }
    
    private boolean canPlay(Table table) {
        for(Bone eachBone : playerHand) {
            if(eachBone.left() == table.left() || eachBone.left() == table.right()) {
                return true;
            } else if(eachBone.right() == table.left() || eachBone.right() == table.right()) {
                return true;
            }
        }
        return false;
    }
    
    private void removeBone(Bone bone) {
        playerHand.remove(bone);
    }
    
    private void showPlayScores() {
        int i = 0;
        for(Play eachPlay : possiblePlays) {
            Bone eachBone = eachPlay.bone();
            System.out.println("bone "+i+": ("+eachBone.left()+","+eachBone.right()+") is given a score of: "+playScore[i]);
            i++;
        }
    }
}
