package userCC;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.ParameterChangeRequest;
import entities.ReportType;
import entities.reportHelper;
/**
 * The ParkMangerCC class provides functionality for park managers to manage reports and parameters,
 * including creating total visitor reports, usage reports, setting parameters, and retrieving parameters.
 * This class handles interactions related to reports, parameters, and park management.
 * @author shady
 */
public class ParkMangerCC {
    public static ArrayList<String> totalVisitorReport;
    public static ArrayList<reportHelper> usageReport;

    /**
     * Sends a request to the server and retrieves the response.
     *
     * @param methodName The name of the method to be called on the server.
     * @param parameters The parameters to be sent to the server.
     * @return The response from the server.
     */
    private static Object sendAndReceive(String methodName, ArrayList<Object> parameters) {
        ClientMessage msg = new ClientMessage(methodName, parameters, parameters.size());
        ClientUI.chat.accept(msg);
        ServerMessage response = ChatClient.messageRecievedFromServerEvents.get(methodName);
        return response.getData();
    }
    /**
     * Creates or gets a total visitor report for a specified date and park.
     *
     * @param date     The date for the report.
     * @param parkName The name of the park.
     */
    @SuppressWarnings("unchecked")
	public static void createTotalVisitorReport(Date date, String ParkName) {
        ReportType reportType = ReportType.number_of_visitors_report;
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(reportType, date, ParkName));
        
        if (!ReportsController.CheckReportExistInDB(reportType, date, ParkName)) {
            ArrayList<Long> result = (ArrayList<Long>) sendAndReceive("create_TotalVisitorReport", parameters);
            
            ArrayList<String> parameterStrings = new ArrayList<>();
            result.forEach(val -> parameterStrings.add(val.toString()));
            
            totalVisitorReport = parameterStrings;
            ReportsController.addReportToDB(reportType, date, parameterStrings, ParkName);
        } else {
            String str = ReportsController.getReport(date, reportType, ParkName);
            totalVisitorReport = new ArrayList<>(Arrays.asList(str.split(" ")));
        }
    }
    /**
     * Creates or gets a usage report for a specified date and park.
     *
     * @param date     The date for the report.
     * @param parkName The name of the park.
     */
    @SuppressWarnings("unchecked")
	public static void createUsageReport(Date date, String ParkName) {
        ReportType reportType = ReportType.Usags_Report;
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(reportType.toString(), date, ParkName));
        if (!ReportsController.CheckReportExistInDB(reportType, date, ParkName)) {
            ArrayList<reportHelper> report = (ArrayList<reportHelper>) sendAndReceive("create_UsageReport", parameters);
            usageReport = report;
            ArrayList<String> data = new ArrayList<>();
            report.forEach(r -> {
                data.add(String.valueOf(r.getDate().getTime()));
                data.add(r.getRange());
            });
            ReportsController.addReportToDB(reportType, date, data, ParkName);
        } else {
            String str = ReportsController.getReport(date, reportType, ParkName);
            ArrayList<String> list = new ArrayList<>(Arrays.asList(str.split(" ")));
            ArrayList<reportHelper> reportHelpers = convertToReportHelpers(list);
            usageReport = reportHelpers;
        }
    }
    /**
     * Sets a parameter for park management.
     *
     * @param parameter The parameter change request.
     * @param startDate The start date 
     */
    public static void setParameter(ParameterChangeRequest parameter, Date startDate) {
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(startDate, parameter.getType().toString(), parameter.getPark().getName(), "waiting", parameter.getNewValue(), parameter.getSendDate()));
        sendAndReceive("set_ParameterDB", parameters);
    }
    /**
     * gets parameters for park manger.
     *
     * @param ParkName The name of the park.
     * @return The list of parameters.
     */
    @SuppressWarnings("unchecked")
	public static ArrayList<Object> getParameter(String ParkName) {
        return (ArrayList<Object>) sendAndReceive("getParameter_ofParkManger", new ArrayList<>(Arrays.asList(ParkName)));
    }
    /**
     * Converts a list of strings to reportHelper objects for usage report.
     *
     * @param list The list of strings to be converted.
     * @return The list of reportHelper objects.
     */
    private static ArrayList<reportHelper> convertToReportHelpers(ArrayList<String> list) {
        ArrayList<reportHelper> reportHelpers = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            reportHelpers.add(new reportHelper(new Date(Long.parseLong(list.get(i))), list.get(i + 1)));
        }
        return reportHelpers;
    }
}
