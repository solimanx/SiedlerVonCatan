package network.client.view;

import enums.PlayerState;

public class PlayerResourceUpdateRunnable implements Runnable {

	final int modelID;
	final GameViewController gameViewController;
	final int[] resources;

	public PlayerResourceUpdateRunnable(int modelID, GameViewController gameViewController, int[] resources) {
		super();
		this.modelID = modelID;
		this.gameViewController = gameViewController;
		this.resources = resources;
	}

	@Override
	public void run() {
		//gameViewController.setResourceCards(modelID, resources);
	}

}
