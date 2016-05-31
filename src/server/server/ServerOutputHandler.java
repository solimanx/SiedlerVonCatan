package server.server;

import server.controller.ServerNetworkController;

public class ServerOutputHandler {
	private ServerNetworkController networkController;
	private Server server;

	public ServerOutputHandler(ServerNetworkController nc, Server server) {
		this.networkController = nc;
		this.server = server;
	}
}
