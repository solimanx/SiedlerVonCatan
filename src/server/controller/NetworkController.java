package server.controller;

import server.server.InputHandler;
import server.server.OutputHandler;
import server.server.Server;
import server.controller.GameController;

public class NetworkController {

	private GameController gameController;
	private OutputHandler outputHandler;
	private InputHandler inputHandler;
	private Server server;

	public NetworkController(GameController gc){
		this.gameController = gc;
		this.server = new Server();
		server.start();
		this.outputHandler = new OutputHandler(this,server);
		this.inputHandler = new InputHandler(this);
	}
}
