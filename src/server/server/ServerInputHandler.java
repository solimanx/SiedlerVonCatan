package server.server;

import server.controller.ServerNetworkController;
import parsing.Parser;

public class ServerInputHandler {
	private Parser parser;
	private ServerNetworkController networkController;

	public ServerInputHandler(ServerNetworkController nc) {
		this.networkController = nc;
		this.parser = new Parser();
	}

}
