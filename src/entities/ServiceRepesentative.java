package entities;

import java.io.Serializable;

/**
 * class holds all the  necessary data for service representative
 * @author Group7
 */
@SuppressWarnings("serial")
public class ServiceRepesentative extends User implements Serializable {

		
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param workerId
	 * @param email
	 */
	public ServiceRepesentative(String userName, String password, String firstName, String lastName, String workerId,
			String email) {
		super(userName, password, firstName, lastName, workerId, email,null);
	}

}
