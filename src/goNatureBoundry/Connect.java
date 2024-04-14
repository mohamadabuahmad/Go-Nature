package goNatureBoundry;

import Client.ClientController;
import Client.ClientUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *this class is the home page of the client side 
 * @author mohamad
 *
 */
public class Connect {

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;
    /**
     * Handles the event of clicking the connect button.
     * Retrieves the IP address and port number from the text fields, initializes a ClientController
     * with these parameters,and displays the home page.
     */
    @FXML
    private void handleConnectButton() {
        String ip = ipTextField.getText();
        int port = Integer.parseInt(portTextField.getText());

        try {
            ClientUI.chat = new ClientController(ip, port);

            // show the home page
            Stage stage = (Stage) ipTextField.getScene().getWindow();
            GoNature homePage = new GoNature();
            homePage.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
