package audio;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Soundeffects {

	public static double globalVolume = 0.5;
	public static SimpleBooleanProperty globalVolumeBoolean = new SimpleBooleanProperty(true);

	public static ObjectProperty<Image> volOn = new SimpleObjectProperty<Image>(new Image("/textures/vol_up.png"));
	public static ObjectProperty<Image> volOff = new SimpleObjectProperty<Image>(new Image("/textures/vol_mute.png"));

	public static ObjectProperty<Image> getVolumeGraphicProperty() {
		return globalVolume == 0.0 ? volOff : volOn;
	}

	public static void playSoundtrack1() {
		if (SOUNDTRACK2.isPlaying()) {
			SOUNDTRACK2.stop();
		}
		SOUNDTRACK1.play();
	}

	public static void playSoundtrack2() {
		if (SOUNDTRACK1.isPlaying()) {
			SOUNDTRACK1.stop();
		}
		SOUNDTRACK2.play();
	}

	public static void setVolume(double volume) {
		globalVolume = volume;
	}

	public static void toggleVolume() {
		globalVolume = globalVolume > 0.0 ? 0.0 : 0.5;
	}

	public final static AudioClip BUILD = new AudioClip(Soundeffects.class.getResource("/audio/build.wav").toString());
	public final static AudioClip BUYCARD = new AudioClip(
			Soundeffects.class.getResource("/audio/buycard.mp3").toString());
	public final static AudioClip DICEROLL = new AudioClip(
			Soundeffects.class.getResource("/audio/diceroll.mp3").toString());
	// public final static AudioClip ERROR = new
	// AudioClip(Soundeffects.class.getResource("/audio/error.wav").toString());
	public final static AudioClip HARBOUR = new AudioClip(
			Soundeffects.class.getResource("/audio/harbour.wav").toString());
	public final static AudioClip SELECT = new AudioClip(Soundeffects.class.getResource("/audio/login.wav").toString());
	public final static AudioClip RESOURCE = new AudioClip(
			Soundeffects.class.getResource("/audio/resource.mp3").toString());
	public final static AudioClip ROBBER = new AudioClip(
			Soundeffects.class.getResource("/audio/robber.mp3").toString());
	public final static AudioClip SWORD = new AudioClip(Soundeffects.class.getResource("/audio/sword.mp3").toString());
	public final static AudioClip VICTORY = new AudioClip(
			Soundeffects.class.getResource("/audio/victory.mp3").toString());
	public final static AudioClip CHATRECEIVE = new AudioClip(
			Soundeffects.class.getResource("/audio/chatreceive.mp3").toString());
	public final static AudioClip LONGESTROAD = new AudioClip(
			Soundeffects.class.getResource("/audio/longestroad.wav").toString());
	public final static AudioClip SOUNDTRACK1 = new AudioClip(
			Soundeffects.class.getResource("/audio/siedlersoundtrack1.mp3").toString());
	public final static AudioClip SOUNDTRACK2 = new AudioClip(
			Soundeffects.class.getResource("/audio/siedlersoundtrack2.mp3").toString());

	public static boolean isMuted() {
		return !globalVolumeBoolean.get();

	}


	public static void toggleMuteOnOff() {
		if (globalVolumeBoolean.get())
			globalVolumeBoolean.set(false);
		else
			globalVolumeBoolean.set(true);
	}

}
