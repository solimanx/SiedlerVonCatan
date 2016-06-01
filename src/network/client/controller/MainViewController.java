package network.client.controller;

import enums.CornerStatus;
import enums.ResourceType;
import network.client.view.View;

public class MainViewController {
	private View view;
	private ViewController viewController;

	public MainViewController(View view, ViewController vc) {
		super();
		this.viewController = vc;
		this.view = view;
		init();
	}

	private void init() {
		view.chatInput.setOnAction(event -> {
			String message = view.chatInput.getText();
			view.chatInput.clear();
			// vc.chatMessageSent(message);
			try {
				viewController.flowController.networkController.client.write(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// view.messages.appendText(message + "\n");
		});
	}

	private void setChat() {
		view.chatInput.setOnAction(event -> {
			String message = view.chatInput.getText();
			view.chatInput.clear();
			// vc.chatMessageSent(message);
			try {
				viewController.flowController.chatSendMessage(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		viewController.flowController.requestBuildStreet(streetCoordinates[0], streetCoordinates[1],
				streetCoordinates[2]);

	}

	public void fieldClick(int[] fieldCoordinates) {
		System.out.println(
				"Clicked on " + fieldCoordinates[0] + " , " + fieldCoordinates[1] + " , " + fieldCoordinates[2]);

	}

	public void receiveChatMessage(String line) {
		view.messages.appendText(line + "\n");
	}

	public void setStreet(int u, int v, int dir, int playerID) {
		view.setStreet(u, v, dir, viewController.getPlayerColor(playerID));
		// TODO Auto-generated method stub

	}

	public void setBandit(int u, int v) {
		view.setBandit(u, v);
		// TODO Auto-generated method stub

	}

	public void setCorner(int x, int y, int dir, CornerStatus village, int playerId) {
		// TODO Auto-generated method stub

	}
	
	public void setField(int u, int v, ResourceType resourceType, int diceIndex) {
		view.setFieldResourceType(u, v, viewController.fieldColors.get(resourceType));
		view.setFieldChip(u, v, diceIndex);
	}

}
