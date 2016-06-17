package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 * <b>Felder (Fields)</b>
 * <p>
 * Contains information about field: ID/location, land type and dice index.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolField {

	@SerializedName("Ort")
	private Index fieldID;

	@SerializedName("Typ")
	private String fieldType;

	@SerializedName("Zahl")
	private Integer diceIndex;

	public ProtocolField(Index fieldID, String fieldType, Integer diceIndex) {
		this.fieldID = fieldID;
		this.fieldType = fieldType;
		this.diceIndex = diceIndex;
	}

	public Index getFieldID() {
		return fieldID;
	}

	public String getFieldType() {
		return fieldType;
	}

	public Integer getDiceIndex() {
		return diceIndex;
	}

}
