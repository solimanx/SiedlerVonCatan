package view.runnables;

import view.GameViewController;

// TODO: Auto-generated Javadoc
public class ServerResponseRunnable implements Runnable {

	final String message;
	final GameViewController gameViewController;

	/**
	 * Instantiates a new server response runnable.
	 *
	 * @param message the message
	 * @param gameViewController the game view controller
	 */
	public ServerResponseRunnable(String message, GameViewController gameViewController) {
		super();
		this.message = message;
		this.gameViewController = gameViewController;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		gameViewController.setServerResponse(message);
	}

}
