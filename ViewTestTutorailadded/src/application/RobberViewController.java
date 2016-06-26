package application;

import application.GameViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
	
	private int[] result = new int[5];
	
	private GameViewController gvc;
	
	public void init(GameViewController gvc) {
		this.gvc = gvc;
	}
	
	@FXML
    void handleOKButton(ActionEvent event) {
		GameViewController.robberLoss(result);
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
    }

	public void createSpinner(int[] resources) {
//		claySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
//			System.out.println(newVal);
//		});
		woodSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, resources[0], 0));
		claySpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, resources[1], 0));
		woolSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, resources[3], 0));
		cornSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, resources[4], 0));
		oreSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, resources[2], 0));
		
		woodSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[0] = newVal;
		});
		
		claySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[1] = newVal;
		});
		
		woolSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[3] = newVal;
		});
		
		cornSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[4] = newVal;
		});
		
		oreSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
			result[2] = newVal;
		});
		
		grid.add(woodSpinner,0,0);
		grid.add(claySpinner,0,1);
		grid.add(woolSpinner,0,2);
		grid.add(cornSpinner,0,3);
		grid.add(oreSpinner,0,4);
		
	}
}
