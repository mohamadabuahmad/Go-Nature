package entities;

import java.io.Serializable;
/**
 * this is ParameterType class that hold information about the parameter type
 * @author  Group7 
 *
 */
public enum ParameterType implements Serializable {

	MAXNUMBEROFVISITORS, MAXNUMBEROFORDER, STAYTIME;
 
	/**
	 * 
	 * @param p
	 * @return  string based on the p 
	 */
	public static String getType(ParameterType p) {
		switch (p) {
		case MAXNUMBEROFORDER:
			return "max number of order";
		case MAXNUMBEROFVISITORS:
			return "Max numberof visitors";
		case STAYTIME:
			return "Stay Time";
		
		default:
			break;
		}
		return "";
	}

}
