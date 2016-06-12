package network.server.controller;

import java.util.ArrayList;

public class TradeController {

	private ServerController serverController;
	private int tradeCounter = 0;
	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	public TradeController(ServerController serverController) {
		this.serverController = serverController;
	}

	public void clientOffersTrade(int modelID, int[] supply, int[] demand) {
		TradeOffer offer = new TradeOffer(modelID,tradeCounter,supply,demand);
		tradeOffers.add(offer);
		serverController.sendClientOffer(modelID, tradeCounter, supply, demand);
		tradeCounter++;		
	}

	public void acceptTrade(int modelID, int tradingID) {
		for (int i = 0;i <tradeOffers.size();i++){
			if (tradeOffers.get(i).getTradingID() == tradingID){
				tradeOffers.get(tradingID).acceptingPlayers.add(tradingID);
		        serverController.tradeAccepted(modelID, tradingID);
			}
		}		
	}

	public void fulfillTrade(int modelID, int tradingID, int partnerModelID) {
		for (int i = 0;i <tradeOffers.size();i++){
			TradeOffer tOf = tradeOffers.get(i);
			if (tOf.getTradingID() == tradingID){
				boolean notFound = true;
				for (int j = 0;j < tOf.acceptingPlayers.size();j++){
					if (tOf.acceptingPlayers.get(j) == partnerModelID){
						notFound = false;
					}
				}
				if (notFound){
					serverController.serverResponse(modelID, "This player hasn't accepted your supply");
				} else {
					//TODO: Rohstoffverteilung programmieren
					tradeOffers.remove(i);
					serverController.tradeFulfilled(modelID, partnerModelID);
				}
				break;
			}
		}	
		// TODO Auto-generated method stub
		
	}

	public void cancelTrade(Integer integer, int tradingID) {
		// TODO Auto-generated method stub
		
	}

}
