package client.client;

import client.controller.ClientNetworkController;

public class ClientOutputHandler {

	private ClientNetworkController networkController;
	private Client client;

	public ClientOutputHandler(ClientNetworkController nc, Client client) {
		this.networkController = nc;
		this.client = client;
	}

	public void handleBuildRequest(int x, int y, int dir, int playerId, String string) {
		// TODO Auto-generated method stub
		
	}

}
