package network.client.view.tradeview;

import java.util.HashMap;
import audio.Soundeffects;
import enums.PlayerState;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.unit.Trade;
import network.client.controller.ViewController;

import static sounds.Sound.*;

// TODO: Auto-generated Javadoc
public class TradeViewController {

	@FXML
	private Button cancelTrade;

	@FXML
	private Button placeOfferButton;

	// @FXML
	// private ListView<String> foreignTrades;

	@FXML
	private TableView<Trade> tradeTable;

	private ObservableList<Trade> trades = FXCollections.observableArrayList();

	@FXML
	private TableColumn<Trade, String> tradeStringColumn;

	@FXML
	private TableColumn<Trade, String> statusColumn;

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
	private Button declineTrade;

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

	public SimpleBooleanProperty isPlayerTradingStatus = new SimpleBooleanProperty();

	private Trade selectedTrade;
	private String selectedOffer;

	public HashMap<String, Integer> stringToTradeID = new HashMap<String, Integer>();
	public HashMap<Integer, String> tradeIDtoString = new HashMap<Integer, String>();
	public HashMap<String, Integer> acceptedOfferToModelID = new HashMap<String, Integer>();
	// public HashMap<Integer, Integer> tradeIDtoModelID = new HashMap<Integer,
	// Integer>();
	public HashMap<Integer, String> playerIDtoString = new HashMap<Integer, String>();

	private ViewController viewController;
	private int ownTradeID = 0;
	private Stage stage;

