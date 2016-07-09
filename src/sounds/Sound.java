package sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.io.File;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * Created by Amina on 03.07.2016.
 */
public class Sound {

	/**
	 * Music loop.
	 */
	public static void musicLoop() {

		MediaPlayer mediaPlayer = new MediaPlayer(
				new Media(Sound.class.getResource("/sounds/background.wav").toExternalForm()));
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();

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
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-20.0f);
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
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play move robber sound.
	 */
	@Deprecated // see BANDIT
	public static void playMoveRobberSound() {
		try {
			URL url = Sound.class.getResource("/sounds/Cinematic.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
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
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
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
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play notification sound.
	 */
	public static void playNotificationSound() {
		try {
			URL url = Sound.class.getResource("/sounds/notification.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play ready sound.
	 */
	public static void playReadySound() {
		try {
			URL url = Sound.class.getResource("/sounds/ready.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
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
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play card button sound.
	 */
	public static void playCardButtonSound() {
		try {
			URL url = Sound.class.getResource("/sounds/Card.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
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
			// Thread.sleep(clip.getMicrosecondLength());
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
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play accept trade sound.
	 */
	public static void playAcceptTradeSound() {
		try {
			URL url = Sound.class.getResource("/sounds/accept.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
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
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play select sound.
	 */
	@Deprecated // see BUILD
	public static void playSelectSound() {
		try {
			// this sound doesn't exist..
			URL url = Sound.class.getResource("/sounds/select.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
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
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Play alert sound.
	 */
	public static void playAlertSound() {
		try {
			URL url = Sound.class.getResource("/sounds/invalidAction.wav");
			File file = new File(url.getPath());
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			// Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
