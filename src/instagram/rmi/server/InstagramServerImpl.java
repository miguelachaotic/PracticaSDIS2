package instagram.rmi.server;

import instagram.media.Globals;
import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramClient;
import instagram.rmi.common.InstagramServer;
import instagram.media.Media;
import instagram.stream.ServerStream;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;


public class InstagramServerImpl extends UnicastRemoteObject implements InstagramServer, Instagram {

    private static final class Messages {

        private static final String MSG_BIENVENIDA = "Bienvenido a Instagram!!";

        private static final String MSG_DESPEDIDA = "Hasta luego! Esperamos volver a verte!!";

        private static final String NOT_AUTHENTICATED = "Autenticación fallida, vuelva a intentarlo.";

        private static final String AUTHENTICATED = "Usuario autenticado con éxito.";

        private static final String DELETED = "Lista borrada con éxito.";

        private static final String EMPTY = "La lista elegida está vacía!!";

        private static final String LIST_NOT_FOUND = "La lista elegida no existe!!";

        private static final String ADDEDLIKE = "Like añadido correctamente.";

        private static final String MEDIA_NOT_FOUND = "No se ha encontrado la publicación!!";

        private static final String ADDEDCOMMENT = "Comentario añadido correctamente.";

        private static final String BUFFEROVF_COMMENT = "El comentario supera los 100 carácteres!!";

        private static final String SETCOVER = "Se ha actualizado correctamente la imagen.";

        private static final String EMPTYQUEUE = "La cola elegida está vacía!!";

        private static final String EMPTYDIRECTORY = "El directorio está vacío!!!";

        private static final String DELETEDEFAULT = "No puedes borrar la cola general!!";

        private static final String RANDOMPLAY = "Se ha ejecutado correctamente el media random";

        private static final String STARTMEDIA = "Se ha ejecutado correctamente el media elegido";
    }

    private final MultiMap<String, Media> contents;

    private final Map<String, String> passwords;

    private final Map<String, Media> directory;

    private final Map<String, InstagramClient> connectionMap;


    private static final String DEFAULTQUEUE = "DEFAULT";

    public InstagramServerImpl(MultiMap<String, Media> contents,
                               Map<String, String> passwords,
                               Map<String, Media> directory,
                               RMIClientSocketFactory rmiClientSocketFactory,
                               RMIServerSocketFactory rmiServerSocketFactory
    ) throws RemoteException {
        super(0, rmiClientSocketFactory, rmiServerSocketFactory);
        this.contents = contents;
        this.passwords = passwords;
        this.directory = directory;
        this.connectionMap = new ConcurrentHashMap<>();
    }

    @Override
    public String hello() throws RemoteException {
        return Messages.MSG_BIENVENIDA;
    }

    @Override
    public String auth(String username, String password) throws RemoteException {
        if (username == null || password == null)
            throw new IllegalArgumentException();
        synchronized (passwords) {
            if (!passwords.containsKey(username)) {
                passwords.put(username, password);
            }
            if (!passwords.get(username).equals(password)) {
                return Messages.NOT_AUTHENTICATED;
            }
        }
        return Messages.AUTHENTICATED;
    }

    @Override
    public void add2L(Media media) throws RemoteException {
        if (media == null)
            throw new IllegalArgumentException();
        add2L(media, DEFAULTQUEUE);
    }

    @Override
    public void add2L(Media media, String username) throws RemoteException {
        if (media == null || username == null)
            throw new IllegalArgumentException();
        synchronized (directory) {
            if (directory.containsKey(media.getInternalName()))
                media = directory.get(media.getInternalName());
            else directory.put(media.getInternalName(), media);
        }
        synchronized (contents) {
            contents.push(username, media);
        }
    }

    @Override
    public Media readL() throws RemoteException {
        return readL(DEFAULTQUEUE);
    }

    @Override
    public Media readL(String username) throws RemoteException {
        if (username == null)
            throw new IllegalArgumentException();
        synchronized (contents) {
            return contents.pop(username);
        }

    }

    @Override
    public Media peekL() throws RemoteException {
        return peekL(DEFAULTQUEUE);
    }

