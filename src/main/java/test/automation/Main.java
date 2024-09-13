package test.automation;

import test.automation.networkProtocol.ConsoleConnection;
import test.automation.networkProtocol.SocketConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.FileSystems;

public class Main {
    public static void main(String[] args) {
        try {

            String host = "localhost";
            int port = 5554;
            String userHome = System.getProperty("user.home");
            String authTokenFile = userHome + FileSystems.getDefault().getSeparator() + ".emulator_console_auth_token";
            ConsoleConnection console = new ConsoleConnection();
            String authToken = console.readAuthToken(authTokenFile);
            SocketConnection socket = new SocketConnection(host, port);

            socket.waitForSocketMessage("OK");
            socket.sendMessageToSocket("auth " + authToken);
            socket.waitForSocketMessage("OK");
            socket.sendMessageToSocket("avd name");
            //socket.waitForSocketMessage("OK");
            System.out.println("AVD Name: " + socket.getMessageFromSocket("OK"));
            socket.closeSocket();




















            /*Socket socket = new Socket(host, port);

            // Step 3: Set up input and output streams for the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Step 4: Read the initial console message
            waitForConsoleMessage(in, "OK");

            // Step 5: Send the authentication command
            out.println("auth " + authToken);
            System.out.println("Sent auth command: auth " + authToken);

            // Step 6: Wait for the "OK" response to the auth command
            waitForConsoleMessage(in, "OK");

            // Step 7: Send the command to get the AVD name
            out.println("avd name");

            // Step 8: Read and print the AVD name
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("OK")) {
                    break;
                }
                System.out.println("AVD Name: " + line);
            }

            // Close the connection
            socket.close();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to read the auth token from the file
    private static String readAuthToken() throws Exception {
        String userHome = System.getProperty("user.home");
        String authTokenFile = userHome + System.getProperty("file.separator") + ".emulator_console_auth_token";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(authTokenFile)));
        return reader.readLine().trim();
    }

    // Method to wait for a specific console message
    private static void waitForConsoleMessage(BufferedReader in, String expectedMessage) throws Exception {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals(expectedMessage)) {
                break;
            }
        }
    }
}