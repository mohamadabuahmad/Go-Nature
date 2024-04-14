package entities;

import java.io.Serializable;
import java.sql.Date;

/**
 * this is ParameterChangeRequest class that hold information about parameter change request
 * @author Group7 
 * 
 */
public class ParameterChangeRequest implements Serializable {

	private ParameterType type;
	private Object newValue;
	private Park park;
	private String status;
	private Date sendDate;
	/**
	 * this Constructor ParameterChangeRequest with specific type , newvalue and
	 * oldvalue and park and sender
	 * 
	 * @param type
	 * @param oldValue
	 * @param newValueString
	 * @param park
	 * @param sender
	 */
	public ParameterChangeRequest(ParameterType type,  Object newValue, Park park, Date date,
			String status) {
		
		this.type = type;
		this.newValue = newValue;
		this.park = park;
		this.status=status;
		this.sendDate=date;
	}
     /**
      * gets the  status  
      * @return status 
      */
	public String getStatus() {
		return status;
	}
    /**
     * sets a new value of the status 
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
	}
     /**
      * gets the sendDate 
      * @return sendDate
      */
	public Date getSendDate() {
		return sendDate;
	}
     /**
      * sets a new value for the sendDate 
      * @param sendDate
      */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
    /**
     * gets the type 
     * @return type 
     */
	public ParameterType getType() {
		return type;
	}
    /**
     * sets a new value of the type 
     * @param type
     */
	public void setType(ParameterType type) {
		this.type = type;
	}
    /**
     * gets the newValue
     * @return newValue
     */
	public Object getNewValue() {
		return newValue;
	}
    /**
     * sets a new value for the newValue
     * @param newValue
     */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
     /**
      * gets the park 
      * @return park
      */
	public Park getPark() {
		return park;
	}
    /**
     * sets a new value of the park 
     * @param park
     */
	public void setPark(Park park) {
		this.park = park;
	}

}