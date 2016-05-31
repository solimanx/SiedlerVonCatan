package server.controller;

import server.server.ServerInputHandler;
import server.server.ServerOutputHandler;
import server.server.Server;

import java.io.IOException;

import server.controller.GameController;

public class ServerNetworkController {

	private GameController gameController;
	private ServerOutputHandler outputHandler;
	private ServerInputHandler inputHandler;
	private Server server;

	public ServerNetworkController(GameController gc) {
		this.gameController = gc;
		this.server = new Server();
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.outputHandler = new ServerOutputHandler(this, server);
		this.inputHandler = new ServerInputHandler(this);
	}
}
