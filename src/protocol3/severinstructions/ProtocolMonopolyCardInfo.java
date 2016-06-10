package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import protocol.object.ProtocolResource;
import protocol3.object.ProtocolMonopolyCard;

/**12.3
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolMonopolyCardInfo {

    @SerializedName("Rohstoff")
    private ProtocolResource resource;

    @SerializedName("Spieler")
    private Integer player_id;

    public ProtocolResource getResource() {
        return resource;
    }

    public Integer getPlayer_id() {
        return player_id;
    }

    //Client to Server
    public ProtocolMonopolyCardInfo(ProtocolResource resource) {
        this.resource = resource;
    }

    //Server to Client
    public ProtocolMonopolyCardInfo(ProtocolResource resource, Integer player_id) {

        this.resource = resource;
        this.player_id = player_id;
    }
}
