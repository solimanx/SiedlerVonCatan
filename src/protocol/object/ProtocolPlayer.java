package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.Color;
import enums.PlayerState;
import protocol3.clientinstructions.ProtocolDevelopmentCards;
import protocol3.serverinstructions.ProtocolPlayKnightCard;

/**
 * <b>Spieler (Player)</b>
 * <p>
 * Contains information about player: player ID, color, name , status, score,
 * and resources.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolPlayer {

	@SerializedName("id")
	private int playerID;

	@SerializedName("Farbe")
	private Color color;

	@SerializedName("Name")
	private String name;

	@SerializedName("Status")
	private PlayerState status;

	@SerializedName("Siegpunkte")
	private int victoryPoints;

	@SerializedName("Rohstoffe")
	private ProtocolResource resources;

	//@SerializedName("Rittermacht")
	//private ProtocolPlayKnightCard playKnightCard;
	//nur ausgespielte Rittter Karten

	//@SerializedName("Entwicklungskarten")
	//private ProtocolDevelopmentCards developmentCards;

	public ProtocolPlayer(int playerID, Color color, String name, PlayerState status, int victoryPoints,
			ProtocolResource resources) {
		this.playerID = playerID;
		this.color = color;
		this.name = name;
		this.status = status;
		this.victoryPoints = victoryPoints;
		this.resources = resources;
	//	this.developmentCards=developmentCards;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public PlayerState getStatus() {
		return status;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public ProtocolResource getResources() {
		return resources;
	}

	//public ProtocolDevelopmentCards getDevelopmentCards() {
	//	return developmentCards;
	//}
}
