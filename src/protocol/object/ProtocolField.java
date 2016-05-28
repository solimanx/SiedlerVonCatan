package protocol.object;

import com.google.gson.annotations.SerializedName;

public class ProtocolField {

	@SerializedName("Ort")
	private String field_id;

	@SerializedName("Typ")
	private String field_type;

	@SerializedName("Zahl")
	private int dice_index;


	public ProtocolField(String field_id, String field_type, int dice_index) {
		this.field_id = field_id;
		this.field_type = field_type;
		this.dice_index = dice_index;
	}

	public String getFieldID() {
		return field_id;
	}

	public String getFieldType()  {
		return field_type;
	}

	public int getDiceIndex()  {
		return dice_index;
	}

}
