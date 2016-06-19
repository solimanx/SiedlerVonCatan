package network.client.view.tradeview;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.client.controller.ViewController;

public class TradeViewController {

	@FXML
	private Button cancelTrade;

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

	@FXML
	private GridPane harbourGrid;

	@FXML
	private Button tradeHarbour;

	@FXML
	RadioButton woolHarbour;

	@FXML
	RadioButton woodHarbour;

	@FXML
	RadioButton clayHarbour;

	@FXML
	RadioButton cornHarbour;

	@FXML
	RadioButton oreHarbour;

	@FXML
	RadioButton genericHarbour;

	private ObservableList<String> tradeList = FXCollections.observableArrayList();
	private ObservableList<String> ownOfferList = FXCollections.observableArrayList();

	private String selectedTrade;
	private String selectedOffer;

	private HashMap<String, Integer> stringToTradeID = new HashMap<String, Integer>();
	private HashMap<String, Integer> acceptedOfferToModelID = new HashMap<String, Integer>();
	private HashMap<Integer, String> tradeIDtoString = new HashMap<Integer, String>();
	private HashMap<Integer, Integer> tradeIDtoModelID = new HashMap<Integer, Integer>();

	private ViewController viewController;
	private int ownTradeID = 0;
	private Stage stage;

	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * Initializes the Trade window with spinners
	 * 
	 * @param resources
	 */
	public void init(int[] resources, ViewController viewController, Stage stage) {
		this.viewController = viewController;
		this.stage = stage;
		start(resources);
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

	public void start(int[] selfResources) {
		updateSpinner(selfResources, grid);
		updateSpinner(selfResources, harbourGrid);
		cancelOffer.setDisable(true);
		if (viewController.getClientController().getGameLogic().hasOreHarbour(0)) {
			oreHarbour.setSelected(true);
		}
		if (viewController.getClientController().getGameLogic().hasCornHarbour(0)) {
			cornHarbour.setSelected(true);
		}
		if (viewController.getClientController().getGameLogic().hasClayHarbour(0)) {
			clayHarbour.setSelected(true);
		}
		if (viewController.getClientController().getGameLogic().hasWoodHarbour(0)) {
			woodHarbour.setSelected(true);
		}
		if (viewController.getClientController().getGameLogic().hasThreeOneHarbour(0)) {
			genericHarbour.setSelected(true);
		}
		if (viewController.getClientController().getGameLogic().hasWoolHarbour(0)) {
			woolHarbour.setSelected(true);
		}
	}

	/**
	 * @param resources
	 */
	public void updateSpinner(int[] resources, GridPane grid) {
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

	/**
	 * @param event
	 */
	@FXML
	void handlePlaceOfferButton(ActionEvent event) {
		viewController.getClientController().requestTrade(resultOffer, resultDemand);

	}

	/**
	 * @param event
	 */
	@FXML
	void handleTradeButton(ActionEvent event) {
		int tradeID = stringToTradeID.get(selectedTrade);
		viewController.getClientController().acceptTrade(tradeID);
	}

	/**
	 * @param event
	 */
	@FXML
	void handleSeaTradeButton(ActionEvent event) {
		viewController.getClientController().requestSeaTrade(resultOffer, resultDemand);
		stage.hide();
		resultDemand = new int[5];
		resultOffer = new int[5];
		
	}

	/**
	 * @param event
	 */
	@FXML
	void handleCancelOwnOffer(ActionEvent event) {
		viewController.getClientController().tradeCancelled(viewController.getClientController().getOwnPlayerID(),
				ownTradeID);
	}

	/**
	 * @param event
	 */
	@FXML
	void fullFillTrade(ActionEvent event) {
		viewController.getClientController().fulfillTrade(ownTradeID, acceptedOfferToModelID.get(selectedOffer));
	}

	/**
	 * @param event
	 */
	@FXML
	void handleCancelTrade(ActionEvent event) {
		int tID = stringToTradeID.get(selectedTrade);
		int pID = tradeIDtoModelID.get(tID);
		viewController.getClientController().tradeCancelled(pID, tID);
	}

	/**
	 * @param tradeID
	 */
	public void offerFulfilled(int tradeID) {
		tradeList.remove(tradeIDtoString.get(tradeID));

	}

	/**
	 * @param offer
	 * @param demand
	 * @param tradeID
	 * @param playerID
	 */
	public void addOffer(int[] offer, int[] demand, int tradeID, int playerID) {
		String tradeString = tradeStringGenerator(offer, demand) + "\n" + "From: "
				+ viewController.getGameViewController().getPlayerNames(playerID);
		tradeIDtoString.put(tradeID, tradeString);
		stringToTradeID.put(tradeString, tradeID);
		tradeIDtoModelID.put(tradeID, playerID);
		Platform.runLater(new AddTradeStringRunnable(tradeString));

	}

	/**
	 * @param offer
	 * @param demand
	 * @param tradeID
	 */
	public void addOwnOffer(int[] offer, int[] demand, int tradeID) {
		String offerString = tradeStringGenerator(offer, demand);
		ownTradeID = tradeID;
		tradeIDtoString.put(tradeID, offerString);
		stringToTradeID.put(offerString, tradeID);
		ownOffer.setText(offerString);
		cancelOffer.setDisable(false);
	}

	/**
	 * called, when player accepts your offer and adds offer to ownOfferList
	 * 
	 * @param playerID
	 * @param tradeID
	 */
	public void acceptingOffer(int modelID, int tradeID) {
		String offerString = viewController.getClientController().getGameLogic().getBoard().getPlayer(modelID).getName()
				+ " accepts your offer";
		Platform.runLater(new AddOfferStringRunnable(offerString));
		int playerID = viewController.getClientController().getPlayerID(modelID);
		acceptedOfferToModelID.put(offerString, playerID);

	}

	/**
	 * cancels trade item from left tradeList
	 * 
	 * @param tradeID
	 */
	public void cancelOffer(int tradeID) {
		Platform.runLater(new RemoveOfferStringRunnable(tradeIDtoString.get(tradeID)));
	}

	private String tradeStringGenerator(int[] offer, int[] demand) {
		StringBuffer getting = new StringBuffer();
		StringBuffer giving = new StringBuffer();
		if (demand.length > 0) {

			getting.append("Demand: ");
			// get is set
			int getWood = demand[0];
			int getClay = demand[1];
			int getOre = demand[2];
			int getSheep = demand[3];
			int getCorn = demand[4];

			if (getWood != 0) {
				getting.append(getWood + " Wood, ");
			}

			if (getClay != 0) {
				getting.append(getClay + " Clay, ");
			}

			if (getOre != 0) {
				getting.append(getOre + " Ore, ");
			}

			if (getSheep != 0) {
				getting.append(getSheep + " Wool, ");
			}

			if (getCorn != 0) {
				getting.append(getCorn + " Corn, ");
			}
			// Remove last space
			getting.deleteCharAt(getting.length() - 1);
			// Remove last comma
			getting.deleteCharAt(getting.length() - 1);
			// Add full stop.
			getting.append(" ");
		}
		if (offer.length > 0) {
			giving.append("Offer: ");
			int giveWood = offer[0];
			int giveClay = offer[1];
			int giveOre = offer[2];
			int giveSheep = offer[3];
			int giveCorn = offer[4];
			if (giveWood != 0) {
				giving.append(giveWood + " Wood, ");
			}

			if (giveClay != 0) {
				giving.append(giveClay + " Clay, ");
			}

			if (giveOre != 0) {
				giving.append(giveOre + " Ore, ");
			}

			if (giveSheep != 0) {
				giving.append(giveSheep + " Wool, ");
			}

			if (giveCorn != 0) {
				giving.append(giveCorn + " Corn, ");

			}
			// Remove last space
			giving.deleteCharAt(giving.length() - 1);
			// Remove last comma
			giving.deleteCharAt(giving.length() - 1);
		}
		return getting.toString() + " \n" + giving.toString();
	}

	/**
	 * called, when own offer is cancelled removes own offer from view clears
	 * own offer accepting players list
	 */
	public void cancelOwnOffer() {
		ownOffer.clear();
		ownOfferList.clear();
		cancelOffer.setDisable(true);

	}

	public class AddTradeStringRunnable implements Runnable {
		final String string;

		public AddTradeStringRunnable(String string) {
			this.string = string;
		}

		@Override
		public void run() {
			tradeList.add(string);

		}

	}

	public class RemoveTradeStringRunnable implements Runnable {
		final String string;

		public RemoveTradeStringRunnable(String string) {
			this.string = string;
		}

		@Override
		public void run() {
			tradeList.remove(string);

		}

	}

	public class AddOfferStringRunnable implements Runnable {
		final String string;

		public AddOfferStringRunnable(String string) {
			this.string = string;
		}

		@Override
		public void run() {
			ownOfferList.add(string);

		}

	}

	public class RemoveOfferStringRunnable implements Runnable {
		final String string;

		public RemoveOfferStringRunnable(String string) {
			this.string = string;
		}

		@Override
		public void run() {
			ownOfferList.remove(string);

		}

	}

}
