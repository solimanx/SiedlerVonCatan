package enums.protocol;

import com.google.gson.annotations.SerializedName;

public enum HarbourType {
	@SerializedName("Hafen") THREE_TO_ONE,

	@SerializedName("Holz Hafen") WOOD,

	@SerializedName("Lehm Hafen") CLAY,

	@SerializedName("Erz Hafen") ORE,

	@SerializedName("Wolle Hafen") SHEEP,

	@SerializedName("Getreide Hafen") CORN,

}
