package sounds;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Created by Amina on 03.07.2016.
 */
public class Sound { public static void playButtonSound() {
    try {
        File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\button-3.wav");
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        clip.start();
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

    public static void playDiceRollSound() {
        try {
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\Shake.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\Cinematic.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\offer.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\cancel.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\notification.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\ready.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\trade.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\Card.wav");
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
            File file = new File("E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\decline.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}



