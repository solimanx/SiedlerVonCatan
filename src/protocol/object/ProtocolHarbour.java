package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.HarbourStatus;
import model.unit.Index;

/**
 * <b>Häfen (Harbours)</b>
 * <p>
 * Contains information about harbour: ID/location, and harbour type.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolHarbour {

	@SerializedName("Ort")
	private Index[] locationID; // length 2

	@SerializedName("Typ")
	private HarbourStatus harbour_type;

	/**
	 * Instantiates a new protocol harbour.
	 *
	 * @param locationID the location ID
	 * @param harbourType the harbour type
	 */
	public ProtocolHarbour(Index[] locationID, HarbourStatus harbourType) {
		this.locationID = locationID;
		this.harbour_type = harbourType;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Index[] getID() {
		return locationID;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public HarbourStatus getType() {
		return harbour_type;
	}

}
