package test.automation.networkProtocol;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    // Needs to add and Handle interupt exception
    public List<String> returnListOfConsoleMessage(BufferedReader in, String expectedMessage)  {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<List<String>> task = () -> {
            return in.lines()
                    .filter(line -> !line.startsWith("INFO")) // Filter out "INFO" lines
                    .filter(line -> expectedMessage == null || !line.equals(expectedMessage)) // Handle expected message
                    .collect(Collectors.toList());
        };

        Future<List<String>> future = executor.submit(task);
        try {
            return future.get(10, TimeUnit.SECONDS);  // Enforce a 10-second timeout
        } catch (TimeoutException | InterruptedException | ExecutionException _) {
            future.cancel(true);  // Cancel the task if it times out
            return new ArrayList<>();  // Return an empty list on timeout
        } finally {
            executor.shutdown();
        }
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
