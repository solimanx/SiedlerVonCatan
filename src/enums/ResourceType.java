package enums;

import com.google.gson.annotations.SerializedName;

public enum ResourceType {
	@SerializedName("Holz") WOOD, 
	
	@SerializedName("Lehm") CLAY, 
	
	@SerializedName("Erz") ORE,
	
	@SerializedName("Wolle") SHEEP,
	
	@SerializedName("Getreide") CORN,

	NOTHING, SEA, UNKNOWN
}
