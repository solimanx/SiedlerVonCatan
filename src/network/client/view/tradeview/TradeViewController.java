package network.client.view.tradeview;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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

	@FXML
	private Button acceptButton;
	
	@FXML
	private TextField ownOffer;

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
	private int[] resultOffer = new int[5];
	private int[] resultDemand = new int[5];

	@FXML
	private GridPane grid;

	private ObservableList<String> tradeList = FXCollections.observableArrayList();
	private ObservableList<String> ownOfferList = FXCollections.observableArrayList();

	private String selectedTrade;
	private String selectedOffer;

	private HashMap<String, Integer> stringToTradeID = new HashMap<String, Integer>();
	private HashMap<Integer, String> tradeIDtoString = new HashMap<Integer, String>();
	private HashMap<Integer, Integer> tradeIDtoPlayerID = new HashMap<Integer, Integer>();

	private ViewController viewController;

	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * Initializes the Trade window with spinners
	 * 
	 * @param resources
	 */
	public void init(int[] resources, ViewController viewController) {
		this.viewController = viewController;
		updateSpinner(resources);

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

	/**
	 * @param resources
	 */
	public void updateSpinner(int[] resources) {
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
			resultOffer[0] = newVal;
		});

		giveClaySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultOffer[1] = newVal;
		});

		giveWoolSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultOffer[3] = newVal;
		});

		giveCornSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultOffer[4] = newVal;
		});

		giveOreSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultOffer[2] = newVal;
		});

		getWoodSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultDemand[0] = newVal;
		});

		getClaySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultDemand[1] = newVal;
		});

		getWoolSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultDemand[3] = newVal;
		});

		getCornSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultDemand[4] = newVal;
		});

		getOreSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			resultDemand[2] = newVal;
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
	}

	@FXML
	void handlePlaceOfferButton(ActionEvent event) {
		viewController.getClientController().requestTrade(resultOffer, resultDemand);
	}

	@FXML
	void handleTradeButton(ActionEvent event) {
		int tradeID = stringToTradeID.get(selectedTrade);
		viewController.getClientController().acceptTrade(tradeID);
	}

	void handleAcceptButton() {
		int offerID = stringToTradeID.get(selectedOffer);
		int playerID = tradeIDtoPlayerID.get(selectedOffer);
		viewController.getClientController().fulfillTrade(offerID, playerID);
		ownOfferList.clear();
		ownOffer.clear();
	}
	
	public void offerFulfilled(int tradeID){
		tradeList.remove(tradeIDtoString.get(tradeID));
		
	}

	public void addOffer(int[] offer, int[] demand, int tradeID, int playerID) {
		String tradeString = tradeStringGenerator(offer, demand) + "\n" + "With:"
				+ viewController.getGameViewController().getPlayerNames(playerID);
		tradeIDtoString.put(tradeID, tradeString);
		stringToTradeID.put(tradeString, tradeID);
		tradeIDtoPlayerID.put(tradeID, playerID);
		tradeList.add(tradeString);
	}

	public void addOwnOffer(int[] offer, int[] demand, int tradeID) {
		String offerString = tradeStringGenerator(offer, demand);
		tradeIDtoString.put(tradeID, offerString);
		stringToTradeID.put(offerString, tradeID);
		ownOffer.setText(offerString);
	}
	
	public void acceptingOffer(int playerID, int tradeID){
		String offerString = viewController.getGameViewController().getPlayerNames(playerID) + " accepts your offer";
		ownOfferList.add(offerString);
		
	}
	
	public void cancelOwnOffer(){
		ownOffer.clear();
	}
	
	public void cancelOffer(int tradeID){
		tradeList.remove(tradeIDtoString.get(tradeID));
	}

	private String tradeStringGenerator(int[] offer, int[] demand) {
		String getting = "";
		String giving = "";
		if (demand.length > 0) {
			// get is set
			int getWood = demand[0];
			int getClay = demand[1];
			int getOre = demand[3];
			int getSheep = demand[4];
			int getCorn = demand[2];
			getting = "Get " + getWood + " Wood, " + getClay + " Clay, " + getSheep + " Wool, " + getCorn + " Corn, "
					+ getOre + " Ore" + "\n";
		}
		if (offer.length > 0) {
			int giveWood = offer[0];
			int giveClay = offer[1];
			int giveOre = offer[3];
			int giveSheep = offer[4];
			int giveCorn = offer[2];
			giving = "Give " + giveWood + " Wood, " + giveClay + " Clay, " + giveSheep + " Wool, " + giveCorn
					+ " Corn, " + giveOre + " Ore";
		}
		return getting + giving;
	}

}
