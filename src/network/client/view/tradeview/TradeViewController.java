package network.client.view.tradeview;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import network.client.controller.ViewController;

public class TradeViewController {

	@FXML
	private Button placeOfferButton;

	@FXML
	private ListView<String> foreignTrades;

	@FXML
	private ListView<String> ownOffers;

	@FXML
	private Button cancelOffer;

	@FXML
	private Button tradeButton;

	private Spinner<Integer> giveWoodSpinner;
	private Spinner<Integer> giveClaySpinner;
	private Spinner<Integer> giveWoolSpinner;
	private Spinner<Integer> giveCornSpinner;
	private Spinner<Integer> giveOreSpinner;
	private Spinner<Integer> getWoodSpinner;
	private Spinner<Integer> getClaySpinner;
	private Spinner<Integer> getWoolSpinner;
	private Spinner<Integer> getCornSpinner;
	private Spinner<Integer> getOreSpinner;
	private int[][] result = new int[2][5];

	@FXML
	private GridPane grid;

	private ObservableList<String> tradeList;
	private ObservableList<String> ownOfferList;
	
	private String selectedTrade;
	private String selectedOffer;

	private HashMap<String, Integer> stringToTradeID = new HashMap<String, Integer>();
	private HashMap<Integer, String> tradeIDtoString = new HashMap<Integer, String>();
	private HashMap<Integer, Integer> tradeIDtoPlayerID = new HashMap<Integer, Integer>();
	
	private ViewController viewController;

	/**
	 * Initializes the Trade window with spinners
	 * 
	 * @param resources
	 */
	public void init(int[] resources, ViewController viewController) {
		this.viewController = viewController;
		giveWoodSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[0], 0));
		giveClaySpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[1], 0));
		giveWoolSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[3], 0));
		giveCornSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[4], 0));
		giveOreSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[2], 0));
		getWoodSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		getClaySpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		getWoolSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		getCornSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		getOreSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));

		giveWoodSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0][0] = newVal;
		});

		giveClaySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0][1] = newVal;
		});

		giveWoolSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0][3] = newVal;
		});

		giveCornSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0][4] = newVal;
		});

		giveOreSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0][2] = newVal;
		});

		getWoodSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1][0] = newVal;
		});

		getClaySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1][1] = newVal;
		});

		getWoolSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1][3] = newVal;
		});

		getCornSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1][4] = newVal;
		});

		getOreSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1][2] = newVal;
		});

		grid.add(giveWoodSpinner, 1, 1);
		grid.add(giveClaySpinner, 1, 2);
		grid.add(giveWoolSpinner, 1, 3);
		grid.add(giveCornSpinner, 1, 4);
		grid.add(giveOreSpinner, 1, 5);
		grid.add(getWoodSpinner, 2, 1);
		grid.add(getClaySpinner, 2, 2);
		grid.add(getWoolSpinner, 2, 3);
		grid.add(getCornSpinner, 2, 4);
		grid.add(getOreSpinner, 2, 5);


		foreignTrades.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		foreignTrades.setItems(tradeList);
		foreignTrades.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				selectedTrade = newValue;

			}
		});

		ownOffers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		ownOffers.setItems(ownOfferList);
		ownOffers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				selectedOffer = newValue;

			}

		});
	}

	@FXML
	void handlePlaceOfferButton(ActionEvent event) {
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result.length; j++) {
				if (result[i][j] != 0) {
					viewController.getClientController().tradeRequest(result);
					break;
				}
				break;
			}
		}
	}

	@FXML
	void handleTradeButton(ActionEvent event) {
		int tradeID = stringToTradeID.get(selectedTrade);
		viewController.getClientController().tradeConfirmation(tradeIDtoPlayerID.get(tradeID), tradeID);
	}

	public void addTrade(int[][] tradeResources, int tradeID, int playerID) {
		String tradeString = tradeStringGenerator(tradeResources) + "\n" + "With:" + viewController.getGameViewController().getPlayerNames(playerID);
		tradeIDtoString.put(tradeID, tradeString);
		stringToTradeID.put(tradeString, tradeID);
		tradeIDtoPlayerID.put(tradeID, playerID);
		tradeList.add(tradeString);
	}

	public void addOwnOffer(int[][] tradeResources, int tradeID) {
		String offerString = tradeStringGenerator(tradeResources);
		tradeIDtoString.put(tradeID, offerString);
		stringToTradeID.put(offerString, tradeID);
		ownOfferList.add(offerString);
	}

	private String tradeStringGenerator(int[][] tradeResources) {
		if (tradeResources[0].length > 0) {
			// get is set
			int getWood = tradeResources[0][0];
			int getClay = tradeResources[0][1];
			int getOre = tradeResources[0][3];
			int getSheep = tradeResources[0][4];
			int getCorn = tradeResources[0][2];
			String getting = "Get " + getWood + " Wood, " + getClay + " Clay, " + getSheep + " Wool, " + getCorn
					+ " Corn, " + getOre + " Ore" + "\n";
			if (tradeResources[1].length > 0) {
				int giveWood = tradeResources[1][0];
				int giveClay = tradeResources[1][1];
				int giveOre = tradeResources[1][3];
				int giveSheep = tradeResources[1][4];
				int giveCorn = tradeResources[1][2];
				String giving = "Give " + giveWood + " Wood, " + giveClay + " Clay, " + giveSheep + " Wool, " + giveCorn
						+ " Corn, " + giveOre + " Ore";
				return getting + giving;
			}

		}

		return "";

	}

}
