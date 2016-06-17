package network.client.view.devcardview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	void init(ViewController viewController, Stage stage) {
		this.stage = stage;
		this.viewController = viewController;
		devCardListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		devCardListView.setItems(devCardList);
		devCardListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				devCardSelected = newValue;
				playCardButton.setDisable(false);

			}
		});
	}

	@FXML
	void handleCancel(ActionEvent event) {

	}

	@FXML
	void handleOK(ActionEvent event) {

	}

	@FXML
	void handlePlayCardButton(ActionEvent event) {
		if (devCardSelected.equals(CardType.KNIGHT.toString())) {
			stage.hide();
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
						.load(getClass().getResource("/network/client/view/tradeview/inventionView.fxml").openStream());
				Scene scene = new Scene(root);
				Stage inventionStage = new Stage();
				inventionStage.setScene(scene);
				inventRChooser1.getItems().addAll(ResourceType.values());
				inventRChooser2.getItems().addAll(ResourceType.values());
				inventionStage.initModality(Modality.WINDOW_MODAL);
				inventionStage.initOwner(this.stage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
