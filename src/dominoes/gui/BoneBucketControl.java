package dominoes.gui;

import dominoes.Bone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * BoneBucketControl Controller
 *
 * Superclass for creating bone buckets like player and table buckets
 *
 * @author Hisham Khalifa
 */
public class BoneBucketControl extends AnchorPane {

    private List<Bone> bonesArrayList = new ArrayList<>();
    private Boolean draggable;
    private Boolean droppable;
    private Boolean horizontal;
    private EventHandler boneDraggedOverHandler;
    private EventHandler boneDragEnteredHandler;
    private EventHandler boneDragExitedHandler;
    private EventHandler boneDroppedHandler;
    private Object owner;
    @FXML
    public HBox bonesHBox; // This is public for subclassing control
    @FXML
    public Label titleCard; // This is public for subclassing control

    public BoneBucketControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BoneBucketControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.draggable = true;
            this.droppable = false;
            this.horizontal = false;


            installHandlers();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public BoneBucketControl(Bone[] bonesArray) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BoneBucketControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            this.draggable = true;
            this.droppable = false;
            this.horizontal = false;

            installHandlers();

            bonesArrayList = new ArrayList<>(Arrays.asList(bonesArray));
            redraw();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setTitleCardText(String string) {
        this.titleCard.setText(string);
    }

    public void addBone(Bone bone) {
        bonesArrayList.add(bone);
        redraw();
    }

    public void addBone(int index, Bone bone) {
        bonesArrayList.add(index, bone);
        redraw();
    }

    public void removeBone(Bone bone) {
        bonesArrayList.remove(bone);
        redraw();
    }

    public void removeBone(int index) {
        bonesArrayList.remove(index);
        redraw();
    }

    public void removeAllBones() {
        bonesArrayList.clear();
        redraw();
    }

    public void setTitleCardVisible(Boolean b) {
        titleCard.setVisible(b);
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;

        redraw();
    }

    public void setDroppable(Boolean droppable) {

        if (droppable) {
            this.setOnDragOver(boneDraggedOverHandler);
            this.setOnDragEntered(boneDragEnteredHandler);
            this.setOnDragExited(boneDragExitedHandler);
            this.setOnDragDropped(boneDroppedHandler);
        } else {
            this.setOnDragOver(null);
            this.setOnDragEntered(null);
            this.setOnDragExited(null);
            this.setOnDragDropped(null);
        }
    }

    public void redraw() {
        // Clear all bones in HBox.
        bonesHBox.getChildren().clear();

        double scaleX = 0.15;
        double scaleY = 0.15;

        boolean tooManyFlag = false;
        boolean ellipseFlag = true;

        int size = bonesArrayList.size();

        if (size > 10 & this.horizontal) {
            tooManyFlag = true;
        }

        int boneCount = 0;

        // Add bones to HBox.
        for (Bone bone : bonesArrayList) {

            // For long table, don't show all bones. Place an "ellipse" bone indicator.
            if (tooManyFlag) {

                if (boneCount >= 3 & boneCount < (size - 3)) {
                    if (ellipseFlag) {
                        ellipseFlag = false;
                        BoneControl boneControl = new BoneControl();

                        bonesHBox.getChildren().add(boneControl);

                        boneControl.setScaleX(scaleX);
                        boneControl.setScaleY(scaleY);
                    }
                    boneCount++;
                    continue;
                }
                
            }

            BoneControl boneControl = new BoneControl(bone);

            boneControl.setOwningBucket(this);

            boneControl.setDraggable(this.draggable);
            boneControl.setHorizontal(this.horizontal);

            bonesHBox.getChildren().add(boneControl);

            // Scale Set Spacing in hbox to -100 when doing this
            boneControl.setScaleX(scaleX);
            boneControl.setScaleY(scaleY);

            boneCount++;
        }
    }

    private void installHandlers() {

        boneDraggedOverHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                event.consume();
            }
        };

        if (this.droppable) {
            this.setOnDragOver(boneDraggedOverHandler);
        }

        boneDragEnteredHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                event.consume();
            }
        };

        if (this.droppable) {
            this.setOnDragEntered(boneDragEnteredHandler);
        }

        boneDragExitedHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                event.consume();
            }
        };

        if (this.droppable) {
            this.setOnDragExited(boneDragExitedHandler);
        }

        boneDroppedHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();

                String boneString = db.getString();

                String boneArray[] = boneString.split(",");

                int left = Integer.parseInt(boneArray[0]);
                int right = Integer.parseInt(boneArray[1]);


                Bone bone = new Bone(left, right);

                addBone(bone);

                BoneBucketControl source = (BoneBucketControl) event.getSource();

                source.getScene().setCursor(Cursor.DEFAULT);

                event.consume();

            }
        };

        if (this.droppable) {
            this.setOnDragDropped(boneDroppedHandler);
        }
    }

    public void setHorizontal(boolean b) {
        this.horizontal = b;

        if (horizontal) {
            bonesHBox.setSpacing(-95);
        } else {
            bonesHBox.setSpacing(-115);
        }

        redraw();
    }

    public int size() {
        return bonesArrayList.size();
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }
}
