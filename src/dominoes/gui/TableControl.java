package dominoes.gui;

import dominoes.Bone;
import dominoes.Play;
import dominoes.Table;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

/**
 * TableControle Controller
 *
 * @author Hisham Khalifa
 */
public class TableControl extends BoneBucketControl {

    private Table table;
    private EventHandler dropBoneEventHandler;

    public TableControl() {
        super();

        this.setTitleCardVisible(false);
        this.setDraggable(false);
        this.setDroppable(true);
        this.setHorizontal(true);

        installDropHandler();
    }

    public TableControl(Table table) {
        super(table.layout());

        this.setTitleCardVisible(false);
        this.setDraggable(false);
        this.setDroppable(true);
        this.setHorizontal(true);

        this.table = table;

        installDropHandler();
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    private void installDropHandler() {
        dropBoneEventHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();

                String boneString = db.getString();

                String boneArray[] = boneString.split(",");

                int left = Integer.parseInt(boneArray[0]);
                int right = Integer.parseInt(boneArray[1]);

                Bone bone = new Bone(left, right);

                BoneBucketControl source = (TableControl) event.getSource();

                source.getScene().setCursor(Cursor.DEFAULT);

                if (getOwner() instanceof DominoUIImpl) {
                    DominoUIImpl owner = (DominoUIImpl) getOwner();
                    owner.setPlayMade(bone);

                    double dropX = event.getX();

                    if (dropX <= 498) {
                        owner.setPlaySideMade(Play.LEFT);
                    } else {
                        owner.setPlaySideMade(Play.RIGHT);
                    }
                    
                    // Let's inform our delegate that a drop has occurred.
                    owner.submitPlay();

                }

                event.consume();

            }
        };

        this.setOnDragDropped(dropBoneEventHandler);
    }
}
