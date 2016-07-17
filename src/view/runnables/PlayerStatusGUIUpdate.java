package view.runnables;

import enums.PlayerState;
import view.GameViewController;

// TODO: Auto-generated Javadoc
public class PlayerStatusGUIUpdate implements Runnable {

	final int modelID;
	final GameViewController gameViewController;
	final int victoryPoints;
	final PlayerState status;
	final int[] resources;
	final Integer devCards;

	/**
	 * Instantiates a new player status GUI update.
	 *
	 * @param modelID
	 *            the model ID
	 * @param gameViewController
	 *            the game view controller
	 * @param victoryPoints
	 *            the victory points
	 * @param status
	 *            the status
	 * @param resources
	 *            the resources
	 */
	public PlayerStatusGUIUpdate(int modelID, GameViewController gameViewController, int victoryPoints,
			PlayerState status, int[] resources, Integer devCards) {
		super();
		this.modelID = modelID;
		this.gameViewController = gameViewController;
		this.victoryPoints = victoryPoints;
		this.status = status;
		this.resources = resources;
		this.devCards = devCards;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		gameViewController.setVictoryPoints(modelID, victoryPoints);
		//gameViewController.setResourceCards(modelID, resources);
		gameViewController.setPlayerStatus(modelID, status);
		if (modelID != 0){
			gameViewController.setDevCards(modelID);
		}
	}

}
