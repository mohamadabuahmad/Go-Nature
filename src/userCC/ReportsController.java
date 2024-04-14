package userCC;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.ParameterChangeRequest;
import entities.ReportType;
/**
 * The ReportsController class provides functionality for managing reports and parameter change requests.
 * This class handles interactions related to retrieving reports, adding reports to the database,
 * checking report existence, and fetching parameter change requests.
 * @author shady
 * @author mohamd
 */
public class ReportsController {
	/**
     * Sends a request to the server and retrieves the response.
     *
     * @param methodName The name of the method to be called on the server.
     * @param parameters The parameters to be sent to the server.
     * @return The response from the server.
     */
    private static Object sendRequestAndGetResponse(String methodName, ArrayList<Object> parameters) {
        ClientMessage message = new ClientMessage(methodName, parameters, parameters.size());
        ClientUI.chat.accept(message);
        ServerMessage response = ChatClient.messageRecievedFromServerEvents.get(methodName);

        if (response == null) {
            System.err.println("Error: No response received from server for method " + methodName);
            return null;
        }

        return response.getData();
    }
    /**
     * gets a report from the database for a specified date, report type, and park.
     *
     * @param date       The date of the report.
     * @param reportType The type of the report.
     * @param ParkName   The name of the park.
     * @return The report as a string.
     */
    public static String getReport(Date date, ReportType reportType, String ParkName) {
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(reportType.toString(), date, ParkName));
        Object response = sendRequestAndGetResponse("get_Report", parameters);
        return response != null ? (String) response : "";
    }
    /**
     * Adds a report to the database.
     *
     * @param reportType The type of the report.
     * @param date       The date of the report.
     * @param list       The list of report data.
     * @param ParkName   The name of the park.
     */
    public static void addReportToDB(ReportType reportType, Date date, List<String> list, String ParkName) {
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(reportType.toString(), date, new ArrayList<>(list), ParkName));
        sendRequestAndGetResponse("insert_Report", parameters);
    }
    /**
     * Checks the existence of a report in the database.
     *
     * @param reportType The type of the report.
     * @param date       The date of the report.
     * @param ParkName   The name of the park.
     * @return True if the report exists in the database, false otherwise.
     */
    public static boolean CheckReportExistInDB(ReportType reportType, Date date, String ParkName) {
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(reportType.toString(), date, ParkName));
        Object response = sendRequestAndGetResponse("check_ReportExistence", parameters);
        return response != null && (boolean) response;
    }
    /**
     * gets all parameter change requests associated with a park.
     *
     * @param ParkName The name of the park.
     * @return An ArrayList of parameter change requests.
     */
    @SuppressWarnings("unchecked")
	public static ArrayList<ParameterChangeRequest> GetAllRequests(String ParkName) {
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(ParkName));
        Object response = sendRequestAndGetResponse("get_AllRequests", parameters);
        return response != null ? (ArrayList<ParameterChangeRequest>) response : new ArrayList<>();
    }
}
