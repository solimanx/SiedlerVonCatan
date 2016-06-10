package enums;

import com.google.gson.annotations.SerializedName;

public enum HarbourStatus {
	@SerializedName("Hafen") THREE_TO_ONE, 
	
	@SerializedName("Holz Hafen") WOOD,

	@SerializedName("Lehm Hafen") CLAY,

	@SerializedName("Erz Hafen") ORE,

	@SerializedName("Wolle Hafen") SHEEP,

	@SerializedName("Getreide Hafen") CORN,

	@SerializedName("NULL") NULL
	



}
