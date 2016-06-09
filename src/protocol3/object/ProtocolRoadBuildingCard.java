package protocol3.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolRoadBuildingCard {

	@SerializedName("Straße 1")
	String road1_id;
	@SerializedName("Straße 2")
	String road2_id;

	public ProtocolRoadBuildingCard(String road1_id, String road2_id) {
		this.road1_id = road1_id;
		this.road2_id = road2_id;

	}

	public String getRoad1_id() {

		return road1_id;
	}

	public String getRoad2_id() {
		return road2_id;
	}

}
