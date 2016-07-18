package sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import audio.Soundeffects;

import java.io.File;
import java.net.URL;

// TODO: Auto-generated Javadoc

/**
 * Created by Amina on 03.07.2016.
 */
public class Sound {

    static float globalVolume = 0.5f;

    public static void setGain(Clip clip, float globalVolume) {
        if (globalVolume != -1) {
            if ((globalVolume < 0) || (globalVolume > 1)) {
                throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
            }
        }

        Sound.globalVolume = globalVolume;

        try {
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (globalVolume == -1) {
                control.setValue(0);
            } else {
                float max = control.getMaximum();
                float min = control.getMinimum();
                float range = max - min;

                control.setValue(min + (range * globalVolume));
            }
        } catch (IllegalArgumentException e) {
            // gain not supported
            e.printStackTrace();
        }
    }


    /**
     * Play connect sound.
     */
    public static void playConnectSound() {
        try {
            URL url = Sound.class.getResource("/sounds/button-3.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
//			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
//			gainControl.setValue(-20.0f);
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play dice roll sound.
     */
    @Deprecated // see DICEROLL
    public static void playDiceRollSound() {
        try {
            URL url = Sound.class.getResource("/sounds/Shake.wav");
            File file = new File(url.getPath());

            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Play make offer sound.
     */
    public static void playMakeOfferSound() {
        try {
            URL url = Sound.class.getResource("/sounds/offer.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play cancel offer sound.
     */
    public static void playCancelOfferSound() {
        try {
            URL url = Sound.class.getResource("/sounds/cancel.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play notification sound.
     */
    @Deprecated
    public static void playNotificationSound() {
        try {
            URL url = Sound.class.getResource("/sounds/notification.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play ready sound.
     */
    @Deprecated
    public static void playReadySound() {
        try {
            URL url = Sound.class.getResource("/sounds/ready.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play trade button sound.
     */
    public static void playTradeButtonSound() {
        try {
            URL url = Sound.class.getResource("/sounds/trade.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play card button sound.
     */
    @Deprecated
    public static void playCardButtonSound() {
        try {
            URL url = Sound.class.getResource("/sounds/Card.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play decline trade offer sound.
     */
    public static void playDeclineTradeOfferSound() {
        try {
            URL url = Sound.class.getResource("/sounds/decline.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play cancel trade sound.
     */
    public static void playCancelTradeSound() {
        try {
            URL url = Sound.class.getResource("/sounds/cancel.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play full fill trade sound.
     */
    public static void playFullFillTradeSound() {
        try {
            URL url = Sound.class.getResource("/sounds/accept.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play winner sound.
     */
    public static void playWinnerSound() {
        try {
            URL url = Sound.class.getResource("/sounds/winnerSound.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Play click sound, when Player clicks on Help Button and Tabs on the Help View
     */
    public static void playHelpButtonSound() {
        try {
            URL url = Sound.class.getResource("/sounds/HelpViewButton.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            setGain(clip, globalVolume);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
