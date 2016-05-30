package server.server;

import server.controller.NetworkController;

public class OutputHandler {
	private NetworkController networkController;
	private Server server;

	public OutputHandler(NetworkController nc,Server server){
		this.networkController = nc;
		this.server = server;
	}
}
