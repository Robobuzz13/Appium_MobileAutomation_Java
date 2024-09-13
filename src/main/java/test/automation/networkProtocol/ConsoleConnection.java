package test.automation.networkProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleConnection {

    public void waitForConsoleMessage(BufferedReader in, String expectedMessage) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals(expectedMessage)) {
                break;
            }
        }
    }

    public List<String> returnListOfConsoleMessage(BufferedReader in, String expectedMessage) throws IOException {
        List<String> consoleMsgLst = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals(expectedMessage)) {
                break;
            }
            consoleMsgLst.add(line);
        }
        return consoleMsgLst;
    }

    public List<String> returnListOfConsoleMessage(BufferedReader in) throws IOException {
        List<String> consoleMsgLst = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            consoleMsgLst.add(line);
        }
        return consoleMsgLst;
    }

    public List<String> executeCommandInConsole(String[] command,String expectedMessage){
        List<String> returnStringStatement = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            returnStringStatement.addAll(returnListOfConsoleMessage(reader,expectedMessage));
        }  catch (IOException _) {   // exception handling will be done later
        }
        return returnStringStatement;
    }

    public List<String> executeCommandInConsole(String[] command){
        List<String> returnStringStatement = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            returnStringStatement.addAll(returnListOfConsoleMessage(reader));
        }  catch (IOException _) {   // exception handling will be done later
        }
        return returnStringStatement;
    }

    public String readAuthToken(String authTokenFile) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(authTokenFile)));
        return reader.readLine().trim();
    }
}
