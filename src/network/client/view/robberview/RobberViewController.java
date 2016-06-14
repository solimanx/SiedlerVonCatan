package network.client.view.robberview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.client.view.GameViewController;

public class RobberViewController {

	@FXML
	private GridPane grid;

	private Spinner<Integer> woodSpinner;

	private Spinner<Integer> claySpinner;

	private Spinner<Integer> woolSpinner;

	private Spinner<Integer> cornSpinner;

	private Spinner<Integer> oreSpinner;

	@FXML
	private Button okButton;

	@FXML
	private Label label;
	
	@FXML
	private Label toGive;
	
	private SimpleIntegerProperty toGiveProperty;

	private int[] result = new int[5];

	private GameViewController gvc;

	public void init(GameViewController gvc) {
		this.gvc = gvc;
	}

	@FXML
    void handleOKButton(ActionEvent event) {
		gvc.robberLoss(result);
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
    }

	public void createSpinner(int[] resources) {
		int sum = 0;
		for (int i = 0; i < resources.length; i++) {
			 sum = sum + resources[i];
		}
		toGiveProperty.set(sum);

		woodSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[0], 0));
		claySpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[1], 0));
		woolSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[3], 0));
		cornSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[4], 0));
		oreSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[2], 0));

		woodSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0] = newVal;
		});

		claySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1] = newVal;
		});

		woolSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[2] = newVal;
		});

		cornSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[3] = newVal;
		});

		oreSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[4] = newVal;
		});

		grid.add(woodSpinner,1,0);
		grid.add(claySpinner,1,1);
		grid.add(woolSpinner,1,2);
		grid.add(cornSpinner,1,3);
		grid.add(oreSpinner,1,4);

	}
}
