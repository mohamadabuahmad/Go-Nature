package inviterCC;

import java.sql.Date;
/**
 * @author everyone
 */
public class inputValidator {

	/**
	 *  checks if the input id is valid �built from 9 digits
	 * 
	 * @param id
	 * @return true if id format is correct format,false otherwise
	 */
	public static boolean checkId(String id) {
		if (id.length() == 9)// checking length of id number,should be 9
		{
			for (int i = 0; i < id.length(); i++)
				if (!Character.isDigit(id.charAt(i)))// if one character is not a digit then id isn't valid return false
					return false;
			return true;
		}
		return false;
	}

	/**
	 *  checks if the input email is valid
	 * 
	 * @param email
	 * @return true if email is in correct format,false otherwise
	 */
	public static boolean checkEmail(String email) {
		String regex = "^[\\w-\\.+]*[\\w-\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	/**
	 *  checks if the string is built from the number, length 
	 * digits
	 * 
	 * @param length - wanted length of the string
	 * @param str    - String
	 * @return true if str is of length "length" and is all digits,false otherwise
	 */
	public static boolean checkAllDigitsOfSize(int length, String str) {
		System.out.println(str);
		if (str.length() == length) {
			String regex = "[0-9]*";
			return str.matches(regex);
		}
		return false;
	}

	/**
	 *  checks if the chars in the string are digits
	 * @param str-string
	 * @return true if its all digits,false otherwise
	 */
	public static boolean checkInputIfNumber(String str) {

		String regex = "[0-9]*";
		if (!str.equals(""))
			return str.matches(regex);

		return false;

	}
	/**
	 * creates Date.
	 * @param year
	 * @param month
	 * @param day
	 * @return Date
	 */
	@SuppressWarnings("deprecation")
	public static Date GetMatchDate(int year, int month, int day) {
		return new Date(year - 1900, month - 1, day);
	}

	
	/**
	 *  checks if the  string matches the given Time expression
	 * 
	 * @param inputTimeString
	 * @return true if the string matches else false
	 */
	public static boolean checkTime(String inputTimeString) {
		if (inputTimeString.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
			if (!inputTimeString.equals("00:00"))
				return true;
		}
		return false;
	}

	
	/**
	 *  checks if the String is built from letters
	 * 
	 * @param name
	 * @return true if the string matches else false
	 */
	public static boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

}