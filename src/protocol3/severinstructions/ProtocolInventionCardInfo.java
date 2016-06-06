package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import protocol.object.ProtocolResource;

/**
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolInventionCardInfo {

    @SerializedName("Rohstoffe")
    ProtocolResource resource;

    @SerializedName("Spieler")
    int player_id;

    public ProtocolInventionCardInfo(ProtocolResource resource, int player_id) {
        this.resource = resource;
        this.player_id = player_id;
    }

}
