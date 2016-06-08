package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;

import protocol3.object.ProtocolMonopolyCard;

/**
 * Created on 06.06.2016.
 */
public class ProtocolMonopolyCardInfo {

	@SerializedName("Monopol")
	private ProtocolMonopolyCard monopoly;

	public ProtocolMonopolyCardInfo(ProtocolMonopolyCard monopoly) {

		this.monopoly = monopoly;
	}

	public ProtocolMonopolyCard getMonopolyCard() {
		return monopoly;
	}

}