	/**
	 * Sets the view controller.
	 *
	 * @param viewController
	 *            the new view controller
	 */
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * Initializes the Trade window with spinners.
	 *
	 * @param resources
	 *            the resources
	 * @param viewController
	 *            the view controller
	 * @param stage
	 *            the stage
	 */
	public void init(int[] resources, ViewController viewController, Stage stage) {
		this.viewController = viewController;
		this.stage = stage;
		start(resources);
		cancelOffer.setDisable(true);
		tradeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tradeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Trade>() {

			@Override
			public void changed(ObservableValue<? extends Trade> observable, Trade oldValue, Trade newValue) {
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

		placeOfferButton.disableProperty().bind(isPlayerTradingStatus.not());

		tradeStringColumn.setCellValueFactory(cellData -> cellData.getValue().tradeStringProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		tradeTable.setItems(trades);

		// sbp = new
		// SimpleBooleanProperty(selectedTrade.getStatus().equals("ACCEPTED"));
		// tradeButton.disableProperty().bind(sbp);

	}

	/**
	 * Start.
	 *
	 * @param selfResources
	 *            the self resources
	 */
	public void start(int[] selfResources) {
		resultDemand = new int[5];
		resultOffer = new int[5];
		updateSpinner(selfResources, grid);
		updateSpinner(selfResources, harbourGrid);
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
	 * Update spinner.
	 *
	 * @param resources
	 *            the resources
	 * @param grid
	 *            the grid
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
	 * Handle place offer button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handlePlaceOfferButton(ActionEvent event) {
		viewController.getClientController().requestTrade(resultOffer, resultDemand);
		resultDemand = new int[5];
		resultOffer = new int[5];
		updateSpinner(resultDemand, grid);
		tradeButton.setDisable(true);
		playMakeOfferSound();
	}

	/**
	 * Handle Accept button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleTradeButton(ActionEvent event) {
		if (selectedTrade != null) {
			int tradeID = selectedTrade.getTradeID();// stringToTradeID.get(selectedTrade);
			// int index = tradeList.indexOf(selectedTrade);
			// String newTradeString =
			// selectedTrade.getTradeString().getValue();//selectedTrade +
			// "\nYOU
			// ACCEPTED";

			if (!selectedTrade.getStatus().equals("ACCEPTED")) { // tradeIDtoString.get(tradeID).endsWith("ACCEPTED"))
																	// {

				viewController.getClientController().acceptTrade(tradeID);

				// tradeIDtoString.put(tradeID, newTradeString);
				// stringToTradeID.remove(selectedTrade);
				// stringToTradeID.put(newTradeString, tradeID);

				// tradeIDtoString.put(viewController.getClientController().getOwnPlayerID(),
				// newTradeString);

			}
		}
	}

	public void setAccepted(int tradeID) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				for (Trade trade : trades) {
					if (trade.getTradeID() == tradeID) {
						trade.setStatus("ACCEPTED");
						playTradeButtonSound();
					}
				}
			};
		});
	}

	/**
	 * Handle decline trade.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleDeclineTrade(ActionEvent event) {
		try {
			int tradeID = selectedTrade.getTradeID();
			if (selectedTrade.getStatus().equals("ACCEPTED")) {
				viewController.getClientController().cancelTrade(tradeID);
				playCancelTradeSound();
				selectedTrade.setStatus("");
			} else if (selectedTrade.getStatus().equals("")) {
				selectedTrade.setStatus("DECLINED");
				viewController.getClientController().declineTrade(tradeID);
				playDeclineTradeOfferSound();
			}
		} catch (NullPointerException e) {
		}

		// int tradeID = stringToTradeID.get(selectedTrade);
		// int index = tradeList.indexOf(selectedTrade);
		// if (!tradeIDtoString.get(tradeID).endsWith("ACCEPTED") &&
		// !tradeIDtoString.get(tradeID).endsWith("DECLINED")) {
		// String newTradeString = selectedTrade + "\nYOU DECLINED";
		// tradeList.set(index, newTradeString);
		// tradeIDtoString.put(tradeID, newTradeString);
		// stringToTradeID.remove(selectedTrade);
		// stringToTradeID.put(newTradeString, tradeID);
		//
		// tradeIDtoString.put(viewController.getClientController().getOwnPlayerID(),
		// newTradeString);
		//
		// viewController.getClientController().declineTrade(tradeID);
		//
		// }
	}

	/**
	 * Handle sea trade button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleSeaTradeButton(ActionEvent event) {
		if (viewController.getClientController().getGameLogic().getBoard().getPlayer(0)
				.getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
			if (!Soundeffects.isMuted()){
			Soundeffects.HARBOUR.play();
			}
			viewController.getClientController().requestSeaTrade(resultOffer, resultDemand);
			stage.hide();
			resultDemand = new int[5];
			resultOffer = new int[5];
		}

	}

	/**
	 * Handle cancel own offer.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleCancelOwnOffer(ActionEvent event) {
		// viewController.getClientController().tradeCancelled(viewController.getClientController().getOwnPlayerID(),
		// ownTradeID);
		viewController.getClientController().cancelTrade(ownTradeID);
		resultDemand = new int[5];
		resultOffer = new int[5];
		playCancelOfferSound();
	}

	/**
	 * Full fill trade.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void fullFillTrade(ActionEvent event) {
		if (viewController.getClientController().getGameLogic().getBoard().getPlayer(0)
				.getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
			viewController.getClientController().fulfillTrade(ownTradeID, acceptedOfferToModelID.get(selectedOffer));

			stage.hide();
			playFullFillTradeSound();
		}
	}

	// /**
	// * Handle cancel trade.
	// *
	// * @param event
	// * the event
	// */
	// @FXML
	// void handleCancelTrade(ActionEvent event) {
	// Integer tradeID = stringToTradeID.get(selectedTrade);
	// int index = tradeList.indexOf(selectedTrade);
	// if (tradeIDtoString.get(tradeID).endsWith("ACCEPTED")) {
	// String newSelectedTrade = selectedTrade.replace("\nYOU ACCEPTED", "");
	// tradeList.set(index, newSelectedTrade);
	// tradeIDtoString.put(tradeID, newSelectedTrade);
	// stringToTradeID.remove(selectedTrade);
	// stringToTradeID.put(newSelectedTrade, tradeID);
	//
	// tradeIDtoString.put(viewController.getClientController().getOwnPlayerID(),
	// newSelectedTrade);
	//
	// viewController.getClientController().cancelTrade(tradeID);
	// }
	// }

	/**
	 * Offer fulfilled.
	 *
	 * //* @param threadID the thread ID
	 *
	 * @param modelID
	 *            the model ID
	 * @param partnerModelID
	 *            the partner model ID
	 */
	public void offerFulfilled(int modelID, int partnerModelID) {
		if (modelID == 0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ownOffer.clear();
					ownOfferList.clear();
				}
			});
		}
		try {
			tradeList.remove(playerIDtoString.get(partnerModelID));
		} catch (Exception e) {
			viewController.getGameViewController().alert("Couldn't delete trade!");
		}

	}

	/**
	 * Adds the offer.
	 *
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 * @param tradeID
	 *            the trade ID
	 * @param playerID
	 *            the player ID
	 */
	public void addOffer(int[] offer, int[] demand, int tradeID, int playerID) {
		String tradeString = tradeStringGenerator(offer, demand) + "\n" + "From: "
				+ viewController.getGameViewController().getPlayerNames(playerID);
		Trade trade = new Trade(tradeString, tradeID, playerID);
		trades.add(trade);
		// tradeIDtoString.put(tradeID, tradeString);
		// stringToTradeID.put(tradeString, tradeID);
		// tradeIDtoModelID.put(tradeID, playerID);
		// playerIDtoString.put(playerID, tradeString);
		Platform.runLater(new AddTradeStringRunnable(tradeString));
		viewController.getGameViewController().notify("New Trade", "New trade request:\n" + tradeString);

	}

	/**
	 * Adds the own offer.
	 *
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 * @param tradeID
	 *            the trade ID
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
	 * called, when player accepts your offer and adds offer to ownOfferList.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradeID
	 *            the trade ID
	 */
	public void acceptingOffer(int modelID, int tradeID) {
		String offerString = viewController.getClientController().getGameLogic().getBoard().getPlayer(modelID).getName()
				+ " accepts your offer";
		Platform.runLater(new AddOfferStringRunnable(offerString));
		// int playerID =
		// viewController.getClientController().getPlayerID(modelID);
		acceptedOfferToModelID.put(offerString, modelID);

	}

	/**
	 * cancels trade item from left tradeList.
	 *
	 * @param tradeID
	 *            the trade ID
	 */
	public void cancelOffer(int tradeID, int modelID) {
		for (int i = 0; i < trades.size(); i++) {
			Trade trade = trades.get(i);
			if (trade.getTradeID() == tradeID && trade.getPartnerID() == modelID) {
				Platform.runLater(new RemoveOfferStringRunnable(i));
				break;
			}

		}
	}

	/**
	 * Trade string generator.
	 *
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 * @return the string
	 */
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
	 * own offer accepting players list.
	 */
	public void cancelOwnOffer() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				ownOffer.clear();
				ownOfferList.clear();
				cancelOffer.setDisable(true);

			}

		});

	}

	public class AddTradeStringRunnable implements Runnable {
		final String string;

		/**
		 * Instantiates a new adds the trade string runnable.
		 *
		 * @param string
		 *            the string
		 */
		public AddTradeStringRunnable(String string) {
			this.string = string;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			tradeList.add(string);

		}

	}

	public class RemoveTradeStringRunnable implements Runnable {
		final String string;

		/**
		 * Instantiates a new removes the trade string runnable.
		 *
		 * @param string
		 *            the string
		 */
		public RemoveTradeStringRunnable(String string) {
			this.string = string;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			tradeList.remove(string);

		}

	}

	public class AddOfferStringRunnable implements Runnable {
		final String string;

		/**
		 * Instantiates a new adds the offer string runnable.
		 *
		 * @param string
		 *            the string
		 */
		public AddOfferStringRunnable(String string) {
			this.string = string;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			ownOfferList.add(string);

		}

	}

	public class RemoveOfferStringRunnable implements Runnable {
		final int index;

		/**
		 * Instantiates a new removes the offer string runnable.
		 *
		 * @param string
		 *            the string
		 */
		public RemoveOfferStringRunnable(int index) {
			this.index = index;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			trades.remove(index);

		}

	}

	public void setDeclined(int tradingID) {
		for (Trade trade : trades) {
			if (trade.getTradeID() == tradingID) {
				trade.setStatus("DECLINED");
			}
		}
	}

}
