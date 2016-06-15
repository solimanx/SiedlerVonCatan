package protocol.dualinstructions;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import enums.ResourceType;

/**
 * <b>12.3 Monopol (monopoly card ) </b>
 * <p>
 * only one resource should be specified while playing the monopoly card(client
 * sends to server) Server sends to client Information, which player played the
 * card.
 * </p>
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolPlayMonopolyCard {

	@SerializedName("Spieler")
	private Integer player_id;

	@SerializedName("Rohstoff")
	private ResourceType resourceType;

	// Client to Server
	public ProtocolPlayMonopolyCard(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	// Server to Client
	public ProtocolPlayMonopolyCard(Integer player_id, ResourceType resourceType) {
		this.player_id = player_id;
		this.resourceType = resourceType;

	}

	public Integer getPlayer_id() {
		return player_id;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}
}
