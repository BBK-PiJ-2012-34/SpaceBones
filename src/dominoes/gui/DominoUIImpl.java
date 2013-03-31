package dominoes.gui;

import dominoes.Bone;
import dominoes.BoneYard;
import dominoes.DominoUI;
import dominoes.Dominoes;
import dominoes.Play;
import dominoes.Table;
import dominoes.gui.proxy.PlayerProxy;
import dominoes.gui.sound.MusicPlayer;
import dominoes.players.DominoPlayer;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * JavaFX Main Application Controller class
 *
 * @author Hisham Khalifa
 */
public class DominoUIImpl implements Initializable, DominoUI {

    final private static int maxDots = 6;
    private Dominoes game;
    private Table table;
    private BoneYard boneyard;
    private PlayerProxy playerOne;
    private PlayerProxy playerTwo;
    private PlayerProxy currentPlayer;
    private int roundCount = 1;
    private int pointsToWin = 100;
    private Stage makePlayShowAndWaitStage;
    private Bone playMade;
    private int playSideMade;
    private DominoPlayer lastPlayer = null;
    private Stage nextPlayerPromptStage;
    private TableControl tableBoneBucket;
    private PlayerBucketControl playerBoneBucket;
    private MusicPlayer musicPlayer;
    @FXML
    private Label gameTitleLabel;
    @FXML
    private TextField player1NameField;
    @FXML
    private TextField player2NameField;
    @FXML
    private ChoiceBox player1KindChoiceBox;
    @FXML
    private ChoiceBox player2KindChoiceBox;
    @FXML
    private TextField pointsToWinField;
    @FXML
    private Button drawButton;
    @FXML
    private Label roundCountLabel;
    @FXML
    private Label boneyardBoneCountLabel;
    @FXML
    private Label ai1BoneCountLabel;
    @FXML
    private Label ai2BoneCountLabel;
    @FXML
    private Label player1NameLabel;
    @FXML
    private Label player1PointsLabel;
    @FXML
    private Label player2PointsLabel;
    @FXML
    private Label player2NameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label invalidMoveLabel;
    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupDisplay();
        setupDialogues();
        setupMusic();
    }

    private void setupMusic() {
        musicPlayer = new MusicPlayer();
    }

    private void setupDisplay() {
        // Setup marquee
        InputStream is = this.getClass().getResourceAsStream("resources/Tr2n.ttf");
        gameTitleLabel.setFont(Font.loadFont(is, 75));

        // Setup player types choice boxes and default selections
        player1KindChoiceBox.setItems(FXCollections.observableArrayList("Human", "Advanced AI", "Simple AI"));
        player1KindChoiceBox.getSelectionModel().selectFirst();
        player2KindChoiceBox.setItems(FXCollections.observableArrayList("Human", "Advanced AI", "Simple AI"));
        player2KindChoiceBox.getSelectionModel().select(1);

        // Instantiate table bone bucket
        tableBoneBucket = new TableControl();
        tableBoneBucket.setOwner(this);

        VBox.setMargin(tableBoneBucket, new Insets(20, 0, 0, 0));

        vBox.getChildren().add(1, tableBoneBucket);

        // Instantiate player bone bucket
        playerBoneBucket = new PlayerBucketControl();
        playerBoneBucket.setOwner(this);

        // Apply VBox margins to bucket
        VBox.setMargin(playerBoneBucket, new Insets(0, 0, 90, 0));

        // Add bucket to our vbox
        vBox.getChildren().add(2, playerBoneBucket);

        // Draw button initially disabled
        setDrawButtonDisable(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), vBox);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        // Play UI effects from the beginning
        startEffects();
    }

    public void setDrawButtonDisable(boolean b) {

        drawButton.setDisable(true);
    }

    public void drawButtonRename() {
        if (this.boneyard.size() == 0) {
            drawButton.setText("Pass");
        } else {
            drawButton.setText("Draw");
        }
    }

    private void startEffects() {
        ScaleTransition st;
        st = new ScaleTransition(Duration.seconds(2.0), vBox);
        st.setFromX(3.75);
        st.setToX(1);
        st.setFromY(1.2);
        st.setToY(1.0);

        st.interpolatorProperty().set(Interpolator.LINEAR);

        st.play();

        TranslateTransition tt;
        tt = new TranslateTransition(Duration.seconds(5.0), gameTitleLabel);
        tt.setFromY(0);
        tt.setToY(10);

        tt.setCycleCount(100);
        tt.setAutoReverse(true);

        tt.play();
    }

    @FXML
    private void newGameButtonAction(ActionEvent event) {
        musicPlayer.skipToSecondSong();

        lastPlayer = null;

        startEffects();

        this.roundCount = 1;
        this.pointsToWin = Integer.parseInt(this.pointsToWinField.getText());


        int player1TypeChoice = player1KindChoiceBox.getSelectionModel().selectedIndexProperty().intValue();
        int player2TypeChoice = player2KindChoiceBox.getSelectionModel().selectedIndexProperty().intValue();

        if (player1TypeChoice == 0) {
            playerOne = PlayerBuilder.BuildPlayer(PlayerBuilder.LOCAL_PLAYER, this);
        }

        if (player1TypeChoice == 1) {
            playerOne = PlayerBuilder.BuildPlayer(PlayerBuilder.ADVANCED_AI_PLAYER, this);
        }

        if (player1TypeChoice == 2) {
            playerOne = PlayerBuilder.BuildPlayer(PlayerBuilder.SIMPLE_AI_PLAYER, this);
        }

        if (player2TypeChoice == 0) {
            playerTwo = PlayerBuilder.BuildPlayer(PlayerBuilder.LOCAL_PLAYER, this);
        }

        if (player2TypeChoice == 1) {
            playerTwo = PlayerBuilder.BuildPlayer(PlayerBuilder.ADVANCED_AI_PLAYER, this);
        }

        if (player2TypeChoice == 2) {
            playerTwo = PlayerBuilder.BuildPlayer(PlayerBuilder.SIMPLE_AI_PLAYER, this);
        }

        playerOne.setName(player1NameField.getText());
        playerTwo.setName(player2NameField.getText());

        game = new Dominoes(this, playerOne, playerTwo, pointsToWin, maxDots);

        DominoPlayer winner = game.play(); // This will block until game is over

        // Congratulate winner once game ends
        this.statusLabel.setText(winner.getName() + " WINS GAME!");

        // Set winner's name
        displayWinnersName(winner);
    }

    @FXML
    private void drawButtonAction(ActionEvent event) {
        setDrawButtonDisable(true);
    }

    @FXML
    private void quitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    // DominoUI Methods
    @Override
    public void display(DominoPlayer[] players, Table table, BoneYard boneyard) {

        // Information in each DominoPlayer used by UI
        this.playerOne = (PlayerProxy) players[0];
        this.playerTwo = (PlayerProxy) players[1];

        this.setTable(table);
        this.setBoneyard(boneyard);

        updateStatusBarInfo();
        updateTableBonesBox();
    }

    @Override
    public void displayRoundWinner(DominoPlayer player) {
        // Show round win graphics
        System.out.println("!ROUND Winner! " + player.getName() + " Points: " + player.getPoints() + " Boneyard bones " + boneyard.size());
        this.statusLabel.setText(player.getName() + " WINS ROUND!");

        this.roundCount++;
    }

    @Override
    public void displayInvalidPlay(DominoPlayer player) {
        this.invalidMoveLabel.setText("Invalid move! Try again.");
        invalidMoveStatusShow();

    }

    private void displayWinnersName(DominoPlayer winner) {
        if (winner.equals(this.playerOne)) {
            this.player1PointsLabel.setText(Integer.toString(winner.getPoints()));
        } else if (winner.equals(this.playerTwo)) {
            this.player2PointsLabel.setText(Integer.toString(winner.getPoints()));
        }
    }

    private void updateStatusBarInfo() {
        // Set dashboard details
        this.roundCountLabel.setText(Integer.toString(this.roundCount));
        this.player1NameLabel.setText(this.playerOne.getName());
        this.player1PointsLabel.setText(Integer.toString(this.playerOne.getPoints()));
        this.player2NameLabel.setText(this.playerTwo.getName());
        this.player2PointsLabel.setText(Integer.toString(this.playerTwo.getPoints()));
        this.boneyardBoneCountLabel.setText(Integer.toString(this.boneyard.size()));

        if (!this.playerOne.getIsHuman()) {
            //ai1BoneCountLabel.setText(Integer.toString(this.playerOne.numInHand()));
        } else {
            ai1BoneCountLabel.setText("-");

        }

        if (!this.playerTwo.getIsHuman()) {
            //ai2BoneCountLabel.setText(Integer.toString(this.playerTwo.numInHand()));
        } else {
            ai2BoneCountLabel.setText("-");
        }

    }

    private void updateTableBonesBox() {
        tableBoneBucket.removeAllBones();

        int numBones = this.table.layout().length;
        Bone bones[] = this.table.layout();

        for (int i = 0; i < numBones; i++) {
            tableBoneBucket.addBone(bones[i]);
        }
    }

    public void updatePlayerBonesBox(DominoPlayer player) {
        playerBoneBucket.removeAllBones();
        playerBoneBucket.setBadgeName(player.getName());

        int numBones = player.bonesInHand().length;
        Bone bones[] = player.bonesInHand();

        for (int i = 0; i < numBones; i++) {
            playerBoneBucket.addBone(bones[i]);
        }
    }

    /**
     * This method is called by human players to action their play.
     *
     * @param player The current player
     * @param table The current table layout
     * @return A Play object with the played Bone and side (Play.LEFT or Play.RIGHT)
     */
    public Play getPlay(DominoPlayer player, Table table) {

        String playerName;
        playerName = player.getName();
        this.statusLabel.setText(playerName + "'s Turn");
        this.playerBoneBucket.titleCard.setText(playerName);
        this.playerBoneBucket.removeAllBones();

        // Display modal prompt here for human player to show his bones and play
        // We check if it's not the same player in case it was just an invalid play needing a replay
        // or the fact that the other player is AI hence no need to hide human player's bones.
        if ((lastPlayer != player) & (lastPlayer != null)) {
            PlayerProxy lastPlayerProxy = (PlayerProxy) lastPlayer;
            // Only hide current player's bones if last (other) player is also a human.
            if (lastPlayerProxy.getIsHuman()) {
                showNextPlayerPrompt();
            }
            this.invalidMoveLabel.setText("");
        }

        lastPlayer = player;

        this.currentPlayer = (PlayerProxy) player;

        updatePlayerBonesBox(this.currentPlayer);
        updateTable(table);

        printOptions(player);

        Boolean boneDropped = false;

        // Dummy stage - We just want to wait for drag'n'drop before returning WITHOUT BLOCKING any other windows.
        makePlayShowAndWaitStage = new Stage();
        makePlayShowAndWaitStage.initModality(Modality.NONE);
        makePlayShowAndWaitStage.showAndWait();

        return new Play(getPlayMade(), getPlaySideMade());
    }

    public void delayAIPlay(DominoPlayer player, Table table) {
        lastPlayer = player;
        this.currentPlayer = (PlayerProxy) player;
        updatePlayerBonesBox(this.currentPlayer);
        updateTable(table);

        lastPlayer = player;
        if ((lastPlayer != player) || (lastPlayer == null)) {
            this.invalidMoveLabel.setText("");
        }

        this.statusLabel.setText(player.getName() + "'s Turn (AI)");
        fadeInLabel(this.statusLabel);
    }

    private void invalidMoveStatusShow() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), invalidMoveLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);

        ft.play();

        ft.setDuration(Duration.millis(10000));
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ft.playFromStart();
    }

    private void fadeInLabel(Label label) {
        FadeTransition ft = new FadeTransition(Duration.millis(5000), label);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);

        ft.play();
    }

    private void showNextPlayerPrompt() {
        if (nextPlayerPromptStage != null) {
            nextPlayerPromptStage.showAndWait();
        } else {
            setupDialogues();
            nextPlayerPromptStage.showAndWait();
        }

        nextPlayerPromptStage.close();
        nextPlayerPromptStage = null;
    }

    private void setupDialogues() {
        nextPlayerPromptStage = new Stage();
        nextPlayerPromptStage.initModality(Modality.APPLICATION_MODAL);

        nextPlayerPromptStage.initStyle(StageStyle.TRANSPARENT);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("TextBoxControl.fxml"));

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            nextPlayerPromptStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void submitPlay() {
        if (makePlayShowAndWaitStage != null) {
            makePlayShowAndWaitStage.close();
            makePlayShowAndWaitStage = null;
        }
    }

    private BoneYard getBoneyard() {
        return boneyard;
    }

    private void setBoneyard(BoneYard boneyard) {
        this.boneyard = boneyard;
    }

    private void setTable(Table table) {
        this.table = table;
    }

    public Bone getPlayMade() {
        return playMade;
    }

    public void setPlayMade(Bone draggedBone) {
        this.playMade = draggedBone;
    }

    public int getPlaySideMade() {
        return playSideMade;
    }

    public void setPlaySideMade(int playSideMade) {
        this.playSideMade = playSideMade;
    }

    // Console output methods (mainly for AI play which doesn't use the GUI).
    private void printBone(Bone bone) {
        System.out.print("[" + bone.left() + "," + bone.right() + "]");
    }

    public void updateTable(Table table) {
        Bone[] layout = table.layout();
        System.out.print("Table: ");
        for (Bone eachBone : layout) {
            printBone(eachBone);
            System.out.print(" ");
        }
        System.out.print("\n ");
        
    }

    private void printOptions(DominoPlayer player) {
        System.out.println("Player options: ");
        Bone[] hand = player.bonesInHand();
        int option = 0;
        for (Bone eachBone : hand) {
            System.out.print(" " + option + ": ");
            printBone(eachBone);
            option++;
        }
        System.out.print("\n ");
    }
}
