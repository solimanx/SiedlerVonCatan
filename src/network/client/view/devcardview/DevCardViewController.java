package network.client.view.devcardview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CardType;
import enums.ResourceType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.client.controller.ViewController;
import network.client.view.GameViewController;
import network.client.view.tradeview.TradeViewController;

public class DevCardViewController implements Initializable {

	@FXML
	private ListView<String> devCardListView;

	@FXML
	private Button playCardButton;

	@FXML
	private Button cancelButton;

	@FXML
	private ComboBox<String> resourceA;

	@FXML
	private ComboBox<String> resourceB;

	@FXML
	private Button okButton;

	private ViewController viewController;

	private ObservableList<String> devCardList = FXCollections.observableArrayList();

	private String devCardSelected;

	private Stage stage;

	@FXML
	private ComboBox<ResourceType> monopolyRChooser;

	@FXML
	private Button monopolyOK;

	@FXML
	private ComboBox<ResourceType> inventRChooser1;

	@FXML
	private ComboBox<ResourceType> inventRChooser2;

	@FXML
	private Button inventionOK;
	
	private static Logger logger = LogManager.getLogger(DevCardViewController.class.getSimpleName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void init(ViewController viewController, Stage stage, int[] devCards) {
		this.stage = stage;
		this.viewController = viewController;
		devCardList.clear();
		devCardListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		devCardListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				devCardSelected = newValue;
				playCardButton.setDisable(false);

			}
		});

		for (int i = 0; i < devCards.length; i++) {
			for (int j = 0; j < devCards[i]; j++) {
				String string = "";
				switch (i) {
				case 0:
					string = CardType.KNIGHT.toString();
					break;
				case 1:
					string = CardType.VICTORYPOINT.toString();
					break;
				case 2:
					string = CardType.INVENTION.toString();
					break;
				case 3:
					string = CardType.MONOPOLY.toString();
					break;
				case 4:
					string = CardType.STREET.toString();
					break;
				}
				devCardList.add(string);
			}
		}
		devCardListView.setItems(devCardList);
	}

	@FXML
	void handleCancel(ActionEvent event) {
		this.stage.close();
	}

	@FXML
	void handleInventionOK(ActionEvent event) {
		int[] resources = { 0, 0, 0, 0, 0 };
		switch (inventRChooser1.getSelectionModel().getSelectedItem()) {
		case WOOD:
			resources[0] += 1;
			break;
		case CLAY:
			resources[1] += 1;
			break;
		case SHEEP:
			resources[3] += 1;
			break;
		case CORN:
			resources[4] += 1;
			break;
		case ORE:
			resources[2] += 1;
			break;
		}
		switch (inventRChooser2.getSelectionModel().getSelectedItem()) {
		case WOOD:
			resources[0] += 1;
			break;
		case CLAY:
			resources[1] += 1;
			break;
		case SHEEP:
			resources[3] += 1;
			break;
		case CORN:
			resources[4] += 1;
			break;
		case ORE:
			resources[2] += 1;
			break;
		}
		viewController.getClientController().playInventionCard(resources);
		this.stage.close();
	}

	@FXML
	void handleMonopolyOK(ActionEvent event) {

		viewController.getClientController().playMonopolyCard(monopolyRChooser.getSelectionModel().getSelectedItem());
		this.stage.close();
	}

	@FXML
	void handlePlayCardButton(ActionEvent event) {
		if (devCardSelected.equals(CardType.KNIGHT.toString())) {
			stage.hide();
			viewController.getGameViewController().setKnight(true);
			viewController.getGameViewController().setMoveRobberState();
		} else if (devCardSelected.equals(CardType.STREET.toString())) {
			viewController.getGameViewController().setIsStreetDev(true);
		} else if (devCardSelected.equals(CardType.MONOPOLY.toString())) {
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			try {
				root = loader
						.load(getClass().getResource("/network/client/view/tradeview/monopolyView.fxml").openStream());
				Scene scene = new Scene(root);
				Stage monopolyStage = new Stage();
				monopolyStage.setScene(scene);
				monopolyRChooser.getItems().addAll(ResourceType.values());
				monopolyStage.initModality(Modality.WINDOW_MODAL);
				monopolyStage.initOwner(this.stage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (devCardSelected.equals(CardType.INVENTION.toString())) {
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			try {
				root = loader
						.load(getClass().getResource("/network/client/view/tradeview/InventionView.fxml").openStream());
				Scene scene = new Scene(root);
				Stage inventionStage = new Stage();
				inventionStage.setScene(scene);
				inventRChooser1.getItems().addAll(ResourceType.values());
				inventRChooser2.getItems().addAll(ResourceType.values());
				inventionStage.initModality(Modality.WINDOW_MODAL);
				inventionStage.initOwner(this.stage);
			} catch (IOException e) {
				logger.error("Input/Output Exception", e);
				logger.catching(Level.ERROR, e);
				e.printStackTrace();
			}

		}

	}

}
