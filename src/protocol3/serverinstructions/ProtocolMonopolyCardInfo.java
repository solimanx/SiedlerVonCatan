package protocol3.serverinstructions;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import enums.ResourceType;
import protocol.object.ProtocolResource;
import protocol3.object.ProtocolMonopolyCard;


/**
 * <b>12.3 Monopol (monopoly card ) </b>
 * <p>
 * only one resource should be specified while playing the monopoly card(client sends to server)
 * Server sends to client Information, which player played the card.
 * </p>
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolMonopolyCardInfo {

    @SerializedName("Rohstoff")
    private ResourceType resourceType;

    @SerializedName("Spieler")
    private Integer player_id;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getPlayer_id() {
        return player_id;
    }

    //Client to Server
    public ProtocolMonopolyCardInfo(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    //Server to Client
    public ProtocolMonopolyCardInfo(ResourceType resourceType, Integer player_id) {

        this.resourceType = resourceType;
        this.player_id = player_id;
    }
}
