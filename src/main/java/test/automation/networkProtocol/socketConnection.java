package test.automation.networkProtocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class socketConnection {

    public String readAuthToken(String authTokenFile) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(authTokenFile)));
        return reader.readLine().trim();
    }


}
