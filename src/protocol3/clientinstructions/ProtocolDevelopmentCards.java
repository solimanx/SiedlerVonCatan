package protocol3.clientinstructions;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.ProtocolPlayKnightCard;

/**
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolDevelopmentCards {

    @SerializedName("Ritter")
    private Integer knight;

    @SerializedName("Straßenbau")
    private Integer roadbuild;

    @SerializedName("Monopol")
    private Integer monopoly;

    @SerializedName("Erfindung")
    private Integer invention;

    @SerializedName("Unbekannt")
    private Integer unknown;

    public ProtocolDevelopmentCards(Integer knight, Integer roadbuild, Integer monopoly, Integer invention,
                                    Integer unknown) {
        this.knight = knight;
        this.roadbuild = roadbuild;
        this.monopoly = monopoly;
        this.invention = invention;
        this.unknown = unknown;

    }

    public Integer getKnight() {
        return knight;
    }

    public Integer getRoadbuild() {
        return roadbuild;
    }

    public Integer getMonopoly() {
        return monopoly;
    }

    public Integer getInvention() {
        return invention;
    }

    public Integer getUnknown() {
        return unknown;
    }

}
