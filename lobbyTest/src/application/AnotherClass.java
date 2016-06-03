package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class AnotherClass {
	TestController tc;
	public AnotherClass(TestController tc){
		this.tc = tc;
	}
	public void init(){
		tc.handleTOne();
	}
}
