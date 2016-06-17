package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 * <b>Bauen (Build request)</b>
 * <p>
 * Client sends server request to build building.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolBuildRequest {

	@SerializedName("Typ")
	private String building_type;

	@SerializedName("Ort")
	private Index locationID;

	public ProtocolBuildRequest(String building_type, Index locationID) {
		this.building_type = building_type;
		this.locationID = locationID;
	}

	public String getBuildingType() {
		return building_type;
	}

	public Index getLocationID() {
		return locationID;
	}

}
