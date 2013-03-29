package dominoes.gui.proxy;

import dominoes.Bone;
import dominoes.BoneYard;
import dominoes.CantPlayException;
import dominoes.Play;
import dominoes.Table;
import dominoes.gui.DominoUIImp;
import dominoes.players.DominoPlayer;
import dominoes.players.LocalPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PlayerProxy - Proxy class used by DominoUIImp (GUI Main Controller). Intercepts makePlay() method.
 *
 * @author Hisham Khalifa
 */
public class PlayerProxy implements DominoPlayer {

    private DominoPlayer realPlayer;
    private DominoUIImp control;
    private Boolean isHuman;

    public PlayerProxy(DominoPlayer player) {
        this.realPlayer = player;
    }

    @Override
    public Play makePlay(Table table) throws CantPlayException {
        Play newPlay;
        newPlay = null;

        // For both AI and human. Will enable Draw button control and rethrow CantPlayException if can't play.
        try {
            newPlay = realPlayer.makePlay(table);
        } catch (CantPlayException e) {
            this.control.drawButtonRename(); // Rename to Pass if boneyard is 0.
            this.control.setDrawButtonDisable(false);
            throw e;
        }

        this.control.setDrawButtonDisable(true);

        // Otherwise we'll get to here in which we let human player play, and for AI we just return the play.
        if (!this.isHuman) // AI played bone, so return it.
        {

            this.control.delayAIPlay(this, table);

            return newPlay;
        }

        // Human play
        newPlay = getControl().getPlay(this, table); // Need human player to select bone in GUI.
        if (newPlay != null) {
            LocalPlayer lp = (LocalPlayer) realPlayer;

            lp.removeBone(newPlay.bone());
        }

        return newPlay;
    }

    @Override
    public void takeBack(Bone bone) {

        realPlayer.takeBack(bone);

    }
    
    @Override
    public void draw(BoneYard boneyard) {

        realPlayer.draw(boneyard);

        this.control.updatePlayerBonesBox(realPlayer);

    }

    @Override
    public int numInHand() {

        return realPlayer.numInHand();

    }

    @Override
    public Bone[] bonesInHand() {

        return realPlayer.bonesInHand();

    }

    @Override
    public void newRound() {
        realPlayer.newRound();

    }

    @Override
    public void setPoints(int points) {
        realPlayer.setPoints(points);

    }

    @Override
    public int getPoints() {
        return realPlayer.getPoints();
    }

    @Override
    public void setName(String name) {
        realPlayer.setName(name);
    }

    @Override
    public String getName() {
        return realPlayer.getName();
    }

    public DominoPlayer getRealPlayer() {
        return realPlayer;
    }

    public void setRealPlayer(DominoPlayer realPlayer) {
        this.realPlayer = realPlayer;
    }

    public DominoUIImp getControl() {
        return control;
    }

    public void setControl(DominoUIImp control) {
        this.control = control;
    }

    public Boolean getIsHuman() {
        return isHuman;
    }

    public void setIsHuman(Boolean isHuman) {
        this.isHuman = isHuman;
    }
}
