package client.client;

import client.controller.NetworkController;

public class OutputHandler {

	private NetworkController networkController;
	private Client client;

	public OutputHandler(NetworkController nc, Client client) {
		this.networkController = nc;
		this.client = client;
	}

}
