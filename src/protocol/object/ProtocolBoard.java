package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.unit.Index;

// TODO: Auto-generated Javadoc
/**
 * <b>Karte (Board)</b>
 * <p>
 * Contains information about board: fields-, buildings-, harbours-list and
 * robber's location.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolBoard {

	@SerializedName("Felder")
	private ProtocolField[] fields;

	@SerializedName("Gebäude")
	private ProtocolBuilding[] buildings;

	@SerializedName("Häfen")
	private ProtocolHarbour[] harbours;

	@SerializedName("Räuber")
	private Index robber_location;

	/**
	 * Instantiates a new protocol board.
	 *
	 * @param fields the fields
	 * @param buildings the buildings
	 * @param harbours the harbours
	 * @param robber_location the robber location
	 */
	public ProtocolBoard(ProtocolField[] fields, ProtocolBuilding[] buildings, ProtocolHarbour[] harbours,
			Index robber_location) {
		this.fields = fields;
		this.buildings = buildings;
		this.harbours = harbours;
		this.robber_location = robber_location;
	}

	/**
	 * Gets the protocol field.
	 *
	 * @param i the i
	 * @return the protocol field
	 */
	public ProtocolField getProtocolField(int i) {
		return fields[i] != null ? fields[i] : null;
	}

	/**
	 * Gets the protocol building.
	 *
	 * @param i the i
	 * @return the protocol building
	 */
	public ProtocolBuilding getProtocolBuilding(int i) {
		return buildings[i] != null ? buildings[i] : null;
	}

	/**
	 * Gets the harbours.
	 *
	 * @param i the i
	 * @return the harbours
	 */
	public ProtocolHarbour getHarbours(int i) {
		return harbours[i] != null ? harbours[i] : null;
	}

	/**
	 * Gets the robber location.
	 *
	 * @return the robber location
	 */
	public Index getRobber_location() {
		return robber_location;
	}

	/**
	 * Gets the amount fields.
	 *
	 * @return the amount fields
	 */
	public int getAmountFields() {
		return fields.length;
	}

	/**
	 * Gets the amount buildings.
	 *
	 * @return the amount buildings
	 */
	public int getAmountBuildings() {
		return buildings.length;
	}

	/**
	 * Gets the amount harbours.
	 *
	 * @return the amount harbours
	 */
	public int getAmountHarbours() {
		return harbours.length;
	}

}
