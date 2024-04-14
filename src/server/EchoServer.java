package server;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Connection;
import entities.Order;
import ocsf.server.*;
import serverboundry.Server;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Group7
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************
	final public static int DEFAULT_PORT = 5555;// The default port to listen on
	public static ArrayList<Connection> clientTypes=new ArrayList<Connection>();
	public static HashMap<ConnectionToClient, Connection> clientsMap=new HashMap<ConnectionToClient, Connection>();
	public static Server serverController;

	// Constructors ****************************************************
	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
			mysqlConnection.connecttoDB();
		
	}
	// Instance methods ************************************************
	 /**
     * This method handles messages received from clients and performs appropriate actions
     * based on the message content.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
	  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	        if (msg instanceof ClientMessage) {
	            ClientMessage clientMessage = (ClientMessage) msg;
	            String methodName = clientMessage.getMethodName();
	            System.out.println("Message received: " + methodName + " from " + client);
	            
	            if (methodName.isEmpty()) {
	                handleEmptyMethodName(clientMessage, client);
	               
	            } else if(((ClientMessage) msg).getMethodName().equals("LOGOUT")){
	            	System.out.println("Echo Server LOGOUT ;");
					clientDisconnected(client);
	            }
	            else
	            {
	            	 System.out.println("anooooooooos");
	                invokeMethodBasedOnMessage(clientMessage, client);
	            }
	        }
	    }
	  /**
	   * Handles the case where the received client message has an empty method name.
	   * This method typically deals with the initial connection setup from a client.
	   *
	   * @param msg    The client message with an empty method name.
	   * @param client The connection from which the message originated.
	   */
	    private void handleEmptyMethodName(ClientMessage msg, ConnectionToClient client) {
	        ServerMessage sr = new ServerMessage(msg.getMethodName(), true);
	        Connection connectionParameter = (Connection) msg.getParameters().get(0);

	        if (!clientsMap.containsValue(connectionParameter)) {
	            clientTypes.add(connectionParameter);
	            serverController.addConnection();
	            clientsMap.put(client, connectionParameter);
	        } else {
	            sr.setData(false);
	        }

	        sendServerMessageToClient(sr, client);
	        serverController.refresh();
	    }
	    /**
	     * Invokes a method based on the received client message and sends the result back
	     * to the client.
	     *
	     * @param msg    The client message containing the method name and parameters.
	     * @param client The connection from which the message originated.
	     */
	    private void invokeMethodBasedOnMessage(ClientMessage msg, ConnectionToClient client) {
	        try {
	            Method method = determineMethodToInvoke(msg);
	            Object result = invokeMethod(method, msg);
	            ServerMessage sr = new ServerMessage(msg.getMethodName(), result);
	            
	            handlePostMethodInvocation(sr, msg);
	            sendServerMessageToClient(sr, client);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    /**
	     * Determines the method to invoke based on the received client message.
	     *
	     * @param msg The client message containing the method name and parameters.
	     * @return The method to invoke.
	     * @throws NoSuchMethodException If the method specified in the client message does not exist.
	     */
	    private Method determineMethodToInvoke(ClientMessage msg) throws NoSuchMethodException {
	        return msg.getParameters() .isEmpty() ?
	                mysqlConnection.class.getMethod(msg.getMethodName()) :
	                mysqlConnection.class.getMethod(msg.getMethodName(), msg.getParameters().getClass());
	    }
	    /**
	     * Invokes the specified method with the provided parameters.
	     *
	     * @param method The method to invoke.
	     * @param msg    The client message containing the parameters for the method invocation.
	     * @return The result of the method invocation.
	     * @throws IllegalAccessException    If the method cannot be accessed due to Java language access control.
	     * @throws InvocationTargetException If the underlying method throws an exception.
	     */
	    private Object invokeMethod(Method method, ClientMessage msg) throws IllegalAccessException, InvocationTargetException {
	        return msg.getParameters() .isEmpty() ?
	                method.invoke(null) :
	                method.invoke(null, msg.getParameters());
	    }
	    /**
	     * This method is typically used to perform additional operations after invoking a method.
	     *
	     * @param sr  The server message resulting from the method invocation.
	     * @param msg The client message containing parameters for the method invocation.
	     */
	    private void handlePostMethodInvocation(ServerMessage sr, ClientMessage msg) {
	        if ("addOrder".equals(sr.getCommand()) && sr.getData() != null) {
	            boolean res = (boolean) sr.getData();
	            if (res) {
	                Order order = (Order) msg.getParameters().get(0);
	                serverController.writeToConsole("[sending sms to: " + order.getPhoneNumber() + "  sending email to: " + order.getEmail() + "  message: order added successfully, we are waiting for you on: " + order.getVisitDate() + ", " + order.getVisitTime() + "]");
	            }
	        }
	    }
	    /**
	     * Sends a server message to the specified client.
	     *
	     * @param sr     The server message to send.
	     * @param client The client connection to send the message to.
	     */
	    private void sendServerMessageToClient(ServerMessage sr, ConnectionToClient client) {
	        try {
	            client.sendToClient(sr);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    


	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
	
	
	/**
	 * Checks for disconnected clients and handles them appropriately.
	 * 
	 * @return The number of disconnected clients found.
	 */
	public int checkDisconnectedClients() {
		ArrayList<ConnectionToClient> connections=new ArrayList<ConnectionToClient>(clientsMap.keySet());
		int cnt=0;
		for(ConnectionToClient c:connections)
		{
			synchronized (c) {
				if(c!=null)
				{
					if(c.toString()==null)
					{
					clientDisconnected(c);
					cnt++;
					}
				}
			}	
		}
		return cnt;
	}
	/**
	 * Handles disconnection of a client.
	 * 
	 * @param client The client connection that has been disconnected.
	 */
	protected  void	clientDisconnected(ConnectionToClient client)
	{
		ServerMessage logoutConfirmation = new ServerMessage("LOGOUT_CONFIRMATION", true);
		try {
			client.sendToClient(logoutConfirmation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Connection connection=clientsMap.remove(client);
		mysqlConnection.updateUserConnectionToDisconnected(connection.getIdNumber());
		clientTypes.remove(connection);
		serverController.refresh();
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class
