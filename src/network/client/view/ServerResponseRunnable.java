package network.client.view;

public class ServerResponseRunnable implements Runnable {

	final String message;
	final GameViewController gameViewController;

	public ServerResponseRunnable(String message, GameViewController gameViewController) {
		super();
		this.message = message;
		this.gameViewController = gameViewController;
	}

	@Override
	public void run() {
		gameViewController.setServerResponse(message);
	}

}
