package enums.protocol;

import com.google.gson.annotations.SerializedName;

public enum FieldType {
	@SerializedName("Wald") FOREST,

	@SerializedName("Hügelland") HILLS,

	@SerializedName("Gebirge") MOUNTAINS,

	@SerializedName("Weideland") PASTURE,

	@SerializedName("Ackerland") GRAIN,

	@SerializedName("Wüste") DESERT,

	@SerializedName("Meer") SEA
}
