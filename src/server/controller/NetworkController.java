package server.controller;

import server.server.InputHandler;
import server.server.OutputHandler;
import server.controller.GameController;

public class NetworkController {

	private GameController gameController;
	private OutputHandler outputHandler;
	private InputHandler inputHandler;

	public NetworkController(GameController gc){
		this.gameController = gc;
		this.outputHandler = new OutputHandler(this);
		this.inputHandler = new InputHandler(this);
	}
}
