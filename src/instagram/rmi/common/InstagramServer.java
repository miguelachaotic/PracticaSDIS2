package instagram.rmi.common;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import instagram.media.Media;

/**
 * Interfaz remota que le sirve al cliente para ejecutar métodos del servidor (con un objeto remoto)
 */
public interface InstagramServer extends Remote {

    /**
     * Establece una conexión con un cliente. El objeto cliente que se recibe es remoto.
     * @param client objeto remoto perteneciente a la conexión entrante
     * @return {@code true} si se ha conectado sin problemas. {@code false} si no ha podido
     * @throws RemoteException si ha ocurrido algún error durante la transimisión entre cliente y servidor
     * @throws IllegalArgumentException si {@code client == null}
     */
    boolean setClientStreamReceptor(InstagramClient client) throws RemoteException;

    /**
     * Reproduce un media aleatorio en el cliente establecido mediante {@code setClientStreamReceptor()}
     * @return un mensaje con información útil respecto al resultado de la operación.
     * @throws RemoteException si ha ocurrido algún error durante la transmisión entre cliente y servidor
     */
    String randomPlay() throws RemoteException;

    /**
     * Empieza a reproducir el objeto Media en el cliente establecido mediante {@code setClientStreamReceptor()}
     * @param media el contenido a reproducir.
     * @return un mensaje con el resultado de la operación.
     * @throws RemoteException si ha ocurrido un error en la transmisión de datos.
     * @throws FileNotFoundException si el contenido no se encuentra en el sistema.
     */
    String startMedia(Media media) throws RemoteException, FileNotFoundException;
}
