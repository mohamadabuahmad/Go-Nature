package entities;


import java.io.Serializable;

/**
 * class that holds all necessary information for worker
 * @author Group 7  
 */
@SuppressWarnings("serial")
public class User implements Serializable {


	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String workerId;
	private String email;	
    private Park park;
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param workerId
	 * @param email
	 * @param position
	 */
	public User (String userName,String password,String firstName,String lastName,String workerId,String email,Park park) {
		this.userName=userName;
		this.password=password;
		this.firstName=firstName;
		this.lastName=lastName;
		this.workerId=workerId;
		this.email=email;
		this.park=park;
	}	
	/**
	 * @return the userName
	 * 	get the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName
	 *  set new value for the  userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}	
	/** 
	 * @return the password
	 * get the password
	 */
	public String getPassword() {
		return password;
	}
	/** 
	 * @param password
	 *  set new value for the   password
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
	/**
	 * @return the firstName
	 * get the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName
	 * set new value for the firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}	
	/** 
	 * @return the lastName
	 * get the lastName
	 */
	public String getLastName() {
		return lastName;
	}	
	/**
	 * @param lastName
	 *  set new value for the   lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	/**
	 * @return the workerId
	 * get the workerId
	 */
	public String getWorkerId() {
		return workerId;
	}	
	/** 
	 * @param workerId
	 * set new value for the   workerId
	 */
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	/**
	 * @return the email
	 * get the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email
	 *  set new value for the   email
	 */
	public void setEmail(String email) {
		this.email = email;
	}  	
	
	/**
	 * get the park 
	 * @return park 
	 */
	public Park getPark() {
		return park;
	}
	/**
	 * sets a new value for  the park 
	 * @param park
	 */
	public void setPark(Park park) {
		this.park = park;
	}
	/**
	 * prints the data 
	 */
	@Override
	public String toString() {
		return "Worker [userName=" + userName + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", workerId=" + workerId + ", email=" + email + "]";
	}
	
    /**
     * 
     * @param otherWorker
     * @return true if the workers are equal and false otherwise
     */
	@Override
    public boolean equals(Object otherWorker) {
    	
    	if(otherWorker instanceof User) {
    		
    	if(userName.equals(((User) otherWorker).getUserName()))
    		if(password.equals(((User) otherWorker).getPassword())) 
    			if(firstName.equals(((User) otherWorker).getFirstName())) 
    				if(lastName.equals(((User) otherWorker).getLastName())) 
    					if(workerId.equals(((User) otherWorker).getWorkerId())) 
    						if(email.equals(((User) otherWorker).getEmail())) 
    							return true;
    	}   	   	
        return false;
    }
	

}
