package test.automation.deviceManagement.android;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class emulatorManagement {

    public String osName = System.getProperty("os.name");
    public final String ADB_PATH = getAndroidSdkPath() + File.separator + "platform-tools" + File.separator + "adb";
    public final String EMULATOR_PATH = getAndroidSdkPath() + File.separator + "emulator"+ File.separator + "emulator";
    String[] listAvailableEmulatorCommand = {EMULATOR_PATH, "-list-avds"};
    String[] listRunningEmulatorCommand = {ADB_PATH, "devices"};
    String[] startFirstRunningEmulatorCommand = {EMULATOR_PATH, "-avd", getEmulatorNames().getFirst(), "-wipe-data"};


    public String getAndroidSdkPath() {
        String sdkPath = System.getenv("ANDROID_HOME");
        if (sdkPath == null || sdkPath.isEmpty()) {
            sdkPath = System.getenv("ANDROID_SDK_ROOT");
        }
        if (sdkPath == null || sdkPath.isEmpty()) {
            String userName = System.getProperty("user.name");
            if (osName.toLowerCase().contains("win")) {
                sdkPath = "C:"+ File.separator+"Users"+File.separator+userName+File.separator+"AppData"
                        +File.separator+"Local"+File.separator+"Android"+File.separator+"Sdk";
            }
            else{
                sdkPath = "/Users/"+userName+"/Library/Android/sdk";
            }
        }
        return sdkPath;
    }

    public String getNodeLocation(){
        String[] command = osName.toLowerCase().contains("win") ? new String[]{"where", "node"} : new String[]{"which", "node"};
        return getStringFromCommand(command);
    }

    public String getAppiumPath() {
        String nodePath = getNodeLocation();
        File nodeDir = new File(nodePath).getParentFile();
        String appiumPath = new File(nodeDir, "node_modules"+File.separator+"appium"+File.separator+"build"+File.separator+"lib"+File.separator+"main.js").getAbsolutePath();
        System.out.println("Appium location: "+appiumPath);  // will be replaced later with logs
        return appiumPath;
    }



    public void executeCommand(String[] command){
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Process result: " + line);   // will be replaced later with logs
            }
            process.waitFor();

        }  catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);     // exception handling will be done later
        }
    }

    public void executeCommandWithProcessBreak(String[] command,String breakCommand){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains(breakCommand)) {
                    break;
                }
            }
        }  catch (IOException e) {
            throw new RuntimeException(e);     // exception handling will be done later
        }
    }

    public String getStringFromCommand(String[] command){
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
        }  catch (IOException e) {
            throw new RuntimeException(e);     // exception handling will be done later
        }
        return output.toString().trim();
    }

    public List<String> getEmulatorNames(){
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
    }

    public String getListOfAvailableEmulator(){
        return getStringFromCommand(listAvailableEmulatorCommand);
    }

    public String getListOfRunningEmulator(){
        return getStringFromCommand(listRunningEmulatorCommand);
    }

    public void startEmulator(){
        executeCommandWithProcessBreak(startFirstRunningEmulatorCommand,"Boot completed");
    }

}
