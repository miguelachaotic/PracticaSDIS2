package instagram.rmi.client.directory.unit;

import instagram.media.Media;
import instagram.rmi.client.stream.InstagramClientImpl;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramServer;

import javax.imageio.spi.RegisterableService;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteNormal {

    public static final int RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {

        SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();

        SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

        InstagramClientImpl instagramClient = new InstagramClientImpl(clientSocketFactory, serverSocketFactory);

        try {
            Registry instagramRegistry = LocateRegistry.getRegistry(
                    "localhost", RMI_PORT, clientSocketFactory
            );

            Remote instagramReference = instagramRegistry.lookup("instagramServer");

            InstagramServer instagramServer = (InstagramServer) instagramReference;

            Instagram instagram = (Instagram) instagramReference;

            instagram.add2L(new Media("SalsaDelGallo"));

            instagramServer.setClientStreamReceptor(instagramClient);

            instagramServer.startMedia(new Media("SalsaDelGallo"));

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
