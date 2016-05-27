package protocol.object;

import com.google.gson.annotations.SerializedName;

import enums.protocol.FieldType;

public class ProtocolField {

	@SerializedName("Ort")
	private String field_id;

	@SerializedName("Typ")
	private FieldType field_type;

	@SerializedName("Zahl")
	private int dice_index;

	private enum FieldType {

		@SerializedName("Ackerland") FARMLAND,

		@SerializedName("Hügelland") HILL,

		@SerializedName("Wald") FOREST,

		@SerializedName("Gebirge") MOUNTAIN,

		@SerializedName("Wüste") DESERT,

		@SerializedName("Meer") SEA

	}

	public ProtocolField(String field_id, FieldType field_type, int dice_index) {
		this.field_id = field_id;
		this.field_type = field_type;
		this.dice_index = dice_index;
	}

	public String getFieldID() {
		return field_id;
	}

	public FieldType getFieldType()  {
		return field_type;
	}

	public int getDiceIndex()  {
		return dice_index;
	}

}
