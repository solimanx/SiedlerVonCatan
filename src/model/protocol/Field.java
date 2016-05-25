package model.protocol;

import enums.protocol.FieldType;

public class Field {
	String field_id;
	FieldType field_type;
	int dice_index;

	public Field(String field_id, FieldType field_type, int dice_index) {
		this.field_id = field_id;
		this.field_type = field_type;
		this.dice_index = dice_index;
	}

	public String getField_id() {
		return field_id;
	}

	public FieldType getField_type() {
		return field_type;
	}

	public int getDice_index() {
		return dice_index;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public void setField_type(FieldType field_type) {
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
