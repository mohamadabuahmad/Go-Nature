package entities;

import java.io.Serializable;
/**
 * class Connection hold information about the user 'ip address , host name, role'
 * Connection class take (String ipAddress, String hostName, String role)
 * @author Group7
 * 
 */
@SuppressWarnings("serial")
public class Connection implements Serializable {
	
	private String ipAddress;
	private String hostName;
	private String role;
	private String idNumber;
	/**
	 * 
	 * @param ipAddress
	 * @param hostName
	 * @param role
	 * @param idNumber
	 */
	public Connection(String ipAddress, String hostName, String role,String idNumber) {
		super();
		this.ipAddress = ipAddress;
		this.hostName = hostName;
		this.role = role;
		this.idNumber=idNumber;
	}
	/**
	 * gets the idNumber
	 * @return idNumber 
	 */
	public String getIdNumber() {
		return idNumber;
	}
	/**
	 * sets the idNumber field using parameter
	 * @param idNumber
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * gets the ipAddress
	 * @return ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * set new String in ipAddress
	 * @param ipAddress
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	/**
	 * gets the hostName
	 * @return hostName
	 */
	public String getHostName() {
		return hostName;
	}
	/**
	 * set new String in hostName
	 * @param hostName
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	/**
	 * gets the role
	 * @return role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * set new String in role
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}


}
