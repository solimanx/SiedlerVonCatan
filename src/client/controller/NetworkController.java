package client.controller;

import client.client.Client;
import client.client.InputHandler;
import client.client.OutputHandler;

public class NetworkController {
	
	private FlowController flowController;
	private OutputHandler outputHandler;
	private InputHandler inputHandler;
	private Client client;

	public NetworkController(FlowController fc){
		this.flowController = fc;
		this.client = new Client();
		client.start();
		this.outputHandler = new OutputHandler(this,client);
		this.inputHandler = new InputHandler(this);
	}

}
