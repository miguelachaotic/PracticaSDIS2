package instagram.rmi.client.stream.unit;

import instagram.media.Globals;
import instagram.media.Media;
import instagram.rmi.client.stream.InstagramClientImpl;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramServer;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PlayStory {

    public static final int RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {

        SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();

        SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

        InstagramClientImpl instagramClient = new InstagramClientImpl(clientSocketFactory, serverSocketFactory);

        System.setProperty("java.rmi.server.hostname", "10.0.200.53");

        try {
            Registry instagramRegistry = LocateRegistry.getRegistry(
                    Globals.server_host, RMI_PORT, clientSocketFactory
            );

            Remote instagramReference = instagramRegistry.lookup("instagramServer");

            InstagramServer instagramServer = (InstagramServer) instagramReference;

            Instagram instagram = (Instagram) instagramReference;

            Media media = new Media("SalsaDelGallo");
            Media media2 = new Media("GataBajoLaLluvia");


            instagram.add2L(media);
            instagram.add2L(media2);

            instagramServer.setClientStreamReceptor(instagramClient);

            instagramServer.startMedia(media2);

            while(instagramClient.isMediaPlayerActive()){
                Thread.sleep(1000); // Esperamos a ver si se ha acabado.
                // Con thread.sleep evitamos sobrecargar el procesador durmiendo el proceso
            }


        } catch (NotBoundException e) {
            System.err.println("The server is not active right now!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (InterruptedException e) {
            System.err.println("There was a problem with user Threads!!");
        } catch(Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}