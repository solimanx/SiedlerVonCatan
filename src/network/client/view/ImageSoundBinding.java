package network.client.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageSoundBinding extends javafx.beans.binding.ObjectBinding<ImageView> {

	SimpleBooleanProperty soundStatus;

	public ImageSoundBinding(SimpleBooleanProperty soundStatus) {
		super.bind(soundStatus);
		this.soundStatus = soundStatus;
	}

	@Override
	protected ImageView computeValue() {
		try {
			String url = !soundStatus.get() ? "/textures/vol_on.png" : "/textures/vol_mute.png";
			ImageView imageView = new ImageView(new  Image(url));
			return imageView;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
