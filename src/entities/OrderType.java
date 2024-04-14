package entities;

import java.io.Serializable;

/**
 * this is OrderType class that hold information about the order type
 * 
 * @author Group7
 */
public enum OrderType implements Serializable {
	PLANNEDTRAVELER {
		public String toString() {
			return "PLANNEDTRAVELER";
		}

	}, /* an order that has been placed using the application for an inviter  */
	PLANNEDGUIDE{
		public String toString() {
			return "PLANNEDGUIDE";
		}
	}, /* an order that has been placed using the application for a group guide */
	UNPLANNEDTRAVELER{
		public String toString() {
			return "UNPLANNEDTRAVELER";
		}
	
	}, /* an order that has been placed at the park for an inviter */
	UNPLANNEDGUIDE{
		public String toString() {
			return "UNPLANNEDGUIDE";
		}
	}/* an order that has been placed at the parkfor a group guide */
}
