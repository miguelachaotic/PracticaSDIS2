package instagram.rmi.client.directory.unit;

import instagram.media.Globals;
import instagram.media.Media;
import instagram.rmi.client.stream.InstagramClientImpl;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteNormal {


    public static void main(String[] args) throws RemoteException {

        // Lo primero tiene que conectarse al servidor

        InstagramClientImpl instagramClient = new InstagramClientImpl();

        InputStream inputStream;
        int userId;
        byte[] userData = new byte[4];

        Registry registry = LocateRegistry.createRegistry(1100);


        try(Socket socket = new Socket("localhost", 2000)){

            inputStream = socket.getInputStream();

            if(inputStream.read(userData) == -1) {
                throw new RuntimeException();
            }
            userId = userData[0];
            userId += userData[1] * 256;
            userId += userData[2] * 256 * 256;
            userId += userData[3] * 256 * 256 * 256;

            Thread.sleep(Globals.delivery_delay_ms);

            registry.rebind("instagramClient_" + userId, instagramClient);

            System.out.println(userId);

            InstagramServer instagramServer = (InstagramServer) Naming.lookup(
                    "rmi://localhost:1099/instagramServer_" + userId
            );

            Instagram instagramUtilities = (Instagram) Naming.lookup(
                    "rmi://localhost:1099/instagramServer_" + userId
            );

            System.out.println(instagramUtilities + " : " + instagramServer);

            Media publicacion = new Media("Hola buenas");

            instagramUtilities.add2L(publicacion);

            publicacion.addLike();

            System.out.println(publicacion);

            Media readMedia = instagramUtilities.readL();

            System.out.println(readMedia);



        } catch (IOException e) {
            System.out.println("Ha habido problemas conectando el cliente con el servidor");
            System.err.println(e);
        } catch (InterruptedException e) {
            System.out.println("Error esperando datos");
        } catch (NotBoundException e) {
            System.err.println("Objeto remoto aún no instanciado");
        }


    }


}
