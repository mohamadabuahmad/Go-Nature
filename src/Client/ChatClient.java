package Client;

import ocsf.client.*;
import Protocol.ServerMessage;
import common.ChatIF;
import entities.Inviter;
import entities.User;

import java.io.*;
import java.util.HashMap;


/**
 *  It overrides some methods defined in the AbstractClient superclass to provide additional functionality.
 * This class handles communication between the client and server, as well as processing messages
 * received from the server.
 * 
 * This class also maintains a HashMap to store received server messages, and it tracks whether 
 * a response from the server is awaited.
 *
 * @author Group7 
 */
public class ChatClient extends AbstractClient {
	// Instance variables *****************
	public static HashMap<String, ServerMessage> messageRecievedFromServerEvents;
	public static User currentUser;
	public static Inviter currentInviter;

	
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public static ChatClient instance;

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		messageRecievedFromServerEvents = new HashMap<String, ServerMessage>();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");
		
		if (msg instanceof ServerMessage) {
			
			messageRecievedFromServerEvents.put(((ServerMessage) msg).getCommand(), (ServerMessage) msg);
		}
		awaitResponse = false;
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(Object message) {

		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;

			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}
	
	@Override
	protected void connectionClosed() {
		
	}
	

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
	}
}
