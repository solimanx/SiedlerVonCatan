package network.server.controller;

import enums.PlayerState;
import model.Board;
import model.GameLogic;
import model.PlayerModel;

/**
 * @author Controls trading between players
 */
public class TradeController {

	private Board board;
	private ServerNetworkController networkController;
	private GameLogic gameLogic;
	private int currentOffer_Player;
	private int[] currentOffer_Get;
	private int[] currentOffer_Give;

	public TradeController(Board board, ServerNetworkController nc, GameLogic logic) {
		this.board = board;
		this.networkController = nc;
		this.gameLogic = logic;
	}

	public void offerToAllPlayers(int offeringPlayerID, int[] resourcesToGet, int[] resourcesToGive) {
		if (board.getPlayerModels()[offeringPlayerID].getPlayerState() != PlayerState.TRADING) {
			// networkController.sendErrorMessageToPlayer(offeringPlayerID,"Offering
			// not allowed");
		} else {
			if (gameLogic.checkPlayerResources(offeringPlayerID, resourcesToGive)) {
				currentOffer_Player = offeringPlayerID;
				currentOffer_Get = resourcesToGet;
				currentOffer_Give = resourcesToGive;
				// networkController.sendTradeOffer(...);
			} else {
				// networkController.sendErrorMessageToPlayer(offeringPlayerID,"not
				// enough Resources");
			}

		}
	}

	public void offerToPlayingPlayer(int offeringPlayerID, int receivingPlayerID, int[] resourcesToGet,
			int[] resourcesToGive) {
		if (board.getPlayerModels()[receivingPlayerID].getPlayerState() != PlayerState.TRADING) {
			// networkController.sendErrorMessageToPlayer(offeringPlayerID,"Offering
			// not allowed");
		} else {

		}
	}

}
