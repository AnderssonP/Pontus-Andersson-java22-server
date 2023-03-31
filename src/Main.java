import java.io.*;
import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4321); // Creates a new ServerSocket object that listens for incoming client connections on port 4321
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        while (true) { // Continuously listens for client connections
            try {
                socket = serverSocket.accept(); // Accepts a new client connection and assigns it to the socket

                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) { // Continuously listens for incoming messages from the client
                    String message = bufferedReader.readLine(); // Reads the incoming message from the client
                    System.out.println("client: " + message);

                    if (message.equalsIgnoreCase("Drogba")) { // If the message is "Drogba", sends information about Drogba to the client
                        sendInfo(bufferedWriter, "Drogba");
                    } else if (message.equalsIgnoreCase("Zidane")) { // If the message is "Zidane", sends information about Zidane to the client
                        sendInfo(bufferedWriter, "Zidane");
                    } else if (message.equalsIgnoreCase("Iniesta")) { // If the message is "Iniesta", sends information about Iniesta to the client
                        sendInfo(bufferedWriter, "Iniesta");
                    } else { // If the message is not any of the above, sends a confirmation message to the client
                        bufferedWriter.write("message received");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                    if (message.equalsIgnoreCase("quit")) { // If the message is "quit", breaks out of the loop and closes the connection
                        break;
                    }
                }

                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // Sends information about a player to the client
    static void sendInfo(BufferedWriter bufferedWriter, String playerName) throws IOException, ParseException {
        String info = getInfoFromJson(playerName); // Gets the player information from a JSON file
        bufferedWriter.write(info);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    // Gets information about a player from a JSON file
    static String getInfoFromJson(String playerName) throws IOException, ParseException {
        String filepath = "src/data.json"; // Specifies the file path of the JSON file
        JSONObject fetchdata = (JSONObject) new JSONParser().parse(new FileReader(filepath)); // Parses the JSON file and stores it in a JSONObject

        JSONObject player = (JSONObject) fetchdata.get(playerName); // Gets the JSONObject for the specified player

        String info = player.get("Born").toString() + "/ " + player.get("Teams").toString() + "/ " + player.get("Goals").toString(); // Formats the player information as a string
        return info; // Returns the player information string
    }
}
