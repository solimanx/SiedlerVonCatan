package enums;

import com.google.gson.annotations.SerializedName;

public enum Color {
	@SerializedName("Rot") RED(javafx.scene.paint.Color.RED),

	@SerializedName("Orange") ORANGE(javafx.scene.paint.Color.ORANGE),

	@SerializedName("Blau") BLUE(javafx.scene.paint.Color.DODGERBLUE),

	@SerializedName("Weiß") WHITE(javafx.scene.paint.Color.WHITESMOKE);
	
	private javafx.scene.paint.Color value;
	
	Color(javafx.scene.paint.Color color){
		this.value = color;
	}
	
	public javafx.scene.paint.Color getValue(){
		return value;
	}

}
