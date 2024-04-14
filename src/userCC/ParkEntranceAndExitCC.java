package userCC;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Order;
/**
 * The ParkEntranceAndExitCC class provides functionality for managing park entrance and exit operations,
 * including identifying visitors at entrance and exit, and updating their arrival status.
 * This class handles interactions related to visitor identification, arrival, and exit at the park.
 * @author everyone
 */
public class ParkEntranceAndExitCC {
	/**
     * Sends a request to the server and retrieves the response.
     *
     * @param methodName The name of the method to be called on the server.
     * @param parameters The parameters to be sent to the server.
     * @return The response from the server.
     */
    private static Object sendRequest(String methodName, ArrayList<Object> parameters) {
        ClientMessage msg = new ClientMessage(methodName, parameters, parameters.size());
        ClientUI.chat.accept(msg);
        ServerMessage response = ChatClient.messageRecievedFromServerEvents.get(methodName);
        return response.getData();
    }
    /**
     * Identifies a visitor at the park entrance.
     *
     * @param identifyNumber The identification number of the visitor.
     * @param parkName       The name of the park.
     * @return True if the visitor is successfully identified, false otherwise.
     */
    public static boolean identifyAtEntrance(String identifyNumber, String parkName) {
        Order order = identify(identifyNumber, parkName);
        if (order == null) return false;
        
        updateArrivalStatus(order, true); 
        return true;
    }
    /**
     * Identifies a visitor at the park exit.
     *
     * @param identifyNumber The identification number of the visitor.
     * @param parkName       The name of the park.
     * @return True if the visitor is successfully identified, false otherwise.
     */
    public static boolean identifyAtExit(String identifyNumber, String parkName) {
        Order order = identify(identifyNumber, parkName);
        if (order == null) return false;
        
        order.setNumOfVisitors(-1 * order.getNumOfVisitors());
        updateArrivalStatus(order, false);
        return true;
    }
    /**
     * Identifies an order based on the provided identification number and park name.
     *
     * @param identifyNumber The identification number of the visitor.
     * @param parkName       The name of the park.
     * @return The identified order, or null if no order is found.
     */
   
    public static Order identify(String identifyNumber, String parkName) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(identifyNumber);
        parameters.add(Date.valueOf(LocalDate.now()));
        parameters.add(Time.valueOf(LocalTime.now()));
        parameters.add(parkName);
        return (Order) sendRequest("search_Order", parameters);
    }


    /**
     * Updates the arrival status of a visitor.
     *
     * @param order     The order associated with the visitor.
     * @param isEntrance True then updating entrance status, false then  exit status.
     */
    private static void updateArrivalStatus(Order order, boolean isEntrance) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(order);
        parameters.add(order.getOrderType());
        String methodName = isEntrance ? "updateStatus_forArrival" : "updateStatus_forExit";
        sendRequest(methodName, parameters);
    }
}
