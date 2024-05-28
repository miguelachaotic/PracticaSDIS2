package instagram.rmi.server;

import instagram.media.Globals;
import instagram.media.Media;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;


public class ServerLauncher {


    public static final int RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {
        MultiMap<String, Media> contents = new MultiMap<>();
        Map<String, String> passwords = new HashMap<>();
        Map<String, Media> directory = new HashMap<>();

        if(args.length != 1){
            System.err.println("Usage: java ServerLauncher <path to RMI registry>");
            System.exit(1);
        }

        Globals.server_host = args[0];

        SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

        SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();

        InstagramServerImpl instagramServer = new InstagramServerImpl(
                contents, passwords, directory, clientSocketFactory, serverSocketFactory
        );


        Registry registry = LocateRegistry.createRegistry(RMI_PORT, clientSocketFactory, serverSocketFactory);

        registry.rebind("instagramServer", instagramServer);

        System.out.println("Server started!! Waiting for connections.");
    }
}
