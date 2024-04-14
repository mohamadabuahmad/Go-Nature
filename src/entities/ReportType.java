package entities;

/**
 * 
 * this is an enum class for the report types
 *@author Group7 
 */
public enum ReportType {

	cancelled_order_report{
		public String toString() {
			return "Cancelled Order Report";
		}
	}, number_of_visitors_report{
		public String toString() {
			return "Number Of visitors Report";
		}
	}, Usags_Report{
		public String toString() {
			return "Usags Report";
		}
	}, Visits_Report{
		public String toString() {
			return "Visits Report";
		}
	};

	
}
