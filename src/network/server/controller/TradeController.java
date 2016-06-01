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
	private PlayerModel[] playerModels;
	private GameLogic gameLogic;
	private int currentOffer_Player;
	private int[] currentOffer_Get;
	private int[] currentOffer_Give;

	public TradeController(Board board, ServerNetworkController nc, GameLogic logic) {
		this.board = board;
		this.networkController = nc;
		this.playerModels = board.getPlayerModels();
		this.gameLogic = logic;
	}

	public void offerToAllPlayers(int offeringPlayerId, int[] resourcesToGet, int[] resourcesToGive) {
		if (playerModels[offeringPlayerId].getPlayerState() != PlayerState.TRADING) {
			// networkController.sendErrorMessageToPlayer(offeringPlayerId,"Offering
			// not allowed");
		} else {
			if (gameLogic.checkPlayerResources(offeringPlayerId, resourcesToGive)) {
				currentOffer_Player = offeringPlayerId;
				currentOffer_Get = resourcesToGet;
				currentOffer_Give = resourcesToGive;
				// networkController.sendTradeOffer(...);
			} else {
				// networkController.sendErrorMessageToPlayer(offeringPlayerId,"not
				// enough Resources");
			}

		}
	}

	public void offerToPlayingPlayer(int offeringPlayerId, int receivingPlayerId, int[] resourcesToGet,
			int[] resourcesToGive) {
		if (playerModels[receivingPlayerId].getPlayerState() != PlayerState.TRADING) {
			// networkController.sendErrorMessageToPlayer(offeringPlayerId,"Offering
			// not allowed");
		} else {

		}
	}

}
