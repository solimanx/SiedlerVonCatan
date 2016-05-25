package enums.protocol;

import com.google.gson.annotations.SerializedName;

public enum ResourceType {
	@SerializedName("Wald") WOOD,

	@SerializedName("Hügelland") CLAY,

	@SerializedName("Gebirge") ORE,

	@SerializedName("Weideland") SHEEP,

	@SerializedName("Ackerland") CORN,

	@SerializedName("Meer") SEA,
	
	@SerializedName("Unbekannt") UNKNOWN
}
