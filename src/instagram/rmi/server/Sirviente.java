package instagram.rmi.server;

import instagram.media.Globals;
import instagram.rmi.common.InstagramClient;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Sirviente implements Runnable{

    private final InstagramServerImpl instagramServer;

    private final InetAddress ip;

    private final int port;

    private final int id;

    private static final String CLIENTOBJECT = "instagramClient";

    public Sirviente(InstagramServerImpl instagramServer, InetAddress ip, int port, int id) {
        this.instagramServer = instagramServer;
        this.ip = ip;
        this.port = port;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("instagramServer_" + id, instagramServer);

            Thread.sleep(Globals.delivery_delay_ms);

            InstagramClient instagramClient = (InstagramClient) Naming.lookup(
                    "rmi://" + ip + ":" + port + "/" + CLIENTOBJECT
            );

            instagramServer.setClientStreamReceptor(instagramClient);

        } catch (RemoteException e) {
            System.out.println("Client on " + ip.getHostAddress() + ":" + port + " got disconnected.");
        } catch (InterruptedException | MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
