package view.runnables;

import view.GameViewController;

// TODO: Auto-generated Javadoc
public class PlayerResourceUpdateRunnable implements Runnable {

	final int modelID;
	final GameViewController gameViewController;
	final int[] resources;

	/**
	 * Instantiates a new player resource update runnable.
	 *
	 * @param modelID the model ID
	 * @param gameViewController the game view controller
	 * @param resources the resources
	 */
	public PlayerResourceUpdateRunnable(int modelID, GameViewController gameViewController, int[] resources) {
		super();
		this.modelID = modelID;
		this.gameViewController = gameViewController;
		this.resources = resources;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//gameViewController.setResourceCards(modelID, resources);
	}

}
