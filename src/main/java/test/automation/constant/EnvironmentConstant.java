package test.automation.constant;

import test.automation.networkProtocol.ConsoleConnection;

import java.io.File;

public class EnvironmentConstant {

    public final String OS_NAME = System.getProperty("os.name");
    public final String ADB_PATH = getAndroidSdkPath() + File.separator + "platform-tools" + File.separator + "adb";
    public final String EMULATOR_PATH = getAndroidSdkPath() + File.separator + "emulator"+ File.separator + "emulator";
    public final String NODE_PATH = getNodeLocation();
    public final String APPIUM_PATH = getAppiumPath();

    private String getAndroidSdkPath() {
        String sdkPath = System.getenv("ANDROID_HOME");
        if (sdkPath == null || sdkPath.isEmpty()) {
            sdkPath = System.getenv("ANDROID_SDK_ROOT");
        }
        if (sdkPath == null || sdkPath.isEmpty()) {
            String userName = System.getProperty("user.name");
            if (OS_NAME.toLowerCase().contains("win")) {
                sdkPath = "C:"+ File.separator+"Users"+File.separator+userName+File.separator+"AppData"
                        +File.separator+"Local"+File.separator+"Android"+File.separator+"Sdk";
            }
            else{
                sdkPath = "/Users/"+userName+"/Library/Android/sdk";
            }
        }
        return sdkPath;
    }

   private String getNodeLocation(){
        String[] command = OS_NAME.toLowerCase().contains("win") ? new String[]{"where", "node"} : new String[]{"which", "node"};
        return new ConsoleConnection().executeCommandInConsoleReturnString(command);
    }

   private String getAppiumPath() {
        String nodePath = getNodeLocation();
        File nodeDir = new File(nodePath).getParentFile();
        String appiumPath = new File(nodeDir, "node_modules"+File.separator+"appium"+File.separator+"build"+File.separator+"lib"+File.separator+"main.js").getAbsolutePath();
        System.out.println("Appium location: "+appiumPath);  // will be replaced later with logs
        return appiumPath;
    }
}
