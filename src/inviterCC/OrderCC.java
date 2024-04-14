package inviterCC;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Order;
import entities.Park;
/**
 * The OrderCC class provides functionality for managing orders, including checking availability,
 * adding, confirming, and canceling orders, as well as fetching available order dates.
 * This class handles interactions related to orders and parks.
 * @author anood
 */
public class OrderCC {

	/**
     * Sends a message to the server and retrieves the response.
     *
     * @param methodName The name of the method to be called on the server.
     * @param parameters The parameters to be sent to the server.
     * @return The response from the server.
     */
    private static ServerMessage sendMessageToServer(String methodName, ArrayList<Object> parameters) {
        ClientMessage msg = new ClientMessage(methodName, parameters, parameters.size());
        ClientUI.chat.accept(msg);
        return ChatClient.messageRecievedFromServerEvents.get(methodName);
    }
    /**
     * Checks the availability of an order at a specific time and date for a given park.
     *
     * @param time         The visit time.
     * @param date         The visit date.
     * @param numOfVisitors The number of visitors.
     * @param parkName     The name of the park.
     * @return True it is availibile, false otherwise.
     */
    @SuppressWarnings("deprecation")
	public static boolean checkOrderAvailibilty(Time time, Date date, int numOfVisitors, String parkName) {
        if (time.compareTo(new Time(19, 0, 0)) < 0 && time.compareTo(new Time(8, 0, 0)) > 0) {
            ArrayList<Object> parameters = new ArrayList<>();
            parameters.add(time);
            parameters.add(date);
            parameters.add(numOfVisitors);
            parameters.add(parkName);
            ServerMessage response = sendMessageToServer("check_Availibility", parameters); 
            System.out.println("check:"+response);
            return (boolean) response.getData();
        }
        return false;
    }
    /**
     * Adds an order to the database.
     *
     * @param order The order to be added.
     * @return True if the order is added successfully, false otherwise.
     */
    public static boolean addOrder(Order order) {
    	LocalDate localDate = order.getVisitDate().toLocalDate();
	    LocalDate nextDay = localDate.plusDays(1);
	    java.sql.Date sqlNextDay = java.sql.Date.valueOf(nextDay);
        if (order.getOrderType().toString().startsWith("UNPLANNED") || 
            checkOrderAvailibilty(order.getVisitTime(), sqlNextDay, order.getNumOfVisitors(), order.getPark().getName())) {
            ArrayList<Object> parameters = new ArrayList<>();
            parameters.add(order);
            ServerMessage response = sendMessageToServer("add_Order", parameters);
            
            return (boolean) response.getData();
        }
        return false;
    }
    /**
     * Confirms an existing order.
     *
     * @param order The order to be confirmed.
     * @return True if the order is confirmed successfully, false otherwise.
     */
    public static boolean confirmOrder(Order order) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(order);
        parameters.add("confirmed");
        return updateOrderStatus(parameters);
    }
    /**
     * Cancels an existing order.
     *
     * @param order The order to be canceled.
     * @return True if the order is canceled successfully, false otherwise.
     */
    public static boolean cancelOrder(Order order) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(order);
        parameters.add("unconfirmed");
        return updateOrderStatus(parameters);
    }

 
    /**
     * Updates the status of an order.
     * @param parameters The parameters for updating the order status.
     * @return True if the order status is updated successfully, false otherwise.
     */
    public static boolean updateOrderStatus(ArrayList<Object> parameters) {
        ServerMessage response = sendMessageToServer("update_StatusforOrder", parameters);
        return (boolean) response.getData();
    }
    /**
     * Fetches available order dates for a given park and visitor count.
     *
     * @param time         The desired visit time.
     * @param date         The initial visit date.
     * @param numOfVisitors The number of visitors.
     * @param parkName     The name of the park.
     * @return An ArrayList of available Timestamps representing available order dates.
     */
    @SuppressWarnings("deprecation")
	public static ArrayList<Timestamp> fetchAvailibleOrderDates(Time time, Date date, int numOfVisitors, String parkName) {
        ArrayList<Timestamp> availableDates = new ArrayList<>();
        for (int hour = 8; hour <= 19; hour++) {
            Time checkTime = new Time(time.getTime());
            checkTime.setHours(hour);
            
            if (checkOrderAvailibilty(checkTime, date, numOfVisitors, parkName)) {
                Timestamp timestamp = new Timestamp(date.getYear(), date.getMonth(), date.getDate(), hour, 0, 0, 0);
                availableDates.add(timestamp);
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (int i = 1; i <= 3; i++) { 
            cal.add(Calendar.DATE, 1); 
            Date nextDate = new Date(cal.getTimeInMillis());
            
            if (checkOrderAvailibilty(time, nextDate, numOfVisitors, parkName)) {
                Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
                timestamp.setHours(time.getHours());
                timestamp.setMinutes(time.getMinutes());
                timestamp.setSeconds(0);
                timestamp.setNanos(0);
                availableDates.add(timestamp);
            }
        }
        
        return availableDates;
    }
    /**
     * gets all orders associated with an inviter.
     *
     * @param idNumber The ID number of the inviter.
     * @return An ArrayList of orders associated with the inviter.
     */
    @SuppressWarnings("unchecked")
	public static ArrayList<Order> getInviterOrders(String idNumber) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(idNumber);
        ServerMessage response = sendMessageToServer("get_AllOrders", parameters);
        return (ArrayList<Order>) response.getData();
    }
    /**
     * gets all parks .
     *
     * @return An ArrayList of all parks .
     */
    @SuppressWarnings("unchecked")
	public static ArrayList<Park> getAllParksforClient() {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(null); 
        ServerMessage response = sendMessageToServer("get_AllParks", parameters);
        return (ArrayList<Park>) response.getData();
    }

}
