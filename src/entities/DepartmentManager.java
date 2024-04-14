package entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *class DepartmentManager hold information about the DepartmentManager 
 *(String userName, String password, String firstName, String lastName, String workerId,String email)
 *also an ArrayList<Park> parks that the  DepartmentManager manage .
 * @author Group7 
 */
public class DepartmentManager extends User implements Serializable {

	ArrayList<Park> parks;
	/**
	 * gets the ArrayList parks 
	 * @return parks
	 */
	public ArrayList<Park> getParks() {
		return parks;
	}
	/**
	 * set a new ArrayList park 
	 * @param parks
	 */
	public void setParks(ArrayList<Park> parks) {
		this.parks = parks;
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param workerId
	 * @param email
	 */
	public DepartmentManager(String userName, String password, String firstName, String lastName, String workerId,String email) {
		super(userName, password, firstName, lastName, workerId, email,null);
	}

	

}
