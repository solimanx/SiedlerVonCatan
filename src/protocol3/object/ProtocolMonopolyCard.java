package protocol3.object;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import protocol.object.ProtocolResource;

/**
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolMonopolyCard {

    @SerializedName("Rohstoffe")
    ProtocolResource resource;

    public ProtocolMonopolyCard(ProtocolResource resource) {
        this.resource = resource;

    }

    public ProtocolResource getResource() {
        return resource;

    }
}
