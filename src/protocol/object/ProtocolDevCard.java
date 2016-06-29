package protocol.object;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b> 6.7 Entwicklungskarte Objekte( development cards)</b>
 * <p>
 * </p>
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolDevCard {

	@SerializedName("Ritter")
	private Integer knight;

	@SerializedName("Straßenbau")
	private Integer roadbuild;

	@SerializedName("Monopol")
	private Integer monopoly;

	@SerializedName("Erfindung")
	private Integer invention;

	@SerializedName("Siegpunkt")
	private Integer victoryPoint;

	@SerializedName("Unbekannt")
	private Integer unknown;

	/**
	 * Instantiates a new protocol dev card.
	 *
	 * @param knight the knight
	 * @param roadbuild the roadbuild
	 * @param monopoly the monopoly
	 * @param invention the invention
	 * @param victoryPoint the victory point
	 * @param unknown the unknown
	 */
	public ProtocolDevCard(Integer knight, Integer roadbuild, Integer monopoly, Integer invention, Integer victoryPoint,
			Integer unknown) {
		this.knight = knight;
		this.roadbuild = roadbuild;
		this.monopoly = monopoly;
		this.invention = invention;
		this.victoryPoint = victoryPoint;
		this.unknown = unknown;

	}

	/**
	 * Gets the knight.
	 *
	 * @return the knight
	 */
	public Integer getKnight() {
		return knight;
	}

	/**
	 * Gets the roadbuild.
	 *
	 * @return the roadbuild
	 */
	public Integer getRoadbuild() {
		return roadbuild;
	}

	/**
	 * Gets the monopoly.
	 *
	 * @return the monopoly
	 */
	public Integer getMonopoly() {
		return monopoly;
	}

	/**
	 * Gets the invention.
	 *
	 * @return the invention
	 */
	public Integer getInvention() {
		return invention;
	}

	/**
	 * Gets the victory point.
	 *
	 * @return the victory point
	 */
	public Integer getVictoryPoint() {
		return victoryPoint;
	}

	/**
	 * Gets the unknown.
	 *
	 * @return the unknown
	 */
	public Integer getUnknown() {
		return unknown;
	}

}
