package Protocol;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *This class used to convert the  message from  the client to a common format 
 * @author Group7
 *
 */
public class ClientMessage implements Serializable {
	private ArrayList<Object> parameters;
	private String methodName;
	private int numParameters;

	/**
	 * constructor 
	 * @param methodName-the name of method to be called on mysqlconnection class
	 * @param parameters-an arraylist<object> that has all parameter that the mysqlconnection method needs
	 * @param numParameters-the number of this  parameters
	 */
	public ClientMessage( String methodName,ArrayList<Object> parameters, int numParameters) {
		this.parameters = parameters;
		this.methodName = methodName;
		this.numParameters = numParameters;
	}
	/**
	 * get the methodName
	 * @return methodName 
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * sets a new value for  the methodName 
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * get the numParameters
	 * @return numParameters 
	 */
	public int getNumParameters() {
		return numParameters;
	}

	/**
	 * sets a new value for  the numParameters 
	 * @param numParameters
	 */
	public void setNumParameters(int numParameters) {
		this.numParameters = numParameters;
	}
	/**
	 * get the ArrayList<Object> parameter
	 * @return parameter 
	 */
	public ArrayList<Object> getParameters() {
		return parameters;
	}

	/**
	 * sets a new value for the  parameter 
	 * @param parameters
	 */
	public void setParameters(ArrayList<Object> parameters) {
		this.parameters = parameters;
	}



}