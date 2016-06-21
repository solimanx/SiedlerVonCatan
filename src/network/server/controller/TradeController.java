package network.server.controller;

import java.util.ArrayList;

import enums.HarbourStatus;
import enums.ResourceType;
import settings.DefaultSettings;

public class TradeController {

	private ServerController serverController;
	private int tradeCounter = 0;
	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	public TradeController(ServerController serverController, int amountPlayers) {
		this.serverController = serverController;
	}

	public void clientOffersTrade(int modelID, int[] supply, int[] demand) {
		TradeOffer offer = new TradeOffer(modelID, tradeCounter, supply, demand);
		tradeOffers.add(offer);
		serverController.sendClientOffer(modelID, tradeCounter, supply, demand);
		tradeCounter++;
	}

	public void acceptTrade(int modelID, int tradingID, boolean accept) {
		for (int i = 0; i < tradeOffers.size(); i++) {
			if (tradeOffers.get(i).getTradingID() == tradingID) {
				if (accept){
					tradeOffers.get(i).acceptingPlayers.add(modelID);
				} else {
					tradeOffers.get(i).decliningPlayers.add(modelID);
				}
				
				serverController.tradeAccepted(modelID, tradingID, accept);
			}
		}
	}


	public void fulfillTrade(int modelID, int tradingID, int partnerModelID) {
		for (int i = 0; i < tradeOffers.size(); i++) {
			TradeOffer tOf = tradeOffers.get(i);
			if (tOf.getTradingID() == tradingID) {
				boolean notFound = true;
				for (int j = 0; j < tOf.acceptingPlayers.size(); j++) {
					if (tOf.acceptingPlayers.get(j) == partnerModelID) {
						notFound = false;
					}
				}
				if (notFound) {
					serverController.serverResponse(modelID, "This player hasn't accepted your supply");
				} else {
					int[] offer = tOf.getSupply();
					int[] demand = tOf.getDemand();
					if (serverController.gameLogic.checkPlayerResources(modelID, offer)){
						if (serverController.gameLogic.checkPlayerResources(partnerModelID, demand)){
							
							serverController.subFromPlayersResources(modelID, offer);
							serverController.subFromPlayersResources(partnerModelID, demand);

							serverController.addToPlayersResource(modelID, demand);
							serverController.addToPlayersResource(partnerModelID, offer);
							
							int threadID = serverController.modelPlayerIdMap.get(modelID);
							int partnerThreadID = serverController.modelPlayerIdMap.get(partnerModelID);
							
							serverController.getServerOutputHandler().costs(threadID, offer, threadID);
							serverController.getServerOutputHandler().costs(partnerThreadID, demand, partnerThreadID);
							
							serverController.getServerOutputHandler().resourceObtain(threadID, demand);
							serverController.getServerOutputHandler().resourceObtain(partnerThreadID, offer);

							tradeOffers.remove(i);

							serverController.tradeFulfilled(modelID, partnerModelID);
						} else {
							serverController.error(partnerModelID, "You haven't got enough resources for the trade");
							serverController.error(modelID, "Your partner hasn't got enough resources for this trade");
						}
					} else {
						serverController.error(partnerModelID, "Your partner hasn't got enough resources for this trade");
						serverController.error(modelID, "You haven't got enough resources for the trade");
					}
				}
				break;
			}
		}

	}

	public void cancelTrade(int modelID, int tradingID) {
		TradeOffer currOf;
		for (int i = 0; i < tradeOffers.size(); i++) {
			currOf = tradeOffers.get(i);
			if (currOf.getTradingID() == tradingID) {
				if (currOf.getOwnerID() == modelID) {
					tradeOffers.remove(i);
					serverController.tradeCancelled(modelID, tradingID);
				} else {
					for (int j = 0; j < currOf.acceptingPlayers.size(); j++) {
						if (currOf.acceptingPlayers.get(j) == modelID) {
							currOf.acceptingPlayers.remove(j);
							break;
						}
					}

				}
				serverController.tradeCancelled(modelID, tradingID);
				break;
			}
		}

	}
	

