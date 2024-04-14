package server;
import javafx.application.Application;
import javafx.stage.Stage;
import serverboundry.Server;

import java.io.IOException;
/**
 * It provides functionality to start and stop the server, as well as methods
 * to manage server operations.
 * This class extends javafx.application.Application and implements the start() method to
 * initialize the server's GUI.
 * @author Group7
 */
public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	public static EchoServer sv;

	/**
	 * The main method of the ServerUI class. It launches the JavaFX application by calling
     * launch(args).
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} 

	/**
	 * Initializes and starts the server user interface.
	 * @param primaryStage The primary stage for the JavaFX application.
     * @throws Exception If an error occurs during the initialization of the server GUI.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		Server server = new Server();

		server.start(primaryStage);

	}

	/**
	 * starts the server
	 * @param p- the port to run server on
	 * @return true if server started successfully,false otherwise
	 */
	public static boolean runServer(String p) {
		int port = 0; // Port to listen on 

		try {
			port = Integer.parseInt(p); // Set port to 5555
			
		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
		return true;
	}
	
	/**
	 * closes the server
	 * @return true if server closed successfully,false otherwise
	 */
	public static boolean closeServer()
	{
		try {
			if(sv!=null)
				sv.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Invoked when the JavaFX application is stopped. It closes the server and exits the
     * application.
	 */
	@Override
	public void stop()
	{
		closeServer();
		System.exit(0);
	}

}
