package application;

import java.io.IOException;

import enums.HarbourStatus;
import enums.ResourceType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/application/GameView.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			GameViewController controller = (GameViewController) loader.getController();
			primaryStage.show();
			controller.startScene(primaryStage);
			
			//testing
			controller.setField(2, 0, ResourceType.CLAY, 10);
			controller.setHarbour(1, 2, HarbourStatus.CORN);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
