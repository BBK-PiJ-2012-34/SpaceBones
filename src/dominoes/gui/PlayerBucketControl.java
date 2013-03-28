package dominoes.gui;

import dominoes.players.DominoPlayer;

/**
 * BoneBucketControl Controller
 *
 * @author Hisham Khalifa
 */
public class PlayerBucketControl extends BoneBucketControl {

    private DominoPlayer dominoPlayer;
    private String badgeName;

    public PlayerBucketControl() {
        super();
        
        this.setTitleCardVisible(true);
        this.setDraggable(true);
        this.setDroppable(false);
        this.setHorizontal(false);

    }

    public PlayerBucketControl(DominoPlayer player) {
        super(player.bonesInHand());

        this.setTitleCardVisible(true);
        this.setDraggable(true);
        this.setDroppable(false);
        this.setHorizontal(false);

        this.dominoPlayer = player;

    }

    public DominoPlayer getDominoPlayer() {
        return dominoPlayer;
    }

    public void setDominoPlayer(DominoPlayer player) {
        this.dominoPlayer = player;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
        this.setTitleCardText(badgeName);
    }

}
