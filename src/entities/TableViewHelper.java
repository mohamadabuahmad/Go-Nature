package entities;

import java.sql.Date;

/**
 * this class is for adding for  TableView
 *  @author Group7
 *
 */
public class TableViewHelper {
	int id;
	String name, status;
	Date date, endDate, startDate;
	String parkName;
	String classType;
	Object oldValue, newValue;

	/**
	 * 
	 * @param id
	 * @param nameString
	 * @param status
	 * @param date
	 */
	public TableViewHelper(int id, String nameString, String status, Date date) {
		super();
		this.id = id;
		this.name = nameString;
		this.status = status;
		this.date = date;
	}
	/**
	 * 
	 * @param id 
	 * @param name 
	 * @param status 
	 * @param date 
	 * @param parkName 
	 */
	public TableViewHelper(int id, String name, String status, Date date, String parkName) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.date = date;
		this.parkName = parkName;
	}

	/**
	 * get the endDate
	 * @return endDate 
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * sets a new value for  the endDate 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * get the startDate
	 * @return startDate 
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * sets  a new value for the startDate 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * get the oldValue
	 * @return oldValue 
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * sets  a new value for the oldValue 
	 * @param oldValue
	 */
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	/**
	 * get the newValue
	 * @return newValue 
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * sets a new value for  the newValue 
	 * @param newValue
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * get the classType
	 * @return classType 
	 */
	public String getClassType() {
		return classType;
	}
	/**
	 * sets a new value for  the classType 
	 * @param classType
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}
	/**
	 * get the parkName
	 * @return parkName 
	 */
	public String getParkName() {
		return parkName;
	}

	/**
	 * sets a new value for  the parkName 
	 * @param parkName
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	/**
	 * get the id 
	 * @return id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets a new value for  the id 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * get the name 
	 * @return name 
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets  a new value for the name 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * get the status
	 * @return status 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * sets a new value for  the status 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * get the date 
	 * @return date 
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * sets a new value for the  date 
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
