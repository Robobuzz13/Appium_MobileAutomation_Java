package test.automation.constant;

public class EmulatorCommands {

    private EnvironmentConstant envConstant;

    public EmulatorCommands() {
        this.envConstant = new EnvironmentConstant();
    }


    public String[] listAvailableEmulatorCommand = {envConstant.EMULATOR_PATH, "-list-avds"};
    public String[] listRunningEmulatorCommand = {envConstant.ADB_PATH, "devices"};
    //String[] startFirstRunningEmulatorCommand = {EMULATOR_PATH, "-avd", getEmulatorNames().getFirst(), "-wipe-data"};
}
