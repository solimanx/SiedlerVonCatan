package enums;

import com.google.gson.annotations.SerializedName;

// TODO: Auto-generated Javadoc
public enum Color {
	@SerializedName("Rot") RED(javafx.scene.paint.Color.RED),

	@SerializedName("Orange") ORANGE(javafx.scene.paint.Color.ORANGE),

	@SerializedName("Blau") BLUE(javafx.scene.paint.Color.DODGERBLUE),

	@SerializedName("Weiß") WHITE(javafx.scene.paint.Color.WHITESMOKE),
	
	@SerializedName("Grün") GREEN(javafx.scene.paint.Color.GREEN),
	
	@SerializedName("Braun") BROWN(javafx.scene.paint.Color.BROWN);

	private javafx.scene.paint.Color value;

	/**
	 * Instantiates a new color.
	 *
	 * @param color the color
	 */
	Color(javafx.scene.paint.Color color) {
		this.value = color;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public javafx.scene.paint.Color getValue() {
		return value;
	}

}
