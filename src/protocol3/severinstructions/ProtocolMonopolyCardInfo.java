package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolResource;
import protocol3.object.ProtocolMonopolyCard;

/**
 * Created on 06.06.2016.
 */
public class ProtocolMonopolyCardInfo {

    @SerializedName("Rohstoff")
    private ProtocolResource resource;

    @SerializedName("Spielr")
    private int player_id;

    public ProtocolResource getResource() {
        return resource;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public ProtocolMonopolyCardInfo(ProtocolResource resource, int player_id) {

        this.resource = resource;
        this.player_id = player_id;
    }
}
