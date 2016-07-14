package network.client.view.tradeview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Trade {
	private SimpleStringProperty tradeString;
	private SimpleStringProperty status;
	private SimpleIntegerProperty tradeID;
	private SimpleIntegerProperty partnerID;

	public Trade(String tradeString, int tradeID,
			int partnerID) {
		super();
		this.tradeString = new SimpleStringProperty(tradeString);
		this.status = new SimpleStringProperty("");
		this.tradeID = new SimpleIntegerProperty(tradeID);
		this.partnerID = new SimpleIntegerProperty(partnerID);
	}

	public SimpleStringProperty tradeStringProperty() {
		return tradeString;
	}

	public String getTradeString() {
		return tradeString.get();
	}

	public void setTradeString(String tradeString) {
		this.tradeString.set(tradeString);
	}

	public SimpleStringProperty statusProperty() {
		return status;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public SimpleIntegerProperty tradeIDProperty() {
		return tradeID;
	}

	public int getTradeID() {
		return tradeID.get();
	}

	public void setTradeID(int tradeID) {
		this.tradeID.set(tradeID);
	}

	public SimpleIntegerProperty partnerIDProperty() {
		return partnerID;
	}

	public int getPartnerID() {
		return partnerID.get();
	}

	public void setPartnerID(int partnerID) {
		this.partnerID.set(partnerID);
	}

}
