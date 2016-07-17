package network.server.controller;

import java.util.ArrayList;

import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import model.unit.TradeOffer;
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
	 * @param modelID
	 *            the model ID
	 * @param supply
	 *            the supply
	 * @param demand
	 *            the demand
	 * @return true, if successful
	 */
	private boolean checkValidTradeRequest(int modelID, int[] supply, int[] demand) {
		if (serverController.gameLogic.getBoard().getPlayer(modelID)
				.getPlayerState() != PlayerState.TRADING_OR_BUILDING) {
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
			TradeOffer tOf = getTradeByID(tradingID);
			// kann nicht null sein wegen check
			if (accept) {
				tOf.getAcceptingPlayers().add(modelID);
			} else {
				tOf.getDecliningPlayers().add(modelID);
			}

			serverController.tradeAccepted(modelID, tradingID, accept);
		} else {
			serverController.serverResponse(modelID, "Unzulässige Handelsannahme");
		}
	}

	public boolean checkValidSeaTrade(int modelID, int offResource, int demResource, int offAmount) {
		if (serverController.gameLogic.getBoard().getPlayer(modelID).getPlayerState() != PlayerState.TRADING_OR_BUILDING){
			return false;
		}
		if (serverController.getPlayerResources(modelID)[offResource] < offAmount) {
			return false;
		}
		if (offResource == demResource) {
			return false;
		}
		ArrayList<HarbourStatus> harbours = getPlayerHarbours(modelID);
		switch (offAmount) {
		case 2:
			for (int i = 0; i < harbours.size(); i++) {
				if (harbours.get(i).name().equals(DefaultSettings.RESOURCE_ORDER[offResource].name())) {
					return true;
				}
			}
			break;
		case 3:
			for (int i = 0; i < harbours.size(); i++) {
				if (harbours.get(i).name().equals("THREE_TO_ONE")) {
					return true;
				}
			}
			break;
		case 4:
			return true;
		default:
			return false;

		}
		return false;
	}

	/**
	 * Checks whether the trade can be accepted.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 * @param accept
	 *            the accept
	 * @return true, if successful
	 */
	private boolean checkValidTradeAccept(int modelID, int tradingID, boolean accept) {
		int[] tradeResource;
		TradeOffer tOf = getTradeByID(tradingID);
		if (tOf != null) {
			if (tOf.getOwnerID() == modelID) {
				return false;
			} else {
				tradeResource = tOf.getDemand();
			}
		} else {
			return false;
		}

		if (accept == true) {
			if (serverController.gameLogic.checkPlayerResources(modelID, tradeResource)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
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
		if (serverController.gameLogic.getBoard().getPlayer(modelID)
				.getPlayerState() != PlayerState.TRADING_OR_BUILDING) {
			serverController.serverResponse(modelID, "Du bist nicht am Zug!");
		} else {
			TradeOffer tOf = getTradeByID(tradingID);
			if (tOf != null) {
				boolean notFound = true;
				for (int j = 0; j < tOf.getAcceptingPlayers().size(); j++) {
					if (tOf.getAcceptingPlayers().get(j) == partnerModelID) {
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

							tradeOffers.remove(tOf);

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
			} else {
				serverController.serverResponse(modelID, "Ungültige Handels ID");

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
		TradeOffer offer = getTradeByID(tradingID);
		if (offer != null) {
			if (offer.getOwnerID() == modelID) {
				tradeOffers.remove(offer);
				serverController.tradeCancelled(modelID, tradingID);
			} else {
				for (int j = 0; j < offer.getAcceptingPlayers().size(); j++) {
					if (offer.getAcceptingPlayers().get(j) == modelID) {
						offer.getAcceptingPlayers().remove(j);
						serverController.tradeCancelled(modelID, tradingID);
						break;
					}
				}
			}
		} else {
			serverController.serverResponse(modelID, "Ungültige Handels ID");
		}
	}

	private TradeOffer getTradeByID(int tradingID) {
		for (int i = 0; i < tradeOffers.size(); i++) {
			if (tradeOffers.get(i).getTradingID() == tradingID) {
				return tradeOffers.get(i);
			}
		}
		return null;
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
		int offResource = 0;
		int demResource = 0;
		for (int i = 0; i < offer.length; i++) {
			if (offer[i] > 0) {
				offResource = i;
			}
			if (demand[i] > 0) {
				demResource = i;
			}
		}
		if (demand[demResource] != 1) {
			serverController.serverResponse(modelID, "Unzulässiger Seehandel");
		} else {
			int offAmount = offer[offResource];
			ResourceType offerResType = DefaultSettings.RESOURCE_ORDER[offResource];
			ResourceType demandResType = DefaultSettings.RESOURCE_ORDER[demResource];
			if (checkValidSeaTrade(modelID, offResource, demResource, offAmount)) {
				if (serverController.resourceStackDecrease(demandResType)) {
					serverController.addToPlayersResource(modelID, demandResType, 1);
					serverController.obtainToAll(modelID, demand, true);
					serverController.subFromPlayersResources(modelID, offerResType, offAmount);
					serverController.costsToAll(modelID, offer, true);
					serverController.resourceStackIncrease(offer);
				} else {
					serverController.serverResponse(modelID, "Resourcenstapel leer");
				}
			} else {
				serverController.serverResponse(modelID, "Unzulässiges Handelsangebot");
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

	public void clearTrades() {
		tradeOffers.clear();
	}

}
