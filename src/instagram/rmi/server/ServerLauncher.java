package instagram.rmi.server;

import instagram.media.Media;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class ServerLauncher {

    public static void main(String[] args) throws RemoteException {
        MultiMap<String, Media> contents = new MultiMap<>();
        Map<String, String> passwords = new HashMap<>();
        Map<String, Media> directory = new HashMap<>();

        InstagramServerImpl instagramServer = new InstagramServerImpl();

        Registry registry = LocateRegistry.createRegistry(1099);





    }



}
