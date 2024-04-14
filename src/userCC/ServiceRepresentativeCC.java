package userCC;

import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Inviter;
/**
 * The ServiceRepresentativeCC class provides functionality for service representatives,
 * such as adding new guides and retrieving all guides from the database.
 * This class handles interactions related to service representative tasks, including sending requests to the server
 * and receiving responses.
 * @author marwa
 */
public class ServiceRepresentativeCC {

    /**
     * Unified method to send a request to the server and receive a response.
     * 
     * @param methodName The name of the method to call on the server.
     * @param parameters The parameters required for the server method. Can be null.
     * @param size The number of parameters.
     * @return The data from the server's response.
     */
    private static Object sendRequestAndGetResponse(String methodName, ArrayList<Object> parameters, int size) {
        ClientMessage message = new ClientMessage(methodName, parameters, size);
        ClientUI.chat.accept(message);
        ServerMessage response = ChatClient.messageRecievedFromServerEvents.get(methodName);
        return response.getData();
    }

    /**
     * Adds a new guide to the database.
     * 
     * @param newGuide The guide to add.
     * @return true if the operation is successful, else false.
     */
    public static boolean addNewGuide(Inviter newGuide) {
        ArrayList<Object> newGuideList = new ArrayList<>();
        newGuideList.add(newGuide);
        return (boolean) sendRequestAndGetResponse("add_inviterDB", newGuideList, newGuideList.size());
    }

    /**
     * gets all guides from the database.
     * 
     * @return An ArrayList of Inviters representing all guides.
     */
    @SuppressWarnings("unchecked")
	public static ArrayList<Inviter> getAllGuides() {
        return (ArrayList<Inviter>) sendRequestAndGetResponse("get_AllGuides", null, 0);
    }
}
