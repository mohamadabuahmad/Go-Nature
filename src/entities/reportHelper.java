package entities;

import java.io.Serializable;
import java.sql.Date;

/**
 * this class is a helper class for  produce and manage reports
 * @author Group7
 *
 */
@SuppressWarnings("serial")
public class reportHelper implements Serializable{
	Date date;
	String range;
	
	/**
	 * constuctor
	 * @param date-the date of the report
	 * @param range-range of free times in the report
	 */
	public reportHelper(Date date, String range) {
		super();
		this.date = date;
		this.range = range;
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
	/**
	 * get the range 
	 * @return range 
	 */
	public String getRange() {
		return range;
	}
	/**
	 * sets a new value for  the range 
	 * @param range
	 */
	public void setRange(String range) {
		this.range = range;
	}

}
