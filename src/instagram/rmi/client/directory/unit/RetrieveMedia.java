package instagram.rmi.client.directory.unit;

import instagram.media.Globals;
import instagram.media.Media;
import instagram.rmi.common.Instagram;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RetrieveMedia {

    public static final int RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {

        Globals.server_host = args[0];

        Media media = new Media(args[1]);

        SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();


        try {
            Registry instagramRegistry = LocateRegistry.getRegistry(
                    Globals.server_host, RMI_PORT, clientSocketFactory
            );

            Instagram instagram = (Instagram) instagramRegistry.lookup("instagramServer");

            media = instagram.retrieveMedia(media.getInternalName());

            System.out.println(media);


        } catch (NotBoundException e) {
            System.err.println("The server is not active right now!");
        } catch (NullPointerException e) {
            System.err.println("Media not found");
        } catch (Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}