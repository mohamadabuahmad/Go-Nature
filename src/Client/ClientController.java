// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Client;

import java.io.*;
import common.ChatIF;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Group 7 
 */
public class ClientController implements ChatIF {
	// Class variables *******

	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;

	// Instance variables *********

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;

	// Constructors ***************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientController(String host, int port) {
		try {
			client = new ChatClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	
	
	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 * @param str the input string.
	 */
	public void accept(Object str) {
		client.handleMessageFromClientUI(str);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}
	/**
     * Sends a quit message  disconnect this client .
     */
	public void quit()
	{
		client.quit();
	}
}
//End of ConsoleChat class
