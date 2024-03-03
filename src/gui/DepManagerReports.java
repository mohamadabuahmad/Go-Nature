package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class DepManagerReports {
	
	
	
    @FXML
    public void BackBtn(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/DepManager.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
          	window.setTitle("Department Manager");
        	window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML
    public void VisitsRep(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/VisitsRep.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
        	window.setTitle("Visits Reports");
        	window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void CancelRep(ActionEvent event) {
        try {
        	Parent homePageRoot = FXMLLoader.load(getClass().getResource("/gui/CancelRep.fxml"));
        	Scene homePageScene = new Scene(homePageRoot);
        	Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	window.setScene(homePageScene);
        	window.setTitle("Cancel Reports");
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
