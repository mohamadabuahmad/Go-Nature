package userCC;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.ParameterChangeRequest;
import entities.ReportType;
/**
 * The DepartmentMangerCC class provides functionality for department managers to interact with reports
 * and parameter change requests.
 * This class contains methods for creating and retrieving cancellation and visit reports,
 * confirming or unconfirming parameter change requests, and updating parameter values.
 * This class utilizes the ReportsController class for managing reports in the database.
 * @author mohamad
 */
public class DepartmentMangerCC {

    public static List<Integer> cancelReport;
    public static List<Integer> visitReport;

    /**
     * Creates or retrieves a cancellation report.
     *
     * @param date     The report date.
     * @param parkName The park name.
     */
   
	@SuppressWarnings("unchecked")
	public static void createCancelReport(Date date, String parkName) {
        List<String> result = new ArrayList<>();
        ReportType reportType = ReportType.cancelled_order_report;

        if (!ReportsController.CheckReportExistInDB(reportType, date, parkName)) {
            ArrayList<Object> msg = new ArrayList<>(Arrays.asList(reportType.toString(), date, parkName));
            ClientMessage msg2 = new ClientMessage("create_CancelReport", msg, 2);
            ClientUI.chat.accept(msg2);

            ServerMessage srMessage = ChatClient.messageRecievedFromServerEvents.get(msg2.getMethodName());
            cancelReport = (ArrayList<Integer>) srMessage.getData();
            result.add(String.valueOf(cancelReport.get(0)));
            result.add(String.valueOf(cancelReport.get(1)));
            
            ReportsController.addReportToDB(reportType, date, result, parkName);
        } else {
            displayReportFromDB(date, parkName, reportType);
        }
    }

    /**
     * Creates or retrieves a visit report.
     *
     * @param date     The report date.
     * @param parkName The park name.
     */
    @SuppressWarnings("unchecked")
	public static void createVisitReport(Date date, String parkName) {
        if (!ReportsController.CheckReportExistInDB(ReportType.Visits_Report, date, parkName)) {
        	ArrayList<Object> msg = new ArrayList<>(Arrays.asList(date, parkName));
        	ClientMessage msg2 = new ClientMessage("create_VisitReport", msg, 3);
            ClientUI.chat.accept(msg2);
            ServerMessage srMessage = ChatClient.messageRecievedFromServerEvents.get(msg2.getMethodName());
            visitReport = (ArrayList<Integer>) srMessage.getData();

            List<String> data = convertToStringList(visitReport);
            ReportsController.addReportToDB(ReportType.Visits_Report, date, data, parkName);
        } else {
            displayReportFromDB(date, parkName, ReportType.Visits_Report); 
        }
    } 

    /**
     * Confirms a parameter change request.
     *
     * @param parameter The parameter change request.
     * @param startDate The start date for the parameter change.
     * @param value     The new value for the parameter.
     */
    public static void confirmParameter(ParameterChangeRequest parameter, Date startDate, Object value) {
        updateParameter("confirmed", parameter, startDate, value);
    }

    /**
     * Unconfirms a parameter change request.
     *
     * @param parameter The parameter change request.
     * @param startDate The start date for the parameter change.
     * @param value     The value associated with the unconfirmation.
     */
    public static void unconfirmParameter(ParameterChangeRequest parameter, Date startDate, Object value) {
        updateParameter("unconfirmed", parameter, startDate, value);
    }
    /**
     * Updates the status and value of a parameter change request.
     *
     * @param status    The status of the parameter change request (confirmed or unconfirmed).
     * @param parameter The parameter change request.
     * @param startDate The start date 
     * @param value     The new value for the parameter.
     */
    private static void updateParameter(String status, ParameterChangeRequest parameter, Date startDate, Object value) {
        ArrayList<Object> msg = new ArrayList<>(Arrays.asList(status, startDate, parameter.getType().toString(), parameter.getPark().getName(), "waiting", value));
        System.out.println("Sending message to server: " + msg); 
        ClientMessage msg2 = new ClientMessage("change_Parametervalue", msg, 6);
        ClientUI.chat.accept(msg2);
    }
    /**
     * Retrieves and displays a report from the database.
     *
     * @param date       The report date.
     * @param parkName   The park name.
     * @param reportType The type of report (cancellation report or visit report).
     */
    private static void displayReportFromDB(Date date, String parkName, ReportType reportType) {
        String str = ReportsController.getReport(date, reportType, parkName);
        System.out.println(str);
        List<Integer> report = convertToIntegerList(str.split(" "));
        if (ReportType.cancelled_order_report.equals(reportType)) {
            cancelReport = new ArrayList<>(report);
        } else if (ReportType.Visits_Report.equals(reportType)) {
            visitReport = new ArrayList<>(report);
        }
    }
    /**
     * Converts a list of integers to a list of strings.
     *
     * @param report The list of integers to be converted.
     * @return A list of strings representing the integers.
     */
    private static List<String> convertToStringList(List<Integer> report) {
        List<String> data = new ArrayList<>();
        report.forEach(num -> data.add(String.valueOf(num)));
        return data;
    }
    /**
     * Converts an array of strings to a list of integers.
     *
     * @param data The array of strings to be converted.
     * @return A list of integers parsed from the strings.
     */
    private static List<Integer> convertToIntegerList(String[] data) {
        return Arrays.stream(data)
                     .map(Integer::valueOf)
                     .collect(Collectors.toList());
    }

}
