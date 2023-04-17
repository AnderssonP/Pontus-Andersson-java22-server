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

                while (true) {
                    // Continuously listens for incoming messages from the client
                    String message = bufferedReader.readLine(); // Reads the incoming message from the client
                    System.out.println("client: " + message);
                    if (message.equalsIgnoreCase("ja")) {
                        // Send a message to the client asking for the player name
                        bufferedWriter.write("vilken spelare? " + getNames());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        // Read the player name from the client
                        String player = bufferedReader.readLine();
                        System.out.println("client: " + player);
                        if (playerExists(player)) {
                                    bufferedWriter.write(getInfoFromJson(player));
                                    bufferedWriter.newLine();
                                    bufferedWriter.flush();
                        } else  {
                            bufferedWriter.write("spelaren finns ej, testa igen.");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
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

    // Gets information about a player from a JSON file
    static String getInfoFromJson(String playerName) throws IOException, ParseException {
        String filepath = "src/data.json";
        JSONObject fetchdata = (JSONObject) new JSONParser().parse(new FileReader(filepath));

        JSONObject body = (JSONObject) fetchdata.get("Body");
        JSONArray players = (JSONArray) body.get("Data");

        StringBuilder info = new StringBuilder();
        boolean playerFound = false;

        for (int i = 0; i < players.size(); i++) {
            JSONObject player = (JSONObject) players.get(i);
            String name = (String) player.get("name");
            if (name.equalsIgnoreCase(playerName)) {
                playerFound = true;
                String born = (String) player.get("Born");
                String teams = (String) player.get("Teams");
                String goals = (String) player.get("Goals");

                info.append("Born: ").append(born).append("Teams: ").append(teams).append("Goals: ").append(goals);
            }
        }

        return info.toString();
    }


    private static boolean playerExists(String playerName) throws IOException, ParseException {
        String filepath = "src/data.json";
        JSONObject fetchdata = (JSONObject) new JSONParser().parse(new FileReader(filepath));
        JSONObject body = (JSONObject) fetchdata.get("Body");
        JSONArray data = (JSONArray) body.get("Data");

        for (int i = 0; i < data.size(); i++) {
            Object obj = data.get(i);
            if (obj instanceof JSONObject) {
                JSONObject player = (JSONObject) obj;
                String name = (String) player.get("name");
                if (name.equalsIgnoreCase(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }


    static String getNames() throws IOException, ParseException {
        String filepath = "src/data.json";
        JSONObject fetchdata = (JSONObject) new JSONParser().parse(new FileReader(filepath));
        JSONObject body = (JSONObject) fetchdata.get("Body");
        JSONArray data = (JSONArray) body.get("Data");

        StringBuilder names = new StringBuilder();

        for (int i = 0; i < data.size(); i++) {
            Object obj = data.get(i);
            if (obj instanceof JSONObject) {
                JSONObject player = (JSONObject) obj;
                String name = (String) player.get("name");
                names.append(name).append(", ");
            }
        }

        return names.toString();
    }

}
