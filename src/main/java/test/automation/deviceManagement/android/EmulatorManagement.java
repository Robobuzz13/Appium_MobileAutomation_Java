package test.automation.deviceManagement.android;

import test.automation.constant.EmulatorCommands;
import test.automation.constant.EnvironmentConstant;
import test.automation.networkProtocol.ConsoleConnection;
import test.automation.networkProtocol.SocketConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmulatorManagement {

    private ConsoleConnection console;
    private EmulatorCommands emulatorCommands;

    public EmulatorManagement() {
        this.console = new ConsoleConnection();
        this.emulatorCommands = new EmulatorCommands();
    }

    public String getListOfAvailableEmulator(){
        return console.executeCommandInConsoleReturnString(emulatorCommands.listAvailableEmulatorCommand);
    }

    public String getListOfRunningEmulator(){
        return console.executeCommandInConsoleReturnString(emulatorCommands.listRunningEmulatorCommand);
    }

    public List<Integer> getListOfRunningEmulatorPort(){
        List<Integer> runningEmulatorPort = new ArrayList<>();
        Pattern pattern = Pattern.compile("emulator-(\\d+)");
        for (String result : console.executeCommandInConsoleReturnList(emulatorCommands.listRunningEmulatorCommand)) {
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                try {
                    runningEmulatorPort.add(Integer.parseInt(matcher.group(1)));
                } catch (NumberFormatException _) {
                    // Log the error or handle it accordingly
                }
            }
        }
        return runningEmulatorPort;
    }

    public String getEmulatorNameFromPort(int port){
        SocketConnection socket = null;
        try{
           socket = new SocketConnection("localhost", port);
            socket.waitForSocketMessage("OK");
            socket.sendMessageToSocket("auth " + console.readAuthToken(emulatorCommands.authTokenFile));
            socket.waitForSocketMessage("OK");
            socket.sendMessageToSocket("avd name");
            return socket.getMessageFromSocketString("OK");
        }catch (IOException _) {
            return null;
            // Log the error or handle it accordingly
        }
        finally {
            if (socket != null) socket.closeSocket();
        }
    }

    public List<String> getListOfRunningEmulatorName(){
        List<String> runningEmulatorName = new ArrayList<>();
        for(int port: getListOfRunningEmulatorPort()){
            runningEmulatorName.add(getEmulatorNameFromPort(port));
        }
        return runningEmulatorName;
    }

   /* public void startEmulator(){
        executeCommandWithProcessBreak(startFirstRunningEmulatorCommand,"Boot completed");
    }*/

}
