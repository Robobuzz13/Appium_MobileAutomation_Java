package test.automation.deviceManagement.android;

import test.automation.constant.EmulatorCommands;
import test.automation.networkProtocol.ConsoleConnection;

public class EmulatorManagement {

    private ConsoleConnection console;
    private EmulatorCommands emulatorCommands;

    public EmulatorManagement() {
        this.console = new ConsoleConnection();
        this.emulatorCommands = new EmulatorCommands();
    }

    /*public List<String> getEmulatorNames(){
        String output = getListOfAvailableEmulator();
        List<String> emulatorList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(output))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("INFO")) {
                    emulatorList.add(line);
                }
            }
        } catch (IOException _) {
             // will be replaced by exception handling
        }
        return emulatorList;
    }*/

    public String getListOfAvailableEmulator(){
        return console.executeCommandInConsoleReturnString(emulatorCommands.listAvailableEmulatorCommand);
    }

    public String getListOfRunningEmulator(){
        return console.executeCommandInConsoleReturnString(emulatorCommands.listRunningEmulatorCommand);
    }

   /* public void startEmulator(){
        executeCommandWithProcessBreak(startFirstRunningEmulatorCommand,"Boot completed");
    }*/

}
