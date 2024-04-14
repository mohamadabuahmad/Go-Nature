package entities;

import java.io.Serializable;
import java.sql.Time;

/**
 * this is Park class that hold information about the park 
 * @author Group7
 */
public class Park implements Serializable {

	private String name;
	private String address;
	private Time stayTime;//// * Default is 4 hours *////
	private int currentNumOfUnplannedVisitors;
	private int maxNumOfVisitor;
	private int maxNumOfOrders;
	private int currentNumOfVisitors;
     /**
      * gets the currentNumOfVisitors
      * @return currentNumOfVisitors
      */
	public int getCurrentNumOfVisitors() {
		return currentNumOfVisitors;
	}
     /**
      * sets a  new  value currentNumOfVisitors
      * @param currentNumOfVisitors
      */
	public void setCurrentNumOfVisitors(int currentNumOfVisitors) {
		this.currentNumOfVisitors = currentNumOfVisitors;
	}
    /**
     * gets the currentNumOfUnplannedVisitors
     * @return  currentNumOfUnplannedVisitors
     */
	public int getCurrentNumOfUnplannedVisitors() {
		return currentNumOfUnplannedVisitors;
	}
     /**
      * sets a  new  value currentNumOfUnplannedVisitors
      * @param currentNumOfUnplannedVisitors
      */
	public void setCurrentNumOfUnplannedVisitors(int currentNumOfUnplannedVisitors) {
		this.currentNumOfUnplannedVisitors = currentNumOfUnplannedVisitors;
	}

	/**
	 * 
	 * @param String name
	 * @param String address
	 * @param Time stayTime
	 * @param int maxNumOfVisitor
	 * @param int maxNumOfdOrders
	 */
	public Park(String name, String address, Time stayTime, int maxNumOfVisitor, int maxNumOfdOrders) {

		this.name = name;
		this.address = address;
		this.stayTime = stayTime;
		this.maxNumOfVisitor = maxNumOfVisitor;
		this.maxNumOfOrders = maxNumOfdOrders;
	}

	/**
	 * gets the name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * gets the stayTime
	 * @return stayTime
	 */
	public Time getStayTime() {
		return stayTime;
	}

	/**
	 * set new Time in stayTime
	 * @param stayTime
	 */
	public void setStayTime(Time stayTime) {
		this.stayTime = stayTime;
	}

	/**
	 * gets the maxNumOfVisitor
	 * @return maxNumOfVisitor
	 */
	public int getMaxNumOfVisitor() {
		return maxNumOfVisitor;
	}

	/**
	 * set new intg in maxNumOfVisitor
	 * @param maxNumOfVisitor
	 */
	public void setMaxNumOfVisitor(int maxNumOfVisitor) {
		this.maxNumOfVisitor = maxNumOfVisitor;
	}

	/**
	 * gets the maxNumOdOrders
	 * @return maxNumOfOrders
	 */
	public int getMaxNumOfOrders() {
		return maxNumOfOrders;
	}

	/**
	 * set new int in maxNumOdOrders
	 * @param maxNumOdOrders
	 */
	public void setMaxNumOfOrders(int maxNumOdOrders) {
		this.maxNumOfOrders = maxNumOdOrders;
	}

	/**
	 * gets the address
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * sets new Sting in address
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}