package client.controller;

import client.view.View;

public class MainViewController {
	private View view;
	private ViewController vc;

	public MainViewController(View view, ViewController vc) {
		super();
		this.vc = vc;
		this.view = view;
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

}
