package enums.protocol;

import com.google.gson.annotations.SerializedName;

public enum BuildingType {
	@SerializedName("Straße") ROAD,

	@SerializedName("Dorf") VILLAGE,

	@SerializedName("Stadt") CITY
}
