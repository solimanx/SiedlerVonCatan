package protocol3.clientinstructions;

import com.google.gson.annotations.SerializedName;

import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.ProtocolPlayKnightCard;

/**
 * Created on 06.06.2016.
 */

public class ProtocolDevelopmentCards {

	@SerializedName("Ritter")
	private ProtocolPlayKnightCard knight;

	@SerializedName("Straﬂenbau")
	private ProtocolRoadBuildingCard roadbuild;

	@SerializedName("Monopol")
	private ProtocolMonopolyCard monopoly;

	@SerializedName("Erfindung")
	private ProtocolInventionCard invention;

	public ProtocolDevelopmentCards(ProtocolPlayKnightCard knight, ProtocolRoadBuildingCard roadbuild,
			ProtocolMonopolyCard monopoly, ProtocolInventionCard invention) {

		this.knight = knight;
		this.roadbuild = roadbuild;
		this.monopoly = monopoly;
		this.invention = invention;
	}

	public ProtocolPlayKnightCard getKnight() {
		return knight;
	}

	public ProtocolRoadBuildingCard getRoadbuild() {
		return roadbuild;
	}

	public ProtocolMonopolyCard getMonopoly() {
		return monopoly;
	}

	public ProtocolInventionCard getInvention() {
		return invention;
	}

}
