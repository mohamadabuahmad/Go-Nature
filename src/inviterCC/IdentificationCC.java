package inviterCC;

import java.util.ArrayList;
import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.Inviter;
/**
 * @author celine
 */

public class IdentificationCC {

    /**
     * Checks for a Traveler in the database.
     *
     * @param idNumber - ID number
     * @return Inviter entity or null
     */
    public static Inviter checkId(String idNumber) {
        if (inputValidator.checkId(idNumber)) {
            Inviter result = sendMessageAndGetResponse("id_isExist", idNumber);
            if (result == null) { // ID not in database
                result = new Inviter(idNumber, false);
                sendMessageAndGetResponse("add_inviterDB", result);
            }
            return result;
        }
        return null;
    }

    /**
     * Checks if the input ID is a guide.
     *
     * @param idNumber - ID number
     * @return 1 if the traveler is a guide, 2 if no ID in database, else 0
     */
    public static int checkGuide(String idNumber) {
        Inviter result = sendMessageAndGetResponse("id_isExist", idNumber);
        if (result == null) return 2; // No ID in database
        if (result.isGuide()) return 1; // Is a guide
        // ID found but not a guide, attempt to change to guide
        result.setGuide(true);
        boolean changeSuccess = sendMessageAndGetResponse("change_inviterToguide", result);
        if (changeSuccess) {
            return 5;
        }
        return 0;
    }

    /**
     * Sends a message to the server and waits for a response.
     *
     * @param methodName The name of the method to invoke on the server.
     * @param data       The data to send.
     * @return The response from the server.
     */
    @SuppressWarnings("unchecked")
	private static <T> T sendMessageAndGetResponse(String methodName, Object data) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(data);
        ClientMessage msgFromClient = new ClientMessage(methodName, list, list.size());
        ClientUI.chat.accept(msgFromClient);
        
        ServerMessage msgFromServer = ChatClient.messageRecievedFromServerEvents.get(methodName);
        return (T) msgFromServer.getData();
    }
}
