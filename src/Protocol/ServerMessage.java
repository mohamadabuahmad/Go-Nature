package Protocol;

import java.io.Serializable;

/**
 * This class used to convert the  message from  the server to a common format 
 * @author Group7
 *
 */
public class ServerMessage implements Serializable {

	private String command;
	private Object data;

	/**
	 * constructor 
	 * @param command-the method name in mysqlconnection that we are returning the data from
	 * @param data-the data to be returned from the method 
	 */
	public ServerMessage(String command, Object data) {
		this.command = command;
		this.data = data;
	}
	/**
	 * get the command
	 * @return command 
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * sets a new value for  the command 
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * get the data
	 * @return data 
	 */
	public Object getData() {
		return data;
	}

	/**
	 * sets a new value for  the data 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * prints the data 
	 */
	public String toString() {
		return "SRMessage [command=" + command.toString() + ", data=" + data.toString() + "]";
	}

}
