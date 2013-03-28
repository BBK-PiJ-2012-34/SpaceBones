package dominoes.gui.sound;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * MusicPlayer
 * 
 * @author Hisham Khalifa
 */
public class MusicPlayer {

    
    // streetshark.mp3 is licensed under Creative Commons for Non-Commercial Use.
    // Artist Attribution: spacefish Source URI: http://www.newgrounds.com/audio/listen/528903
    // finalboss.mp3 is licensed under Creative Commons for Non-Commercial Use.
    // Artist Attribution: cmprerry1984 Source URI: http://www.newgrounds.com/audio/listen/528900
    
    final private static String soundOne = "sounds/streetshark.mp3";
    final private static String soundTwo = "sounds/finalboss.mp3";
    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;

    public MusicPlayer() {
        initialize();
        playMusic();
    }

    private void initialize() {
        File file = new File(soundOne);
        String string = file.toURI().toString();
        Media sound = new Media(string);

        mediaPlayer1 = new MediaPlayer(sound);
        
        mediaPlayer1.setCycleCount(500);

        file = new File(soundTwo);
        string = file.toURI().toString();
        sound = new Media(string);

        mediaPlayer2 = new MediaPlayer(sound);

        mediaPlayer2.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer2.play();
            }
        });
    }
   
   public void playMusic() {
       mediaPlayer1.play();
   }
    
    public void skipToSecondSong() {
        if (mediaPlayer1.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer1.stop();
        }

        mediaPlayer2.seek(Duration.ZERO);
        mediaPlayer2.play();
    }
}
