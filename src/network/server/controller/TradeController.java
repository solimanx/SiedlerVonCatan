package network.server.controller;

import java.util.ArrayList;

import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
public class TradeController {

	private ServerController serverController;
	private int tradeCounter = 0;
	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	/**
	 * Instantiates a new trade controller.
	 *
	 * @param serverController
	 *            the server controller
	 * @param amountPlayers
	 *            the amount players
	 */
	public TradeController(ServerController serverController, int amountPlayers) {
		this.serverController = serverController;
	}

	/**
	 * Client offers trade.
	 *
	 * @param modelID
	 *            the model ID
	 * @param supply
	 *            the supply
	 * @param demand
	 *            the demand
	 */
	public void clientOffersTrade(int modelID, int[] supply, int[] demand) {
		if (checkValidTradeRequest(modelID, supply, demand)) {
			TradeOffer offer = new TradeOffer(modelID, tradeCounter, supply, demand);
			tradeOffers.add(offer);
			serverController.sendClientOffer(modelID, tradeCounter, supply, demand);
			tradeCounter++;
		} else {
			serverController.serverResponse(modelID, "Unzulässiges Handelsangebot");
		}
	}

	/**
	 * Checks whether the trade is valid.
	 *
	 * @param modelID the model ID
	 * @param supply the supply
	 * @param demand the demand
	 * @return true, if successful
	 */
	private boolean checkValidTradeRequest(int modelID, int[] supply, int[] demand) {
		if (serverController.gameLogic.getBoard().getPlayer(modelID).getPlayerState() != PlayerState.TRADING_OR_BUILDING){
			return false;
		}
		int sum = 0;
		int[] resources = serverController.gameLogic.getBoard().getPlayer(modelID).getResources();
		for (int i = 0; i < supply.length; i++) {
			// negative supply or demand
			if (supply[i] < 0 || demand[i] < 0) {
				return false;
			}
			// insufficient resources
			else if (supply[i] > resources[i]) {
				return false;
			}
			// trading same resource
			else if (supply[i] > 0 && demand[i] > 0) {
				return false;
			} else {
				sum += supply[i];
			}
		}
		// 0 supply resources
		if (sum == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Accept trade.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 * @param accept
	 *            the accept
	 */
	public void acceptTrade(int modelID, int tradingID, boolean accept) {
		if (checkValidTradeAccept(modelID, tradingID, accept)) {
			for (int i = 0; i < tradeOffers.size(); i++) {
				if (tradeOffers.get(i).getTradingID() == tradingID) {
					if (accept) {
						tradeOffers.get(i).acceptingPlayers.add(modelID);
					} else {
						tradeOffers.get(i).decliningPlayers.add(modelID);
					}

					serverController.tradeAccepted(modelID, tradingID, accept);
				}
			}
		} else {
			serverController.serverResponse(modelID, "Unzulässige Handelsannahme");
		}
	}
	public boolean checkValidSeaTrade(int modelID, int[] offer, int[] demand){
		return false; //TODO
	}

	/**
	 * Checks whether the trade can be accepted.
	 *
	 * @param modelID the model ID
	 * @param tradingID the trading ID
	 * @param accept the accept
	 * @return true, if successful
	 */
	private boolean checkValidTradeAccept(int modelID, int tradingID, boolean accept) {
		int[] tradeResource = null;
		// same id check
		for (int i = 0; i < tradeOffers.size(); i++) {
			if (tradeOffers.get(i).getTradingID() == tradingID && tradeOffers.get(i).getOwnerID() == modelID) {
				return false;
			} else if (tradeOffers.get(i).getTradingID() == tradingID) {
				tradeResource = tradeOffers.get(i).getDemand();
			}
		}

		// afford
		int[] resources = serverController.gameLogic.getBoard().getPlayer(modelID).getResources();

		if (accept == true) {
			for (int i = 0; i < resources.length; i++) {
				if (tradeResource[i] > resources[i]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Fulfill trade.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 * @param partnerModelID
	 *            the partner model ID
	 */
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
					if (serverController.gameLogic.checkPlayerResources(modelID, offer)) {
						if (serverController.gameLogic.checkPlayerResources(partnerModelID, demand)) {

							serverController.subFromPlayersResources(modelID, offer);
							serverController.subFromPlayersResources(partnerModelID, demand);

							serverController.addToPlayersResource(modelID, demand);
							serverController.addToPlayersResource(partnerModelID, offer);

							serverController.costsToAll(modelID, offer, true);
							serverController.costsToAll(partnerModelID, demand, true);

							serverController.obtainToAll(modelID, demand, true);
							serverController.obtainToAll(partnerModelID, offer, true);

							tradeOffers.remove(i);

							serverController.tradeFulfilled(modelID, partnerModelID);
						} else {
							serverController.error(partnerModelID, "You haven't got enough resources for the trade");
							serverController.error(modelID, "Your partner hasn't got enough resources for this trade");
						}
					} else {
						serverController.error(partnerModelID,
								"Your partner hasn't got enough resources for this trade");
						serverController.error(modelID, "You haven't got enough resources for the trade");
					}
				}
				break;
			}
		}

	}

	/**
	 * Cancel trade.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 */
	public void cancelTrade(int modelID, int tradingID) {
		TradeOffer currOf;
		for (int i = 0; i < tradeOffers.size(); i++) {
			currOf = tradeOffers.get(i);
			if (currOf.getTradingID() == tradingID) {
				if (currOf.getOwnerID() == modelID) {
					tradeOffers.remove(i);
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

	/**
	 * Request sea trade.
	 *
	 * @param modelID
	 *            the model ID
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 */
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
		if (offerResType != null && demandAmount == 1) { // currently only
															// single trade
															// allowed
			switch (offerAmount) {
			case 2:
				boolean contains = false;
				for (int i = 0; i < harbours.size(); i++) {
					if (harbours.get(i).name().equals(offerResType.name())) {
						contains = true;
					}
				}
				if (contains) {
					if (serverController.resourceStackDecrease(demandResType)) {
						serverController.addToPlayersResource(modelID, demandResType, 1);
						serverController.obtainToAll(modelID, demand, true);
						serverController.subFromPlayersResources(modelID, offerResType, 2);
						serverController.costsToAll(modelID, offer, true);
						serverController.resourceStackIncrease(offer);
					} else {
						serverController.getServerOutputHandler().serverConfirm("resource stack empty",
								serverController.modelPlayerIdMap.get(modelID));
					}
				} else {
					serverController.getServerOutputHandler().serverConfirm("You don't have a 2:1 harbour of this resource",
							serverController.modelPlayerIdMap.get(modelID));
				}
				break;
			case 3:
				if (harbours.contains(HarbourStatus.THREE_TO_ONE)) {
					if (serverController.resourceStackDecrease(demandResType)) {
						serverController.addToPlayersResource(modelID, demandResType, 1);
						serverController.obtainToAll(modelID, demand, true);
						serverController.subFromPlayersResources(modelID, offerResType, 3);
						serverController.costsToAll(modelID, offer, true);
						serverController.resourceStackIncrease(offer);
					} else {
						serverController.getServerOutputHandler().serverConfirm("resource stack empty",
								serverController.modelPlayerIdMap.get(modelID));
					}
				} else {
					serverController.getServerOutputHandler().serverConfirm("You don't have a 3:1 harbour",
							serverController.modelPlayerIdMap.get(modelID));
				}
				break;
			case 4:
				if (serverController.resourceStackDecrease(demandResType)) {
					serverController.addToPlayersResource(modelID, demandResType, 1);
					serverController.obtainToAll(modelID, demand, true);
					serverController.subFromPlayersResources(modelID, offerResType, 4);
					serverController.costsToAll(modelID, offer, true);
					serverController.resourceStackIncrease(offer);
				} else {
					serverController.getServerOutputHandler().serverConfirm("resource stack empty",
							serverController.modelPlayerIdMap.get(modelID));
				}
				break;

			default:
				serverController.getServerOutputHandler().serverConfirm("invalid resource argument",
						serverController.modelPlayerIdMap.get(modelID));
				break;
			}
		}
	}

	/**
	 * Gets the player harbours.
	 *
	 * @param modelID
	 *            the model ID
	 * @return the player harbours
	 */
	private ArrayList<HarbourStatus> getPlayerHarbours(int modelID) {
		return serverController.gameLogic.getBoard().getPlayer(modelID).getPlayerHarbours();
	}

}
