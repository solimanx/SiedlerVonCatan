package client.controller;

import client.view.View;

public class MainViewController {
	private View view;
	private ViewController vc;

	public MainViewController(View view, ViewController vc) {
		super();
		this.vc = vc;
		this.view = view;
		init();
	}

	private void init() {
		view.chatInput.setOnAction(event -> {
			String message = view.chatInput.getText();
			view.chatInput.clear();
//			vc.chatMessageSent(message);
			try {
				vc.client.write(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			view.messages.appendText(message + "\n");
		});			
	}

	public View getView() {
		return view;
	}

	public void villageClick(int[] villageCoordinates) {
		System.out.println("Clicked on Village " + villageCoordinates[0] + " , " + villageCoordinates[1] + " , "
				+ villageCoordinates[2]);

	}

	public void streetClick(int[] streetCoordinates) {
		System.out.println("Clicked on Street " + streetCoordinates[0] + " , " + streetCoordinates[1] + " , "
				+ streetCoordinates[2]);

	}

	public void fieldClick(int[] fieldCoordinates) {
		System.out.println(
				"Clicked on " + fieldCoordinates[0] + " , " + fieldCoordinates[1] + " , " + fieldCoordinates[2]);

	}
	
	public void chatMessageSent(String message){
		System.out.println("trying to send message: " + message);
	}

	public void receiveChatMessage(String line) {
		//view.messages.appendText(line +"\n");
		System.out.println(line);
	}


}
