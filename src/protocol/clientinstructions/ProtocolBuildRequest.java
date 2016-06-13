package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Bauen (Build request)</b>
 * <p>
 * Client sends server request to build building.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolBuildRequest {

	@SerializedName("Typ")
	private String building_type;

	@SerializedName("Ort")
	private String locationID;

	public ProtocolBuildRequest(String building_type, String locationID) {
		this.building_type = building_type;
		this.locationID = locationID;
	}

	public String getBuildingType() {
		return building_type;
	}

	public String getLocationID() {
		return locationID;
	}

}
