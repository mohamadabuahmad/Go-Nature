package entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

/**
 * this class holds information about the WaitingList
 *  @author Group7
 */
@SuppressWarnings("serial")
public class WaitingList implements Serializable {

	private ArrayList<Order> waitingList = new ArrayList<Order>();
	private Park park;
	private Date date;
    /**
     * get the ArrayList<Order> waitingList
     * @return waitingList
     */
	public ArrayList<Order> getWaitingList() {
		return waitingList;
	}
    /**
     * get the park 
     * @return park
     */
	public Park getPark() {
		return park;
	}
     /**
      * set a new value of the park 
      * @param park
      */
	public void setPark(Park park) {
		this.park = park;
	}
    /**
     * get the date
     * @return date
     */
	public Date getDate() {
		return date;
	}
    /**
     * set a new value of date
     * @param date
     */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * adding unconfirmed order to the WaitingList
	 * @param unconfirmedOrder
	 * @return true or false depened on the procces of the add 
	 */
	public boolean addToWaitingList(Order unconfirmedOrder) {
			return waitingList.add(unconfirmedOrder);
		
	}
	/**
	 * removes order from the WaitingList
	 * @param orderToRemove
	 * @return true or false depened on the procces of the remove 
	 */
	public boolean removeFromWaitingList(Order orderToRemove) {
		return waitingList.remove(orderToRemove);
	}
	
	
	
}
