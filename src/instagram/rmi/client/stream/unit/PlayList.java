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
import java.util.Scanner;

public class PlayList {

    public static final int RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {

        SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();

        SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

        InstagramClientImpl instagramClient = new InstagramClientImpl(clientSocketFactory, serverSocketFactory);


        try {
            Registry instagramRegistry = LocateRegistry.getRegistry(
                    Globals.server_host, RMI_PORT, clientSocketFactory
            );

            Remote instagramReference = instagramRegistry.lookup("instagramServer");

            InstagramServer instagramServer = (InstagramServer) instagramReference;

            Instagram instagram = (Instagram) instagramReference;

            Media media1 = new Media("SalsaDelGallo");
            Media media2 = new Media("SalsaViltrumita");
            Media media3 = new Media("CreeperZombie");
            Media media4 = new Media("Mandanga");
            Media media5 = new Media("GataBajoLaLluvia");


            instagram.add2L(media1);
            instagram.add2L(media2);
            instagram.add2L(media3);
            instagram.add2L(media4);
            instagram.add2L(media5);


            instagramServer.setClientStreamReceptor(instagramClient);


            int respuesta;
            boolean respuestaProcesada = true;
            Scanner miScanner = new Scanner(System.in);

            do{
                System.out.println(instagram.peekL());
                System.out.println("(1) Ver la story actual \n(2) Saltar a la siguiente \n(3) Salir del programa\n Esperando respuesta:");
                respuesta = miScanner.nextInt();
                respuestaProcesada = procesarRespuesta(respuesta, instagramReference, instagramClient);
            }while (respuestaProcesada);



        } catch (NotBoundException e) {
            System.err.println("The server is not active right now!");
        } catch (InterruptedException e) {
            System.err.println("There was a problem with user Threads!!");
        } catch(Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static boolean procesarRespuesta(int respuesta, Remote instagramReference, InstagramClientImpl instagramClient) throws RemoteException, FileNotFoundException, InterruptedException {
        InstagramServer instagramServer = (InstagramServer) instagramReference;

        Instagram instagram = (Instagram) instagramReference;
        switch (respuesta){
            case 1:
                Media mediaActual = instagram.readL();
                instagramServer.startMedia(mediaActual);
                while(instagramClient.isMediaPlayerActive()){
                    Thread.sleep(1000); // Esperamos a ver si se ha acabado.
                    // Con thread.sleep evitamos sobrecargar el procesador durmiendo el proceso
                }
                return true;
            case 2:
                instagram.readL();
                return true;
            case 3:
                return false;
        }
        return true;
    }

}