package instagram.rmi.client.directory.unit;

import instagram.media.Media;
import instagram.rmi.client.stream.InstagramClientImpl;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramServer;


import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class ClienteNormal {

    public static final int RMI_PORT = 1099;


    public static void main(String[] args) throws RemoteException {

        InstagramClientImpl instagramClient = new InstagramClientImpl();

        try {
            Remote instagramReference = Naming.lookup(
                    "rmi://localhost:" + RMI_PORT + "/instagramServer"
            );
            InstagramServer instagramServer = (InstagramServer) instagramReference;

            Instagram instagram = (Instagram) instagramReference;

            instagram.add2L(new Media("SalsaDelGallo"));

            instagramServer.setClientStreamReceptor(instagramClient);

            instagramServer.startMedia(new Media("SalsaDelGallo"));


        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
