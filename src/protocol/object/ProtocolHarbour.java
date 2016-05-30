package protocol.object;

import com.google.gson.annotations.SerializedName;

/**
 * <b>4.3 Häfen</b>
 * <p>
 * Contains information about harbour: ID/location, and harbour type.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolHarbour {

	@SerializedName("Ort")
	String location_id;

	@SerializedName("Typ")
	String harbour_type;

	public ProtocolHarbour(String location_id, String harbour_type) {
		this.location_id = location_id;
		this.harbour_type = harbour_type;
	}

	public String getID() {
		return location_id;
	}

	public String getType() {
		return harbour_type;
	}

}
