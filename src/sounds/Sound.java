package sounds;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

/**
 * Created by Amina on 03.07.2016.
 */
public class Sound { public static void playButtonSound() {
    try {
        URL url = Sound.class.getResource("/sounds/button-3.wav");
        File file = new File(url.getPath());
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        clip.start();
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

    public static void playDiceRollSound() {
        try {URL url = Sound.class.getResource("/sounds/Shake.wav");
            File file = new File(url.getPath());

            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playMoveRobberSound() {
        try {
            URL url = Sound.class.getResource("/sounds/Cinematic.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playMakeOfferSound() {
        try {
            URL url = Sound.class.getResource("/sounds/offer.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playCancelOfferSound() {
        try {
            URL url = Sound.class.getResource("/sounds/cancel.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playNotificationSound() {
        try {
            URL url = Sound.class.getResource("/sounds/notification.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playReadySound() {
        try {
            URL url = Sound.class.getResource("/sounds/ready.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    public static void playTradeButtonSound() {
        try {
            URL url = Sound.class.getResource("/sounds/trade.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void playCardButtonSound() {
        try {
            URL url = Sound.class.getResource("/sounds/Card.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public static void playDeclineTradeOfferSound() {
        try {
            URL url = Sound.class.getResource("/sounds/decline.wav");
            File file = new File(url.getPath());
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}



