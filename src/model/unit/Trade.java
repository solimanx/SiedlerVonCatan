package model.unit;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Trade {
	private SimpleStringProperty tradeString;
	private SimpleStringProperty status;
	private SimpleIntegerProperty tradeID;
	private SimpleIntegerProperty partnerID;

	public Trade(String tradeString, Integer tradeID,
			Integer partnerID) {
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

	public Integer getTradeID() {
		return tradeID.getValue();
	}

	public void setTradeID(Integer tradeID) {
		this.tradeID.set(tradeID);
	}

	public SimpleIntegerProperty partnerIDProperty() {
		return partnerID;
	}

	public Integer getPartnerID() {
		return partnerID.get();
	}

	public void setPartnerID(Integer partnerID) {
		this.partnerID.set(partnerID);
	}

}
