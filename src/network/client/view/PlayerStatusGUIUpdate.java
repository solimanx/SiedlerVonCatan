package network.client.view;

import enums.PlayerState;

// TODO: Auto-generated Javadoc
public class PlayerStatusGUIUpdate implements Runnable {

	final int modelID;
	final GameViewController gameViewController;
	final int victoryPoints;
	final PlayerState status;
	final int[] resources;

	/**
	 * Instantiates a new player status GUI update.
	 *
	 * @param modelID the model ID
	 * @param gameViewController the game view controller
	 * @param victoryPoints the victory points
	 * @param status the status
	 * @param resources the resources
	 */
	public PlayerStatusGUIUpdate(int modelID, GameViewController gameViewController, int victoryPoints,
			PlayerState status, int[] resources) {
		super();
		this.modelID = modelID;
		this.gameViewController = gameViewController;
		this.victoryPoints = victoryPoints;
		this.status = status;
		this.resources = resources;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		gameViewController.setVictoryPoints(modelID, victoryPoints);
		//gameViewController.setResourceCards(modelID, resources);
		gameViewController.setPlayerStatus(modelID, status);
	}

}
