/**
 * This is the class that executes our main method
 * Aside from loading the .fxml, nothing special happens here.
 * 
 * @author Joseph Natale, Peter Ling
 */
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.SongLibController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(
				getClass().getResource("/view/songlib.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
		
		SongLibController listController = 
				loader.getController();
		listController.start(primaryStage);
		
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SongLib");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}