	public void requestSeaTrade(int modelID, int[] offer, int[] demand) {
		ResourceType offerResType = null;
		ResourceType demandResType = null;
		int offerAmount = 0;
		int demandAmount = 0;
		ArrayList<HarbourStatus> harbours = getPlayerHarbours(modelID);
		for (int i = 0; i < offer.length; i++) {
			if (offer[i] != 0) {
				offerResType = DefaultSettings.RESOURCE_ORDER[i];
				offerAmount = offer[i];
			} else if (demand[i] != 0) {
				demandResType = DefaultSettings.RESOURCE_ORDER[i];
				demandAmount = demand[i];
			}

		}
		if (offerResType != null && demandAmount == 1) { //currently only single trade allowed
			switch (offerAmount) {
			case 2:
				boolean contains = false;
				for (int i = 0; i < harbours.size(); i++) {
					if (harbours.get(i).name().equals(offerResType.name())){
						contains = true;
					}
				}
				if (contains) {
					if (serverController.resourceStackDecrease(demandResType)) {
						serverController.addToPlayersResource(modelID, demandResType, 1);
						serverController.getServerOutputHandler().resourceObtain(
								serverController.modelPlayerIdMap.get(modelID),
								demand);
						serverController.subFromPlayersResources(modelID, offerResType, 2);
						serverController.getServerOutputHandler().costs(
								serverController.modelPlayerIdMap.get(modelID),
								offer,serverController.modelPlayerIdMap.get(modelID));
					} else {
						serverController.getServerOutputHandler().error("resource stack empty",
								serverController.modelPlayerIdMap.get(modelID));
					}
				} else {
					serverController.getServerOutputHandler().error("You don't have a 2:1 harbour of this resource",
							serverController.modelPlayerIdMap.get(modelID));
				}
				break;
			case 3:
				if (harbours.contains(HarbourStatus.THREE_TO_ONE)) {
					if (serverController.resourceStackDecrease(demandResType)) {
						serverController.addToPlayersResource(modelID, demandResType, 1);
						serverController.getServerOutputHandler().resourceObtain(
								serverController.modelPlayerIdMap.get(modelID),
								demand);
						serverController.subFromPlayersResources(modelID, offerResType, 3);
						serverController.getServerOutputHandler().costs(
								serverController.modelPlayerIdMap.get(modelID),
								offer,serverController.modelPlayerIdMap.get(modelID));
					} else {
						serverController.getServerOutputHandler().error("resource stack empty",
								serverController.modelPlayerIdMap.get(modelID));
					}
				} else {
					serverController.getServerOutputHandler().error("You don't have a 3:1 harbour",
							serverController.modelPlayerIdMap.get(modelID));
				}
				break;
			case 4:
				if (serverController.resourceStackDecrease(demandResType)) {
					serverController.addToPlayersResource(modelID, demandResType, 1);
					serverController.getServerOutputHandler().resourceObtain(
							serverController.modelPlayerIdMap.get(modelID),
							demand);
					serverController.subFromPlayersResources(modelID, offerResType, 4);
					serverController.getServerOutputHandler().costs(
							serverController.modelPlayerIdMap.get(modelID),
							offer,serverController.modelPlayerIdMap.get(modelID));
				} else {
					serverController.getServerOutputHandler().error("resource stack empty",
							serverController.modelPlayerIdMap.get(modelID));
				}
				break;

			default:
				serverController.getServerOutputHandler().error("invalid resource argument",
						serverController.modelPlayerIdMap.get(modelID));
				break;
			}
		}
	}

	private ArrayList<HarbourStatus> getPlayerHarbours(int modelID) {
		return serverController.gameLogic.getBoard().getPlayer(modelID).getPlayerHarbours();
	}

}
