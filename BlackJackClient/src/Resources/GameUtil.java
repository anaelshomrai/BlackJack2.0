package Resources;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class contains some methods that the application use in different classes.
 * WinPot - A sound plays when a winning pop-up frame created.
 * playShuffle - A sound plays when cards are dealt in the game.
 * setIcon - an icon is set to the specific frame.
 * 
 * @author ANI
 */
public class GameUtil {

    /**
     * When calling this method a short sound is played.
     */
    public static void winPot() {
        AudioInputStream audioInputStream = null;

        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File("./src/audio/slot_machine.wav"));
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        FloatControl gainControl
                = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(+6.0206f);

        clip.start();
    }

    /**
     * When calling this method a short sound is played.
     */
    public static void playShuffle() {
        AudioInputStream audioInputStream = null;

        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File("./src/audio/cardFan.wav"));
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        FloatControl gainControl
                = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(+6.0206f); // Reduce volume by 10 decibels.
        clip.start();
    }

    /**
     * This method sets the icon of the received frame.
     * 
     * @param frame the frame
     */
    public static void setIcon(JFrame frame) {
        String file = "./src/img/card-symbols_b.png";
        ImageIcon img = new ImageIcon(file);
        frame.setIconImage(img.getImage());
    }


}
