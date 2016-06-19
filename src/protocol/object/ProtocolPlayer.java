package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.Color;
import enums.PlayerState;
import protocol.dualinstructions.ProtocolPlayKnightCard;

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

	@SerializedName("Entwicklungskarten")
	private ProtocolDevCard developmentCards;
	
	@SerializedName("Größte Rittermacht")
	private boolean largestArmy;
	
	@SerializedName("Größte Handelsstraße")
	private boolean longestRoad;
	
	
	public ProtocolPlayer(int playerID, Color color, String name, PlayerState status, int victoryPoints,
			ProtocolResource resources, ProtocolDevCard developmentCards, boolean largestArmy, boolean longestRoad ) {
		this.playerID = playerID;
		this.color = color;
		this.name = name;
		this.status = status;
		this.victoryPoints = victoryPoints;
		this.resources = resources;
		this.developmentCards = developmentCards;
		this.largestArmy = largestArmy;
		this.longestRoad = longestRoad;
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

	public ProtocolDevCard getDevelopmentCards() {
		return developmentCards;
	}
	
	public boolean getLargestArmy(){
		return largestArmy;
	}
	
	public boolean getLongestRoad(){
		return longestRoad;
	}
}
