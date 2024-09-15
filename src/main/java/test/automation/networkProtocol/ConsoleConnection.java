package test.automation.networkProtocol;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConsoleConnection {

    private BufferedReader executeCommand(String[] command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            if (!process.waitFor(20, TimeUnit.SECONDS)) {
                process.destroy();
                //System.err.println("Process timed out: " + String.join(" ", command)); will be handled later
                return null;
            }
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException _) {
            // Handle exception (improve this in production)
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // Will be Handled Later
        }
    }

    public List<String> returnListOfConsoleMessage(BufferedReader in, String expectedMessage) throws IOException {
        List<String> consoleMsgLst = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            if (expectedMessage != null && line.equals(expectedMessage)) break;
            consoleMsgLst.add(line);
        }
        return consoleMsgLst;
    }

    public List<String> returnListOfConsoleMessage(BufferedReader in) throws IOException {
        return returnListOfConsoleMessage(in, null);
    }

    private List<String> executeCommandInConsole(String[] command, String expectedMessage) {
        try (BufferedReader reader = executeCommand(command)) {
            return (reader != null) ? returnListOfConsoleMessage(reader, expectedMessage) : new ArrayList<>();
        } catch (IOException _) {
            // Log will be handled later
            return new ArrayList<>();
        }
    }

    public List<String> executeCommandInConsoleReturnList(String[] command, String expectedMessage) {
        return executeCommandInConsole(command, expectedMessage);
    }

    public List<String> executeCommandInConsoleReturnList(String[] command) {
        return executeCommandInConsole(command, null);
    }

    public String executeCommandInConsoleReturnString(String[] command, String expectedMessage) {
        String message = String.join("\n", executeCommandInConsole(command, expectedMessage));
        if (message.endsWith("\n")) {
            return message.substring(0, message.length() - 1);
        }
        return message;
    }

    public String executeCommandInConsoleReturnString(String[] command) {
        String message = String.join("\n", executeCommandInConsole(command, null));
        if (message.endsWith("\n")) {
            return message.substring(0, message.length() - 1);
        }
        return message;
    }

    public String readAuthToken(String authTokenFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(authTokenFile)))) {
            return reader.readLine().trim();
        }
    }
}
