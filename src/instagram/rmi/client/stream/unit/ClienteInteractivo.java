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

public class ClienteInteractivo {

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

            instagramServer.setClientStreamReceptor(instagramClient);

            System.out.println("Introduzca EXIT para salir");
            Scanner miScanner = new Scanner(System.in);
            boolean respuestaProcesada = true;
            String respuesta = null;
            do{
                System.out.print("\n>>");
                respuesta = miScanner.nextLine();
                instagramServer.setClientStreamReceptor(instagramClient);
                respuestaProcesada = procesarRespuesta(respuesta, instagramReference, instagramClient);
            }while (respuestaProcesada);



        } catch (NotBoundException e) {
            System.err.println("The server is not active right now!");
        } catch(Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static boolean procesarRespuesta(String respuesta, Remote instagramReference, InstagramClientImpl instagramClient) throws RemoteException, FileNotFoundException, InterruptedException {
        InstagramServer instagramServer = (InstagramServer) instagramReference;

        Instagram instagram = (Instagram) instagramReference;

        String[] respuestas = respuesta.split(" ");

        switch (respuestas[0]){
            case "HELLO":
                System.out.println("<<" + instagram.hello());
                return true;

            case "AUTH":
                System.out.println("<<" + instagram.auth(respuestas[1], respuestas[2]));
                return true;

            case "ADD2L":
                Media mediaAnadida = new Media(respuestas[1]);
                if(respuestas.length>2) {
                    String username = respuestas[2];
                    instagram.add2L(mediaAnadida, username);
                }else{
                    instagram.add2L(mediaAnadida);
                }
                return true;

            case "READL":
                if(respuestas.length>1){
                    System.out.println("<< SE HA LEIDO:\n" + instagram.readL(respuestas[1]));

                }else{
                    System.out.println("<< SE HA LEIDO:\n" + instagram.readL());
                }
                return true;

            case "PEEKL":
                if(respuestas.length>1){
                    System.out.println("<< MEDIA PEEKED:\n" + instagram.peekL(respuestas[1]));

                }else{
                    System.out.println("<< MEDIA PEEKED:\n" + instagram.peekL());
                }
                return true;

            case "DELETEL":
                System.out.println("<<" + instagram.deleteL(respuestas[1]));
                return true;

            case "GETDIRECTORYLIST":
                System.out.println("<<" + instagram.getDirectoryList());
                return true;

            case "RETRIEVEMEDIA":
                System.out.println("<< MEDIA RETRIEVED:\n" + instagram.retrieveMedia(respuestas[1]));
                return true;

            case "ADDLIKE":
                System.out.println("<<" + instagram.addLike(respuestas[1]));
                return true;

            case "ADDCOMMENT":
                System.out.println("<<" + instagram.addComment(respuestas[1], respuestas[2]));
                return true;

            case "SETCOVER":
                Media cover = new Media(respuestas[1]);
                System.out.println("<<" + instagram.setCover(cover));
                return true;

            case "RANDOMPLAY":
                instagramServer.randomPlay();
                while(instagramClient.isMediaPlayerActive()){
                    Thread.sleep(1000); // Esperamos a ver si se ha acabado.
                    // Con thread.sleep evitamos sobrecargar el procesador durmiendo el proceso
                }
                return true;

            case "STARTMEDIA":
                Media mediaReproducida = new Media(respuestas[1]);
                instagramServer.startMedia(mediaReproducida);

                while(instagramClient.isMediaPlayerActive()){
                    Thread.sleep(1000); // Esperamos a ver si se ha acabado.
                    // Con thread.sleep evitamos sobrecargar el procesador durmiendo el proceso
                }
                return true;

            case "EXIT":
                return false;
        }
        return true;
    }

}