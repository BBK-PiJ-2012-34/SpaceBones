/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoes.players;

import dominoes.gui.DominoUIImpl;
import dominoes.*;
import java.util.ArrayList;


/**
 *
 * @author tom
 */
public class LocalPlayer implements DominoPlayer {

    private ArrayList<Bone> playerHand = new ArrayList<>();
    private String playerName = "";
    private int playerPoints = 0;
    private DominoUIImpl control;

    public LocalPlayer() {
    }

    @Override
    public Play makePlay(Table table) throws CantPlayException {
        if (canPlay(table)) {
            Play newPlay = new Play(playerHand.get(0), Play.LEFT);
            return newPlay;
        } else {
            throw new CantPlayException();
        }
    }

    @Override
    public void takeBack(Bone bone) {
        System.out.println("Take back called.");
        playerHand.add(bone);
    }

    @Override
    public void draw(BoneYard boneyard) {
        Bone newBone = boneyard.draw();
        if (newBone != null) {
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
        for (Bone eachBone : playerHand) {
            if (eachBone.left() == table.left() || eachBone.left() == table.right()) {
                return true;
            } else if (eachBone.right() == table.left() || eachBone.right() == table.right()) {
                return true;
            }
        }
        return false;
    }

    public void removeBone(Bone bone) {
        for (int i = 0; i < playerHand.size(); i++) {
            if (playerHand.get(i).left() == bone.left()) {
                if (playerHand.get(i).right() == bone.right()) {
                    System.out.println("Removed bone");
                    playerHand.remove(i);
                }
            }

        }

        // In case bone is flipped by Dominoes class.
        for (int i = 0; i < playerHand.size(); i++) {
            if (playerHand.get(i).left() == bone.right()) {
                if (playerHand.get(i).right() == bone.left()) {
                    System.out.println("Removed bone");
                    playerHand.remove(i);
                }
            }

        }

    }

    public DominoUIImpl getControl() {
        return control;
    }

    public void setControl(DominoUIImpl control) {
        this.control = control;
    }
}
