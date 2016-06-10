package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Karte (Board)</b>
 * <p>
 * Contains information about board: fields-, buildings-, harbours-list and
 * robber's location.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolBoard {

	@SerializedName("Felder")
	private ProtocolField[] fields;

	@SerializedName("Gebäude")
	private ProtocolBuilding[] buildings;

	@SerializedName("Häfen")
	private ProtocolHarbour[] harbours;

	@SerializedName("Räuber")
	private String robber_location;

	public ProtocolBoard(ProtocolField[] fields, ProtocolBuilding[] buildings, ProtocolHarbour[] harbours,
			String robber_location) {
		this.fields = fields;
		this.buildings = buildings;
		this.harbours = harbours;
		this.robber_location = robber_location;
	}

	public ProtocolField getProtocolField(int i) {
		return fields[i] != null ? fields[i] : null;
	}

	public ProtocolBuilding getProtocolBuilding(int i) {
		return buildings[i] != null ? buildings[i] : null;
	}

	public ProtocolHarbour getHarbours(int i) {
		return harbours[i] != null ? harbours[i] : null;
	}

	public String getRobber_location() {
		return robber_location;
	}

	public int getAmountFields() {
		return fields.length;
	}

	public int getAmountBuildings() {
		return buildings.length;
	}

	public int getAmountHarbours() {
		return harbours.length;
	}

}
