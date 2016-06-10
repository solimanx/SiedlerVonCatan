package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
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
    private ProtocolResource resource;

    @SerializedName("Spieler")
    private Integer player_id;

    //Client to Sever
    public ProtocolInventionCardInfo(ProtocolResource resource) {
        this.resource = resource;
    }

    //Server to Cliet
    public ProtocolInventionCardInfo(ProtocolResource resource, Integer player_id) {

        this.resource = resource;
        this.player_id = player_id;
    }

    public ProtocolResource getResource() {
        return resource;
    }

    public Integer getPlayer_id() {
        return player_id;
    }
}
