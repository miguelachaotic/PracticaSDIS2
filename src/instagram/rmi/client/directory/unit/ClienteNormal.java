package instagram.rmi.client.directory.unit;

import instagram.media.Globals;
import instagram.media.Media;
import instagram.rmi.client.stream.InstagramClientImpl;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramServer;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.File;
import java.io.FileNotFoundException;
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

            Media media = new Media("SalsaDelGallo");

            instagram.add2L(media);

            instagramServer.setClientStreamReceptor(instagramClient);

            instagramServer.startMedia(media);

            while(instagramClient.isMediaPlayerActive());

            new File(Globals.path_destination + media.getName() + Globals.file_extension).delete();

            System.exit(0);

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
