package enums;

import com.google.gson.annotations.SerializedName;

/**
 * @see parsing.objects.HarbourTypeAdapter
 */
public enum HarbourStatus {
	@SerializedName("Hafen") THREE_TO_ONE,

	@SerializedName("Holz Hafen") WOOD,

	@SerializedName("Lehm Hafen") CLAY,

	@SerializedName("Erz Hafen") ORE,

	@SerializedName("Wolle Hafen") SHEEP,

	@SerializedName("Getreide Hafen") CORN,

	NULL // TODO: serialization
}
