package userCC;

import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Park;
/**
 * The WorkerCC class provides functionality for workers, such as checking park vacancy.
 * This class handles interactions related to worker tasks, including sending requests to the server
 * and receiving responses.
 * @author mohamad
 */
public class WorkerCC {
    
    /**
     * Unified method to send a request to the server and receive a response.
     * This method simplifies the process of communicating with the server by consolidating
     * the creation of ClientMessage, sending it, and receiving the ServerMessage.
     *
     * @param methodName The name of the method to call on the server.
     * @param parameters The parameters required for the server method.
     * @return The data from the server's response.
     */
    private static Object sendRequestAndGetResponse(String methodName, ArrayList<Object> parameters) {
        ClientMessage message = new ClientMessage(methodName, parameters, parameters.size());
        ClientUI.chat.accept(message);
        ServerMessage response = ChatClient.messageRecievedFromServerEvents.get(methodName);
        return response.getData();
    }

    /**
     * get the park data based on the given park name.
     *
     * @param parkName The name of the park to check vacancy for.
     * @return The Park entity containing the current park's data.
     */
    public static Park checkParkVacancy(String parkName) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(parkName);
        return (Park) sendRequestAndGetResponse("check_ParkAvailability", parameters);
    }
}
