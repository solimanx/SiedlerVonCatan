package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Ritter ausspielen(play the knight card)</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this card.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolPlayKnightCard {

    @SerializedName("Straße 1")
    String field_id;

    @SerializedName("Ziel")
    int target;

    public ProtocolPlayKnightCard(String field_id, int target) {

        this.field_id = field_id;
        this.target = target;
    }

    public String getField_id() {

        return field_id;
    }

    public int getTarget() {

        return target;
    }

}
