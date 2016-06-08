package protocol3.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 06.06.2016.
 */
public class ProtocolRoadBuildingCard {
	
	   @SerializedName("Straße 1")
	    String road1_id;
	    @SerializedName("Straße 2")
	    String road2_id;
	    @SerializedName("Spieler")
	    int player_id;

	    public ProtocolRoadBuildingCard(String road1_id, String road2_id, int player_id) {
	        this.road1_id = road1_id;
	        this.road2_id = road2_id;
	        this.player_id = player_id;
	    }

	    public String getRoad1_id() {

	        return road1_id;
	    }

	    public String getRoad2_id() {
	        return road2_id;
	    }

	    public int getPlayer_id() {
	        return player_id;
	    }
}
