package protocol.object;

import com.google.gson.annotations.SerializedName;

import enums.protocol.FieldType;

public class ProtocolField {

	@SerializedName("Ort")
	String field_id;

	@SerializedName("Typ")
	String field_type;

	@SerializedName("Zahl")
	int dice_index;

	public ProtocolField(){
	}

	public String getField_id() {
		return field_id;
	}

	public String getField_type() {
		return field_type;
	}

	public int getDice_index() {
		return dice_index;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	public void setDice_index(int dice_index) {
		this.dice_index = dice_index;
	}

	@Override
	public String toString() {
		return "Field [field_id=" + field_id + ", field_type=" + field_type + ", dice_index=" + dice_index + "]";
	}
}
