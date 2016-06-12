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
					serverController.subFromPlayersResources(modelID,tOf.getSupply());
					serverController.subFromPlayersResources(partnerModelID, tOf.getDemand());
					
					serverController.addToPlayersResource(modelID,tOf.getDemand());
					serverController.subFromPlayersResources(partnerModelID, tOf.getSupply());
					
					serverController.statusUpdate(modelID);
					serverController.statusUpdate(partnerModelID);
					
					tradeOffers.remove(i);
					
					serverController.tradeFulfilled(modelID, partnerModelID);
				}
				break;
			}
		}	
		
	}

	public void cancelTrade(int modelID, int tradingID) {
		TradeOffer currOf;
		for (int i = 0;i<tradeOffers.size();i++){
			currOf = tradeOffers.get(i);
			if (currOf.getTradingID() == tradingID && currOf.getOwnerID() == modelID){
				tradeOffers.remove(i);
				serverController.tradeCancelled(modelID, tradingID);
				break;
			}
		}
		
	}

}
