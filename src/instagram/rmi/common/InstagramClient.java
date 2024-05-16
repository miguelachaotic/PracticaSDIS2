package instagram.rmi.common;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import instagram.media.Media;

public interface InstagramClient {

    /**
     * Lanza el reproductor multimedia en el lado del cliente para que lea el contenido especificado en {@code media}.
     * @param media el contenido que se quiere reproducir
     * @return true si se ha completado exitósamente la operación.
     * @throws RemoteException si ha ocurrido algún error durante la transimisión de datos.
     * @throws IllegalArgumentException si {@code media == null}
     */
    boolean launchMediaPlayer(Media media) throws RemoteException, FileNotFoundException;

    /**
     * Comprueba que el reproductor del cliente esté activo. Sirve principalmente para evitar enviar contenido
     * a un cliente que no está activo.
     * @return true si el reproductor del cliente está activo
     * @throws RemoteException si ha ocurrido algún error en la transimisón de datos
     */
    boolean isMediaPlayerActive() throws RemoteException;

    /**
     * Da comienzo al stream de contenido en el lado del cliente
     * @param transferedMedia el contenido que se quiere stremear.
     * @param ip la dirección IP a la que se enviará el contenido.
     * @param port el puerto de la máquina a donde se enviará el contenido stremeado.
     * @throws RemoteException si ha ocurrido algún error en la transmisión de datos.
     * @throws IllegalArgumentException si {@code transferedMedia == null} o si {@code ip == null}
     */
    void startStream(Media transferedMedia, String ip, int port) throws RemoteException, FileNotFoundException;
}
