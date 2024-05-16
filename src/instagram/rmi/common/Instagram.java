package instagram.rmi.common;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import instagram.media.Media;

public interface Instagram extends Remote {

    /**
     * Envía un mensaje al cliente que ha ejecutado el método
     * @return un mensaje de bienvenida
     */
    String hello() throws RemoteException;

    /**
     * Autentifica a un usuario en el sistema. Si el usuario no existe lo incluirá en el sistema.
     * Además, si el usuario sí existe pero su contraseña es incorrecta no se considerará un error y podrá
     * seguir accediendo al resto de funciones del sistema (así viene en la práctica)
     * @param username el nombre de usuario, debe ser {@code != null}
     * @param password la contraseña de ese usuario. Debe ser {@code != null}
     * @return un String indicando si se ha realizado correctamente la operación. El String enviado es siempre
     * distinto de {@code null}
     * @throws IllegalArgumentException si {@code username == null} o si {@code password == null}
     */
    String auth(String username, String password) throws RemoteException;

    /**
     * Añade un contenido a la cola general. A esta cola no es necesario un usuario para poder modificar o ver su
     * contenido. Si no quedan contenidos en la cola devolverá {@code null}.
     * @param media el contenido que se quiere subir en el sistema.
     * @throws IllegalArgumentException si {@code media == null}
     */
    void add2L(Media media) throws RemoteException;

    /**
     * Añade un contenido a la cola del usuario especificado por {@code username}. Si el usuario no existe se creará
     * una nueva cola para él.
     * @param media el contenido que se quiere subir al servidor.
     * @param username el nombre de usuario de la cola a la que se quiere acceder.
     * @throws IllegalArgumentException si {@code media == null} o si {@code username == null}
     */
    void add2L(Media media, String username) throws RemoteException;

    /**
     * Lee y consume el primer elemento de la cola general. El elemento desaparece de la cola general.
     * @return el contenido eliminado de la cola general. Se asegura que siempre es {@code != null}
     */
    Media readL() throws RemoteException;

    /**
     * Lee y consume el primer elemento de la cola especificada por {@code username}. Si el usuario no existe saltará
     * {@code IllegalArgumentException}. Si la cola está vacía devolverá {@code null}.
     * @param username el nombre de usuario de la cola a la que se quiere acceder.
     * @return el contenido eliminado de la cola del usuario proporcionado. Si no quedan contenidos en la cola
     *         devolverá {@code null}
     * @throws IllegalArgumentException si {@code username == null} o no existe en el sistema.
     */
    Media readL(String username) throws RemoteException;

    /**
     * Lee el primer elemento de la cola general. Si no hay ningún elemento en la cola devolverá {@code null}.
     * @return el primer elemento de la cola general sin eliminarlo.
     */
    Media peekL() throws RemoteException;

    /**
     * Lee el primer elemento de la cola especificada por el nombre de usuario {@code username}. Si el usuario no
     * existe se lanzará {@code IllegalArgumentException}. Si no hay elementos en su cola devolverá {@code null}.
     * @param username el nombre de usuario al que se quiere acceder.
     * @return el primer contenido de la cola del usuario {@code username}.
     * @throws IllegalArgumentException si {@code username == null} o si no existe en el sistema.
     */
    Media peekL(String username) throws RemoteException;

    /**
     * Elimina la cola del usuario proporcionado en {@code username}. Se indicará en el valor de retorno el resultado
     * de esta operación.
     * @param username el nombre de usuario que representa la cola que se quiere borrar.
     * @return un string que indica el resultado de la operación. Si el usuario no existe indicará que no se ha borrado
     *         ya que no existía. Si sí existe, entonces se borrará y devolverá un mensaje indicando que se ha
     *         completado de manera correcta.
     * @throws IllegalArgumentException si {@code username == null}
     */
    String deleteL(String username) throws RemoteException;

    /**
     * Obtiene un string con los identificadores de todos los contenidos en la cola general. No devuelve los objetos
     * Media.
     * @return un string con todos los identificadores.
     */
    String getDirectoryList() throws RemoteException;

    /**
     * Obtiene un objeto Media a partir de su identificador.
     * @param mediaID el identificador del contenido al que se quiere acceder.
     * @return el objeto Media representado por su identificador.
     * @throws IllegalArgumentException si {@code mediaID == null}.
     * @throws FileNotFoundException si el contenido no se ha encontrado.
     */
    Media retrieveMedia(String mediaID) throws FileNotFoundException, RemoteException;

    /**
     * Añade un like a un objeto Media dado su identificador.
     * @param mediaID el identificador que representa al objeto Media.
     * @return un mensaje con el resultado de la operación.
     * @throws IllegalArgumentException si {@code mediaID == null}
     * @throws FileNotFoundException si el contenido no se ha encontrado
     */
    String addLike(String mediaID) throws FileNotFoundException, RemoteException;

    /**
     * Añade un comentario a un objeto Media.
     * @param mediaID el identificador que representa al objeto Media que se quiere acceder.
     * @param comment el comentario que se quiere añadir.
     * @return un string con el resultado de la operación.
     * @throws IllegalArgumentException si {@code mediaID == null} o si {@code comment == null}
     * @throws FileNotFoundException si no se ha encontrado el contenido
     */
    String addComment(String mediaID, String comment) throws FileNotFoundException, RemoteException;

    /**
     * Establece una cover (una imagen) a un objeto tipo Media.
     * @param cover la nueva imagen que tendrá el objeto Media.
     * @return un string con el resultado de la operación.
     * @throws IllegalArgumentException si {@code cover == null}.
     */
    String setCover(Media cover) throws RemoteException;
}
