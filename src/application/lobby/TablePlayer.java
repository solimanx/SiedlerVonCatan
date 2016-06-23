package application.lobby;

import enums.Color;
import enums.PlayerState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import protocol.object.ProtocolPlayer;

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

	public TablePlayer(int threadID, enums.Color color, String name, enums.PlayerState status) {
		this.playerId = new SimpleIntegerProperty(threadID);
		this.color = color != null ? new SimpleStringProperty(color.toString()) : new SimpleStringProperty("");
		this.name = name != null ? new SimpleStringProperty(name) : new SimpleStringProperty("no name");
		this.status = new SimpleStringProperty(status.toString());

	}

	public int getPlayerId() {
		return playerId.get();
	}

	public void setPlayerId(SimpleIntegerProperty playerID) {
		this.playerId = playerID;
	}

	public SimpleIntegerProperty playerIdProperty(){
		return playerId;
	}

	public String getColor(){
		return color.get();
	}

	public SimpleStringProperty colorProperty() {
		return color;
	}

	public void setColor(SimpleStringProperty color) {
		this.color = color;
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	public String getName(){
		return name.get();
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public SimpleStringProperty statusProperty() {
		return status;
	}

	public void setStatus(SimpleStringProperty status) {
		this.status = status;
	}

	public String getStatus(){
		return status.get();
	}

}