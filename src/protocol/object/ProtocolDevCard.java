package protocol.object;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;

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

	@SerializedName("Unbekannt")
	private Integer unknown;

	public ProtocolDevCard(Integer knight, Integer roadbuild, Integer monopoly, Integer invention, Integer unknown) {
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
