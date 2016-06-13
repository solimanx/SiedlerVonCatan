package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.HarbourStatus;

/**
 * <b>Häfen (Harbours)</b>
 * <p>
 * Contains information about harbour: ID/location, and harbour type.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolHarbour {

	@SerializedName("Ort")
	private String locationID;

	@SerializedName("Typ")
	private HarbourStatus harbour_type;

	public ProtocolHarbour(String locationID, HarbourStatus harbourType) {
		this.locationID = locationID;
		this.harbour_type = harbourType;
	}

	public String getID() {
		return locationID;
	}

	public HarbourStatus getType() {
		return harbour_type;
	}

}
