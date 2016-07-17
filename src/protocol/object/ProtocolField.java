package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.unit.Index;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol field.
	 *
	 * @param fieldID the field ID
	 * @param fieldType the field type
	 * @param diceIndex the dice index
	 */
	public ProtocolField(Index fieldID, String fieldType, Integer diceIndex) {
		this.fieldID = fieldID;
		this.fieldType = fieldType;
		this.diceIndex = diceIndex;
	}

	/**
	 * Gets the field ID.
	 *
	 * @return the field ID
	 */
	public Index getFieldID() {
		return fieldID;
	}

	/**
	 * Gets the field type.
	 *
	 * @return the field type
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * Gets the dice index.
	 *
	 * @return the dice index
	 */
	public Integer getDiceIndex() {
		return diceIndex;
	}

}
