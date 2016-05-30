package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;

/**
 * <b>8.2 Bauen </b>
 * <p>
 * Client sends server request to build building.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolBuildRequest {

	@SerializedName("Typ")
	private String building_type;

	@SerializedName("Ort")
	private String location_id;

	public ProtocolBuildRequest(String building_type, String location_id) {
		this.building_type = building_type;
		this.location_id = location_id;
	}

	public String getBuilding() {
		return building_type;
	}

	public String getLocation() {
		return location_id;
	}

}
