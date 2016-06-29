package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

// TODO: Auto-generated Javadoc
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
	private Index[] locationID;

	/**
	 * Instantiates a new protocol build request.
	 *
	 * @param building_type the building type
	 * @param location the location
	 */
	public ProtocolBuildRequest(String building_type, Index[] location) {
		this.building_type = building_type;
		this.locationID = location;
	}

	/**
	 * Gets the building type.
	 *
	 * @return the building type
	 */
	public String getBuildingType() {
		return building_type;
	}

	/**
	 * Gets the location ID.
	 *
	 * @return the location ID
	 */
	public Index[] getLocationID() {
		return locationID;
	}

}
