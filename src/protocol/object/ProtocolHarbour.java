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
	private String location_id;

	@SerializedName("Typ")
	private HarbourStatus harbour_type;

	public ProtocolHarbour(String location_id, HarbourStatus harbour_type) {
		this.location_id = location_id;
		this.harbour_type = harbour_type;
	}

	public String getID() {
		return location_id;
	}

	public HarbourStatus getType() {
		return harbour_type;
	}

}
