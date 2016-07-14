package network.client.view.tradeview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Trade {
	private SimpleStringProperty tradeString;
	private SimpleStringProperty status ;
	private SimpleIntegerProperty tradeID;
	private SimpleIntegerProperty partnerID;
	
	public Trade(SimpleStringProperty tradeString, SimpleStringProperty status, SimpleIntegerProperty tradeID,
			SimpleIntegerProperty partnerID) {
		super();
		this.tradeString = tradeString;
		this.status = status;
		this.tradeID = tradeID;
		this.partnerID = partnerID;
	}
	public SimpleStringProperty getTradeString() {
		return tradeString;
	}
	public void setTradeString(SimpleStringProperty tradeString) {
		this.tradeString = tradeString;
	}
	public SimpleStringProperty getStatus() {
		return status;
	}
	public void setStatus(SimpleStringProperty status) {
		this.status = status;
	}
	public SimpleIntegerProperty getTradeID() {
		return tradeID;
	}
	public void setTradeID(SimpleIntegerProperty tradeID) {
		this.tradeID = tradeID;
	}
	public SimpleIntegerProperty getPartnerID() {
		return partnerID;
	}
	public void setPartnerID(SimpleIntegerProperty partnerID) {
		this.partnerID = partnerID;
	}
	
	
	
}
