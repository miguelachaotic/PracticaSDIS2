package instagram.rmi.server;

import instagram.media.Media;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ServerLauncher {


    public static final int RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {
        MultiMap<String, Media> contents = new MultiMap<>();
        Map<String, String> passwords = new HashMap<>();
        Map<String, Media> directory = new HashMap<>();

        Integer objectId = 0;


        Socket socket;
        InetAddress address;
        ByteArrayOutputStream bytesOut;
        InstagramServerImpl instagramServer;

        try(ServerSocket serverSocket = new ServerSocket(2000)){
            while(true){
                socket = serverSocket.accept();
                bytesOut = (ByteArrayOutputStream) socket.getOutputStream();

                bytesOut.write(objectId);

                bytesOut.flush();
                bytesOut.close();

                address = socket.getInetAddress();

                instagramServer = new InstagramServerImpl(
                        contents, passwords, directory
                );

                Runnable serverThread = new Sirviente(
                        instagramServer, address, RMI_PORT, objectId
                );

                serverThread.run();

                objectId++;

            }
        } catch (IOException e) {
            System.err.println("Failed to launch Server");
        }









    }



}
