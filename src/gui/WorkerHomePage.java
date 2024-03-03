package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class WorkerHomePage {
	
	
	
    @FXML
    public void BackBtn(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	String css = this.getClass().getResource("/gui/HomePage.css").toExternalForm();
        	homePageScene.getStylesheets().add(css);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
        	window.setWidth(400); 
        	window.setHeight(400); 
        	window.setTitle("Home Page");
        	window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML
    public void AddOrder(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	String css = this.getClass().getResource("/gui/HomePage.css").toExternalForm();
        	homePageScene.getStylesheets().add(css);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
        	window.setWidth(400); 
        	window.setHeight(400); 
        	window.setTitle("Home Page");
        	window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void CheckAvailability(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	String css = this.getClass().getResource("/gui/HomePage.css").toExternalForm();
        	homePageScene.getStylesheets().add(css);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
        	window.setWidth(400); 
        	window.setHeight(400); 
        	window.setTitle("Home Page");
        	window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void CheckOrder(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/HomePage.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	String css = this.getClass().getResource("/gui/HomePage.css").toExternalForm();
        	homePageScene.getStylesheets().add(css);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
        	window.setWidth(400); 
        	window.setHeight(400); 
        	window.setTitle("Home Page");
        	window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void start(Stage primaryStage) throws Exception {	
	

		Parent root = FXMLLoader.load(getClass().getResource("/gui/WorkerHomePage.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Worker Page");
		primaryStage.show();	 	   
	}

}
