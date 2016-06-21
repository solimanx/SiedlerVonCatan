package network.client.view;

import enums.PlayerState;

public class PlayerStatusGUIUpdate implements Runnable {

	final int modelID;
	final GameViewController gameViewController;
	final int victoryPoints;
	final PlayerState status;
	final int[] resources;

	public PlayerStatusGUIUpdate(int modelID, GameViewController gameViewController, int victoryPoints,
			PlayerState status, int[] resources) {
		super();
		this.modelID = modelID;
		this.gameViewController = gameViewController;
		this.victoryPoints = victoryPoints;
		this.status = status;
		this.resources = resources;
	}

	@Override
	public void run() {
		gameViewController.setVictoryPoints(modelID, victoryPoints);
		//gameViewController.setResourceCards(modelID, resources);
		gameViewController.setPlayerStatus(modelID, status);
	}

}
