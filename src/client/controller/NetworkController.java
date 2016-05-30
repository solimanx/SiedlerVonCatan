package client.controller;

import client.client.InputHandler;
import client.client.OutputHandler;

public class NetworkController {
	
	private FlowController flowController;
	private OutputHandler outputHandler;
	private InputHandler inputHandler;

	public NetworkController(FlowController fc){
		this.flowController = fc;
		this.outputHandler = new OutputHandler(this);
		this.inputHandler = new InputHandler(this);
	}

}
