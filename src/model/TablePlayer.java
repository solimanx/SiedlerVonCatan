package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// TODO: Auto-generated Javadoc
/**
 * <b>Spieler (Player)</b>
 * <p>
 * Contains information about player: player ID, color, name , status, score,
 * and resources.
 * </p>
 *
 */

public class TablePlayer {

	private SimpleIntegerProperty playerId;

	private SimpleStringProperty color;

	private SimpleStringProperty name;

	private SimpleStringProperty status;

	/**
	 * Instantiates a new table player.
	 *
	 * @param threadID the thread ID
	 * @param color the color
	 * @param name the name
	 * @param status the status
	 */
	public TablePlayer(int threadID, enums.Color color, String name, enums.PlayerState status) {
		this.playerId = new SimpleIntegerProperty(threadID);
		this.color = color != null ? new SimpleStringProperty(color.toString()) : new SimpleStringProperty("");
		this.name = name != null ? new SimpleStringProperty(name) : new SimpleStringProperty("no name");
		this.status = new SimpleStringProperty(status.toString());

	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerId() {
		return playerId.get();
	}

	/**
	 * Sets the player id.
	 *
	 * @param playerID the new player id
	 */
	public void setPlayerId(SimpleIntegerProperty playerID) {
		this.playerId = playerID;
	}

	/**
	 * Player id property.
	 *
	 * @return the simple integer property
	 */
	public SimpleIntegerProperty playerIdProperty(){
		return playerId;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public String getColor(){
		return color.get();
	}

	/**
	 * Color property.
	 *
	 * @return the simple string property
	 */
	public SimpleStringProperty colorProperty() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(SimpleStringProperty color) {
		this.color = color;
	}

	/**
	 * Name property.
	 *
	 * @return the simple string property
	 */
	public SimpleStringProperty nameProperty() {
		return name;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return name.get();
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	/**
	 * Status property.
	 *
	 * @return the simple string property
	 */
	public SimpleStringProperty statusProperty() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(SimpleStringProperty status) {
		this.status = status;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus(){
		return status.get();
	}

}