package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;

import protocol3.object.ProtocolRoadBuildingCard;

/**
 * <b>Stra√üenbaukarte ausspielen(play the knight card)</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */
public class ProtocolRoadBuildingCardInfo {

    @SerializedName("Straﬂe")
    private ProtocolRoadBuildingCard road;

    public ProtocolRoadBuildingCardInfo (ProtocolRoadBuildingCard road) {
        this.road = road;

    }

    public ProtocolRoadBuildingCard getRoadBuildingCard() {

        return road;
    }

}
