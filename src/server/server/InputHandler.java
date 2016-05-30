package server.server;

import server.controller.NetworkController;
import parsing.Parser;

public class InputHandler {
	private Parser parser;
	private NetworkController networkController;

	public InputHandler(NetworkController nc) {
		this.networkController = nc;
		this.parser = new Parser();
	}

}
