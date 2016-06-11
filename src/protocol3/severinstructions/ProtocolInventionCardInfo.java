package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import enums.ResourceType;
import protocol.object.ProtocolResource;
import protocol3.object.ProtocolInventionCard;

/**
 * <b>Erfindung(invention card)12.4 </b>
 * <p>
 * Server sends to all clients the information, in order to make the process
 * understandable. (player can get two different resource types)
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolInventionCardInfo {

    @SerializedName("Rohstoffe")
    private ResourceType resourceType;

    @SerializedName("Spieler")
    private Integer player_id;

    //Client to Sever
    public ProtocolInventionCardInfo(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    //Server to Cliet
    public ProtocolInventionCardInfo(ResourceType resourceType, Integer player_id) {

        this.resourceType = resourceType;
        this.player_id = player_id;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getPlayer_id() {
        return player_id;
    }
}
