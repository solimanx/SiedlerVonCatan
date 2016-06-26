package tradeview;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TradeViewController {

    @FXML
    private Button placeOfferButton;

    @FXML
    private ListView<?> offerList;

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
	private Integer[][] result = new Integer[2][5];

	@FXML
	private GridPane grid;

	/**
	 * Initializes the Trade window with spinners
	 * @param resources
	 */
	public void init(int[] resources) {
		giveWoodSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[0], 0));
		giveClaySpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[1], 0));
		giveWoolSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[3], 0));
		giveCornSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[4], 0));
		giveOreSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resources[2], 0));
		getWoodSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
		getClaySpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
		getWoolSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
		getCornSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
		getOreSpinner = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));

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
	}
	
    @FXML
    void handlePlaceOfferButton(ActionEvent event) {

    }

    @FXML
    void handleTradeButton(ActionEvent event) {
    	Stage stage = (Stage) tradeButton.getScene().getWindow();
    	stage.close();
    }
	
}
