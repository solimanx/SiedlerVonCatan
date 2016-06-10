package protocol3.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * Created on 10.06.2016.
 */
@Since(0.3)
public class ProtocolKnightCard {

    @SerializedName("Ort")
    private String location_id;
    @SerializedName("Ziel")
    private int target;

    public ProtocolKnightCard(String location_id, int target) {
        this.location_id = location_id;
        this.target = target;
    }

    public String getLocation_id() {
        return location_id;
    }

    public int getTarget() {
        return target;
    }
}
