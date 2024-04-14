package goNatureBoundry;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * controller for the order invoice
 * @author Group7
 *
 */
public class Invoice implements Initializable {

    @FXML
    private Label date_label;

    @FXML
    private Label id_number_label;

    @FXML
    private Label data_about_order;
    
    private static String str_date,str_id,str_aboutorder;
    
    @Override  
	/**
	 * Initializes the Invoice GUI components with provided data.
	 * @param location    The location used to resolve relative paths for the root object.
	 * @param resources   The resources used to localize the root object.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		date_label.setText(str_date);
		id_number_label.setText(str_id);
		data_about_order.setText(str_aboutorder);
	}
 
    
    /**
     * Initializes and displays the invoice GUI with provided data.
     * @param primaryStage        The primary stage of the JavaFX application to be opened.
     * @param str_date          The string representation of the date of the invoice.
     * @param str_id     The ID of the orderer associated with the invoice.
     * @param str_aboutorder  The String representing the data of the invoice
     * 
     */
    public void start(Stage primaryStage,String str_date,String str_id,String str_aboutorder) {
    	this.str_date=str_date;
		this.str_id=str_id;
		this.str_aboutorder=str_aboutorder;
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Invoice.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setTitle("Invoice");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 

    
	
}