package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.Color;
import enums.PlayerState;
import protocol.dualinstructions.ProtocolPlayKnightCard;

// TODO: Auto-generated Javadoc
/**
 * <b>Spieler (Player)</b>
 * <p>
 * Contains information about player: player ID, color, name , status, score,
 * and resources.
 * </p>
 *
 */

@Since(1.0)
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

	@SerializedName("Rittermacht")
	private Integer knightCount;

	@SerializedName("Entwicklungskarten")
	private ProtocolDevCard developmentCards;

	@SerializedName("Größte Rittermacht")
	private boolean largestArmy;

	@SerializedName("Größte Handelsstraße")
	private boolean longestRoad;


	/**
	 * Instantiates a new protocol player.
	 *
	 * @param playerID the player ID
	 * @param color the color
	 * @param name the name
	 * @param status the status
	 * @param victoryPoints the victory points
	 * @param resources the resources
	 * @param knightCount the knight count
	 * @param developmentCards the development cards
	 * @param largestArmy the largest army
	 * @param longestRoad the longest road
	 */
	public ProtocolPlayer(int playerID, Color color, String name, PlayerState status, int victoryPoints,
			ProtocolResource resources, Integer knightCount, ProtocolDevCard developmentCards, boolean largestArmy, boolean longestRoad ) {
		this.playerID = playerID;
		this.color = color;
		this.name = name;
		this.status = status;
		this.victoryPoints = victoryPoints;
		this.resources = resources;
		this.knightCount = knightCount;
		this.developmentCards = developmentCards;
		this.largestArmy = largestArmy;
		this.longestRoad = longestRoad;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public PlayerState getStatus() {
		return status;
	}

	/**
	 * Gets the victory points.
	 *
	 * @return the victory points
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 */
	public ProtocolResource getResources() {
		return resources;
	}

	/**
	 * Gets the knight count.
	 *
	 * @return the knight count
	 */
	public Integer getKnightCount(){
		return knightCount;
	}

	/**
	 * Gets the development cards.
	 *
	 * @return the development cards
	 */
	public ProtocolDevCard getDevelopmentCards() {
		return developmentCards;
	}

	/**
	 * Gets the largest army.
	 *
	 * @return the largest army
	 */
	public boolean getLargestArmy(){
		return largestArmy;
	}

	/**
	 * Gets the longest road.
	 *
	 * @return the longest road
	 */
	public boolean getLongestRoad(){
		return longestRoad;
	}
}
