package test.automation.networkProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SocketConnection {

    ConsoleConnection console = new ConsoleConnection();
    private int port;
    private String host;

    // Constructor to initialize host and port
    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // ThreadLocal variable for holding the Socket for each thread
    private final ThreadLocal<Socket> threadLocalSocket = ThreadLocal.withInitial(() -> {
        try {
            return new Socket(host, port);
        } catch (IOException _) {
            return null;
        }
    });

    // ThreadLocal variable for holding the PrintWriter for each thread
    private final ThreadLocal<PrintWriter> out = ThreadLocal.withInitial(() -> {
        try {
            return new PrintWriter(threadLocalSocket.get().getOutputStream(), true);
        } catch (IOException _) {
            return null;
        }
    });

    // ThreadLocal variable for holding the BufferedReader for each thread
    private final ThreadLocal<BufferedReader> in = ThreadLocal.withInitial(() -> {
        try {
            return new BufferedReader(new InputStreamReader(threadLocalSocket.get().getInputStream()));
        } catch (IOException _) {
            return null;
        }
    });

    public List<String> getMessageFromSocket(String expectedMessage){
        try{
            return console.returnListOfConsoleMessage(in.get(), expectedMessage);
        }catch(IOException _){
            return null;
        }
    }

    public String getMessageFromSocketString(String expectedMessage){
        try{
           String message = String.join("\n", console.returnListOfConsoleMessage(in.get(), expectedMessage));
            if (message.endsWith("\n")) {
                return message.substring(0, message.length() - 1);
            }
            return message;
        } catch(IOException _){
            return null;
        }
    }

    public void waitForSocketMessage(String expectedMessage){
        try{
            console.returnListOfConsoleMessage(in.get(), expectedMessage);
        }catch(IOException _){
        }
    }

    public void sendMessageToSocket(String message){
        out.get().println(message);
    }

    public void closeSocket(){
        try {
            Socket socket = threadLocalSocket.get();
            if (socket != null) {
                socket.close();
            }
        } catch(IOException _){
        } finally {
            threadLocalSocket.remove();
            out.remove();
            in.remove();
        }
    }


}
