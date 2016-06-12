package application;

import java.io.IOException;

import enums.HarbourStatus;
import enums.ResourceType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import tradeview.TradeViewController;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/tradeview/tradeView.fxml").openStream());
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(false);
			TradeViewController controller = (TradeViewController) loader.getController();
			primaryStage.show();
			int[] a = {4,2,1,4,5};
			controller.init(a);
//			controller.init(a);
//			controller.createSpinner(a);
			//controller.startScene(primaryStage);
			
			//testing
			//controller.setField(2, 0, ResourceType.CLAY, 10);
			//controller.setHarbour(1, 2, HarbourStatus.CORN);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
