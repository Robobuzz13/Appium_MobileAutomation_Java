package test.automation.constant;

import java.nio.file.FileSystems;

public class EmulatorCommands {

    private EnvironmentConstant envConstant = new EnvironmentConstant();

    public EmulatorCommands() {
        this.envConstant = new EnvironmentConstant();
    }

    public final String authTokenFile = envConstant.USER_HOME + FileSystems.getDefault().getSeparator() + ".emulator_console_auth_token";

    public String[] listAvailableEmulatorCommand = {envConstant.EMULATOR_PATH, "-list-avds"};
    public String[] listRunningEmulatorCommand = {envConstant.ADB_PATH, "devices"};

    //String[] startFirstRunningEmulatorCommand = {EMULATOR_PATH, "-avd", getEmulatorNames().getFirst(), "-wipe-data"};
}
