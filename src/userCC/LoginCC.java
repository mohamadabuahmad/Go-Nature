package userCC;

import java.util.ArrayList;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.User;
/**
 * The WorkerLoginCC class provides functionality for user login authentication.
 * This class handles interactions related to user login authentication, including sending login credentials
 * to the server and receiving user information in response.
 * @author celine
 */
public class LoginCC {

    /**
     * Sends a request to the server and returns the response for the given operation.
     * This method unifies the creation and sending of ClientMessage, and handling of ServerMessage.
     *
     * @param methodName The name of the method to call on the server.
     * @param parameters The parameters required for the server method.
     * @return The data from the server's response as an Object.
     */
    private static Object sendRequestAndGetResponse(String methodName, ArrayList<Object> parameters) {
        ClientMessage message = new ClientMessage(methodName, parameters, parameters.size());
        ClientUI.chat.accept(message);
        ServerMessage response = ChatClient.messageRecievedFromServerEvents.get(methodName);
        return response.getData();
    }

    /**
     * Checks the server for a user with the provided username and password.
     * 
     * @param username The user's username.
     * @param password The user's password.
     * @return An instance of the User if found, otherwise null.
     */
    public static User checkUser(String username, String password) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(username);
        parameters.add(password);
        return (User) sendRequestAndGetResponse("check_User", parameters);
    }
}
