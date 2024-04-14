package inviterCC;

import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Order;
/**
 * The WaitingListCC class provides functionality for managing orders in the waiting list.
 * This class handles interactions related to adding orders to the waiting list, canceling orders from the waiting list,
 * confirming orders from the waiting list, and retrieving all orders for a specific traveler from the waiting list.
 * @author eyas
 */
public class WaitingListCC {

	/**
	 *  insert order to the waiting
	 * list
	 * 
	 * @param order - order to enter the waiting list
	 * @return true if its done , else false
	 */
	public static boolean enterWaitingList(Order order) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add(order);
		ClientMessage msg1 = new ClientMessage("add_ToWaitingList", arr, arr.size());
		ClientUI.chat.accept(msg1);
		ServerMessage msg2 = ChatClient.messageRecievedFromServerEvents.get(msg1.getMethodName());
		return (boolean) msg2.getData();
	}

	/**
	 *  delete order from waiting list
	 * @param order
	 * @return true if its done , else false
	 */
	public static boolean cancelOrderFromWaitingList(Order order) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add(order);
		ClientMessage msg1 = new ClientMessage("delete_FromWaitingList", arr, arr.size());
		ClientUI.chat.accept(msg1);
		ServerMessage msg2 = ChatClient.messageRecievedFromServerEvents.get(msg1.getMethodName());
		return (boolean) msg2.getData();
	}

	/**
      * confirm an order from the waiting list .
	 * @param order
	 * @return true if its done , else false
	 */
	public static boolean confirmOrderFromWaitingList(Order order) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add(order);
		ClientMessage msg1 = new ClientMessage("confirm_FromWaitingList", arr, arr.size());
		ClientUI.chat.accept(msg1);
		ServerMessage msg2 = ChatClient.messageRecievedFromServerEvents.get(msg1.getMethodName());
		return (boolean) msg2.getData();
	}

	/**
	 * 
    *get all the orders of the specific inviter
	 * 
	 * @param idNumber
	 * @return arraylist of orders that contains all the orders
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Order> getAllOrdersInWaitingListtoInviter(String idNumber) {

		ArrayList<Object> arr = new ArrayList<Object>();
		ArrayList<Order> arrOrders = new ArrayList<Order>();
		arr.add(idNumber);
		ClientMessage msg1 = new ClientMessage("getAllOrders_InWaitingList", arr, arr.size());
		ClientUI.chat.accept(msg1);
		ServerMessage msg2 = ChatClient.messageRecievedFromServerEvents.get(msg1.getMethodName());
		arrOrders = (ArrayList<Order>) msg2.getData();
		return arrOrders;
	}

}