    @Override
    public Media peekL(String username) throws RemoteException {
        if (username == null)
            throw new IllegalArgumentException();
        synchronized (contents) {
            return contents.peek(username);
        }
    }

    @Override
    public String deleteL(String username) throws RemoteException {
        if (username == null)
            throw new IllegalArgumentException();
        if (username.equals(DEFAULTQUEUE))
            return Messages.DELETEDEFAULT;
        synchronized (contents) {
            if (contents.deleteList(username))
                return Messages.DELETED;
        }
        return Messages.LIST_NOT_FOUND;
    }

    @Override
    public String getDirectoryList() throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        synchronized (directory) {
            if (directory.isEmpty()) return Messages.EMPTYDIRECTORY;
            directory.forEach((k, v) -> stringBuilder.append(
                    v.toString()).append(", ")
            );
        }
        stringBuilder.replace(stringBuilder.length() - 2,
                stringBuilder.length(), "");
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    @Override
    public Media retrieveMedia(String mediaID) throws FileNotFoundException, RemoteException {
        if (mediaID == null)
            throw new IllegalArgumentException();
        synchronized (directory) {
            if (!directory.containsKey(mediaID))
                throw new FileNotFoundException(Messages.MEDIA_NOT_FOUND);
            return directory.get(mediaID);
        }
    }

    @Override
    public String addLike(String mediaID) throws FileNotFoundException, RemoteException {
        Media requestedMedia = retrieveMedia(mediaID);
        requestedMedia.addLike();
        return Messages.ADDEDLIKE;
    }

    @Override
    public String addComment(String mediaID, String comment)
            throws FileNotFoundException, RemoteException {
        if (comment == null || comment.isEmpty())
            throw new IllegalArgumentException();
        if (comment.length() > 100)
            return Messages.BUFFEROVF_COMMENT;
        Media requestedMedia = retrieveMedia(mediaID);
        requestedMedia.addComment(comment);
        return Messages.ADDEDCOMMENT;
    }

    @Override
    public String setCover(Media cover) throws RemoteException {
        if (cover == null)
            throw new IllegalArgumentException();
        Media storedMedia;
        String mediaID = cover.getInternalName();
        try {
            storedMedia = retrieveMedia(mediaID);
            storedMedia.setCover(cover.getCover());
            return Messages.SETCOVER;
        } catch (FileNotFoundException e) {
            return Messages.MEDIA_NOT_FOUND;
        }
    }

    @Override
    public boolean setClientStreamReceptor(InstagramClient client) throws RemoteException {
        try {
            connectionMap.put(getClientHost(), client);
            return true;
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String randomPlay() throws RemoteException {
        Media selectedMedia = null;
        synchronized (directory) {
            if (directory.isEmpty()) return Messages.EMPTYDIRECTORY;
            int randomIndex = new java.util.Random().nextInt(directory.size());
            int i = 0;
            for (String mediaID : directory.keySet()) {
                if (i == randomIndex) {
                    selectedMedia = directory.get(mediaID);
                    break;
                }
                i++;
            }
        }
        if (selectedMedia == null) throw new IllegalStateException();

        try {
            return startMedia(selectedMedia);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public String startMedia(Media media) throws RemoteException, FileNotFoundException {

        try {

            InstagramClient instagramClient = connectionMap.get(getClientHost());
            String pathFile = Globals.path_origin + media.getName() + Globals.file_extension;
            ServerStream serverStream = new ServerStream(pathFile, instagramClient);

            Thread thread = new Thread(serverStream);
            thread.start();
            Thread.sleep(2000);

            instagramClient.launchMediaPlayer(media);

            if (!instagramClient.isMediaPlayerActive()) {
                return "Launcher can't be activated";
            }

            System.out.println("Sending server streaming ready signal" +
                    Globals.server_host + ":" + serverStream.getServerSocketPort());


            instagramClient.startStream(media, Globals.server_host, serverStream.getServerSocketPort());



        } catch (ServerNotActiveException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "Media " + media.getName() + " started";

    }

}
