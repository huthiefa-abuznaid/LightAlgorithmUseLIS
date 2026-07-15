package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	
	public void start(Stage primaryStage) {

		InputView inputView = new InputView(primaryStage);
		inputView.show();
	}

	

	public static void main(String[] args) {
		launch(args);
	}
 
}