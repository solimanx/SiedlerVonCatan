package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("Test.fxml").openStream());
		Scene scene = new Scene(root);



		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
		TestController controller = (TestController) loader.getController();

		AnotherClass ac = new AnotherClass(controller);
		ac.init();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
