package entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

/**
 * class for holding Order information
 * @author Group7
 */

@SuppressWarnings("serial")
public class Order implements Serializable {
		
	private Park park;
	private Date visitDate;
	private Time visitTime;
	private int numOfVisitors;
	private String email;
	private String orderer;//the person who placed the order
	private OrderType orderType;
	private String orderStatus;
	private boolean payStatus;
	private double price;
	private boolean arrived;
	private String phoneNumber;
	private Time entrance;
	private Time exit;
	
	/**
	 * this Constructor have 8 parameters 
	 * @param Park park
	 * @param Date visitDate
	 * @param Time visitTime
	 * @param int numOfVisitors
	 * @param String email
	 * @param String orderer
	 * @param OrderType orderType
	 * @param double price
	 * @param String orderStatus
	 * @param boolean payStatus
	 */
	public Order(Park park, Date visitDate, Time visitTime, int numOfVisitors, String email, String orderer,OrderType orderType, double price,String orderStatus,boolean payStatus) {
		this.park = park;
		this.visitDate = visitDate;
		this.visitTime = visitTime;
		this.numOfVisitors = numOfVisitors;
		this.email = email;
		this.orderer = orderer;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.price = price;
		this.payStatus=payStatus;
	}
	/**
	 * this Constructor have 11 parameters
	 * @param Park park
	 * @param Date visitDate
	 * @param Time visitTime
	 * @param int numOfVisitors
	 * @param String email
	 * @param String orderer
	 * @param OrderType orderType
	 * @param double price
	 * @param String orderStatus
	 * @param boolean payStatus
	 * @param boolean arrived
	 */
	public Order(Park park, Date visitDate, Time visitTime, int numOfVisitors, String email, String orderer,OrderType orderType, double price,String orderStatus,boolean payStatus,boolean arrived) {
		this.park = park;
		this.visitDate = visitDate;
		this.visitTime = visitTime;
		this.numOfVisitors = numOfVisitors;
		this.email = email;
		this.orderer = orderer;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.price = price;
		this.payStatus=payStatus;
		this.arrived=arrived;
	}
	/**
	 * this Constructor have 12 parameters
	  * @param Park park
	 * @param Date visitDate
	 * @param Time visitTime
	 * @param int numOfVisitors
	 * @param String email
	 * @param String orderer
	 * @param OrderType orderType
	 * @param double price
	 * @param String orderStatus
	 * @param boolean payStatus
	 * @param boolean arrived
	 * @param String phoneNumber
	 */
	public Order(Park park, Date visitDate, Time visitTime, int numOfVisitors, String email, String orderer,OrderType orderType, double price,String orderStatus,boolean payStatus,boolean arrived,String phoneNumber) {
		this.park = park;
		this.visitDate = visitDate;
		this.visitTime = visitTime;
		this.numOfVisitors = numOfVisitors;
		this.email = email;
		this.orderer = orderer;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.price = price;
		this.payStatus=payStatus;
		this.arrived=arrived;
		this.phoneNumber=phoneNumber;
	}
	/**
	 * this Constructor have 14 parameters
	 * @param Park park
	 * @param Date visitDate
	 * @param Time visitTime
	 * @param int numOfVisitors
	 * @param String email
	 * @param String orderer
	 * @param OrderType orderType
	 * @param double price
	 * @param String orderStatus
	 * @param boolean payStatus
	 * @param boolean arrived
	 * @param String phoneNumber
	 * @param Time entrance
	 * @param Time exit
	 */
	public Order(Park park, Date visitDate, Time visitTime, int numOfVisitors, String email, String orderer,OrderType orderType, double price,String orderStatus,boolean payStatus,boolean arrived,String phoneNumber,Time entrance,Time exit) {
		this.park = park;
		this.visitDate = visitDate;
		this.visitTime = visitTime;
		this.numOfVisitors = numOfVisitors;
		this.email = email;
		this.orderer = orderer;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.price = price;
		this.payStatus=payStatus;
		this.arrived=arrived;
		this.phoneNumber=phoneNumber;
		this.entrance=entrance;
		this.exit=exit;
	}
	/**
	 * gets entrance
	 * @return entrance
	 */
	public Time getEntrance() {
		return entrance;
	}

	/**
	 * set new Time in entrance
	 * @param entrance
	 */
	public void setEntrance(Time entrance) {
		this.entrance = entrance;
	}

	/**
	 * gets the exit
	 * @return exit
	 */
	public Time getExit() {
		return exit;
	}

	/**
	 * set new Time in exit
	 * @param exit
	 */
	public void setExit(Time exit) {
		this.exit = exit;
	}

	/**
	 * gets the park
	 * @return park
	 */
	public Park getPark() {
		return park;
	}
	/**
	 * set new Park in park
	 * @param park
	 * 
	 */
	public void setPark(Park park) {
		this.park = park;
	}
	/**
	 * gets the visitDate
	 * @return visitDate
	 */
	public Date getVisitDate() {
		return visitDate;
	}
	/**
	 * set new Date in visitDate
	 * @param visitDate
	 */
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	/**
	 * gets the visitTime
	 * @return visitTime
	 */
	public Time getVisitTime() {
		return visitTime;
	}
	/**
	 * set new Time in visitTime
	 * @param visitTime
	 */
    public void setVisitTime(Time visitTime) {
		this.visitTime = visitTime;
	}
	/**
	 * gets the numOfVisitors
	 * @return numOfVisitors
	 */
	public int getNumOfVisitors() {
		return numOfVisitors;
	}
	/**
	 * set new int in numOfVisitors
	 * @param numOfVisitors
	 */
	public void setNumOfVisitors(int numOfVisitors) {
		this.numOfVisitors = numOfVisitors;
	}
	/**
	 * gets the email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * set new Sting in email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * gets the orderer
	 * @return orderer
	 */
	public String getOrderer() {
		return orderer;
	}
	/**
	 * set new Sting in orderer
	 * @param orderer
	 */
	public void setOrderer(String orderer) {
		this.orderer = orderer;
	}
	/**
	 * gets the orderType
	 * @return orderType
	 */
	public OrderType getOrderType() {
		return orderType;
	}
	/**
	 * set new OrderType in orderType
	 * @param orderType
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	/**
	 * gets the orderStatus
	 * @return orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}
	/**
	 * set new Sting in orderStatus
	 * @param orderStatus
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * gets the price
	 * @return price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * set new double in price
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * gets the payStatus
	 * @return payStatus
	 */
	public boolean getPayStatus() {
		return payStatus;
	}
	/**
	 * set new boolean in payStatus
	 * @param payStatus
	 */
	public void setPayStatus(boolean payStatus) {
		this.payStatus = payStatus;
	}
	
	/**
	 * gets the arrived
	 * @return arrived
	 */
	public boolean isArrived() {
		return arrived;
	}

	/**
	 * set new boolean in arrived
	 * @param arrived
	 */
	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}

	/**
	 * gets the phoneNumber
	 * @return phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * set new Sting in phoneNumber
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
