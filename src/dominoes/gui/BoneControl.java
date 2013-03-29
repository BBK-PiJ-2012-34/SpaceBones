package dominoes.gui;

import dominoes.Bone;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * BoneControl Controller
 *
 * Holds an instance of Bone and displays it in a graphical fashion
 *
 * @author Hisham Khalifa
 */
public class BoneControl extends VBox {

    private Bone bone;
    private Boolean horizontal;
    private Boolean draggable;
    private EventHandler boneDragHandler;
    private EventHandler boneDragDoneHandler;
    private BoneBucketControl owningBucket;
    
    @FXML
    private Label left;
    @FXML
    private Label right;


        public BoneControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BoneControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            setHorizontal(false);
            setDraggable(true);

            installHandlers();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    public BoneControl(Bone bone) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BoneControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            this.bone = bone;
            String boneLeftString = Integer.toString(bone.left());
            String boneRightString = Integer.toString(bone.right());
            this.left.setText(boneLeftString);
            this.right.setText(boneRightString);
            setHorizontal(false);
            setDraggable(true);

            installHandlers();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Boolean getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;

        if (horizontal) {
            this.setRotate(-90);
        }
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;

        if (draggable) {
            this.setOnDragDetected(boneDragHandler);
            this.setOnDragDone(boneDragDoneHandler);
        } else {
            this.setOnDragDetected(null);
            this.setOnDragDone(null);
        }
    }

    public Bone getBone() {
        return bone;
    }

    private void installHandlers() {
        // Bone drag handler
        boneDragHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BoneControl source = (BoneControl) event.getSource();

                //source.setVisible(false);
                 
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                
                content.putString(bone.left()+","+bone.right());                
                
                db.setContent(content);
                
                source.getScene().setCursor(Cursor.NONE);
                setSceneCursorImageFromNode(source);
                event.consume();
            }
        };

        if (this.draggable) {
            this.setOnDragDetected(boneDragHandler);
        }
        
        // Bone drag done handler
        boneDragDoneHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                BoneControl source = (BoneControl) event.getSource();
                                             
                source.getScene().setCursor(Cursor.DEFAULT);
                event.consume();
                
            }
        };

        if (this.draggable) {
            this.setOnDragDone(boneDragDoneHandler);
        }
    }

    private void setSceneCursorImageFromNode(Node node) {
        // Create SnapshotParameters with a transparent background.
        Color transparentColor = new Color(0, 0, 0, 0.0); // Alpha 0.0 == transparent.
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(transparentColor);

        WritableImage imageForCursor = node.snapshot(snapshotParameters, null);

        ImageCursor imageCursor = new ImageCursor(imageForCursor,
                imageForCursor.getWidth() / 2,
                imageForCursor.getHeight() / 2);

        node.getScene().setCursor(imageCursor);
    }

    public BoneBucketControl getOwningBucket() {
        return owningBucket;
    }

    public void setOwningBucket(BoneBucketControl owningBucket) {
        this.owningBucket = owningBucket;
    }
}
