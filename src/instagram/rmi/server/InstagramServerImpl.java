package instagram.rmi.server;

import instagram.rmi.common.Instagram;
import instagram.rmi.common.InstagramClient;
import instagram.rmi.common.InstagramServer;
import instagram.media.Media;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


public class InstagramServerImpl extends UnicastRemoteObject implements InstagramServer, Instagram {

    private static final class Messages {

        private static final String MSG_BIENVENIDA       = "Bienvenido a Instagram!!";

        private static final String MSG_DESPEDIDA        = "Hasta luego! Esperamos volver a verte!!";

        private static final String NOT_AUTHENTICATED    = "Autenticación fallida, vuelva a intentarlo.";

        private static final String AUTHENTICATED        = "Usuario autenticado con éxito.";

        private static final String DELETED              = "Lista borrada con éxito.";

        private static final String EMPTY                = "La lista elegida está vacía!!";

        private static final String LIST_NOT_FOUND       = "La lista elegida no existe!!";

        private static final String ADDEDLIKE            = "Like añadido correctamente.";

        private static final String MEDIA_NOT_FOUND      = "No se ha encontrado la publicación!!";

        private static final String ADDEDCOMMENT         = "Comentario añadido correctamente.";

        private static final String BUFFEROVF_COMMENT    = "El comentario supera los 100 carácteres!!";

        private static final String SETCOVER             = "Se ha actualizado correctamente la imagen.";

        private static final String EMPTYQUEUE           = "La cola elegida está vacía!!";

        private static final String EMPTYDIRECTORY      = "El directorio está vacío!!!";

        private static final String DELETEDEFAULT       = "No puedes borrar la cola general!!";
    }

    private final MultiMap<String, Media> contents;

    private final Map<String, String> passwords;

    private final Map<String, Media> directory;

    private final Map<String, InstagramClient> users;

    private static final String DEFAULTQUEUE = "DEFAULT";

    protected InstagramServerImpl() throws RemoteException {
        super();
        contents = new MultiMap<>();
        passwords = new HashMap<>();
        directory = new HashMap<>();
        users = new HashMap<>();
    }

    protected InstagramServerImpl(int port) throws RemoteException {
        super(port);
        contents = new MultiMap<>();
        passwords = new HashMap<>();
        directory = new HashMap<>();
        users = new HashMap<>();
    }

    @Override
    public String hello() {
        return Messages.MSG_BIENVENIDA;
    }

    @Override
    public String auth(String username, String password) {
        if(username == null || password == null)
            throw new IllegalArgumentException();
        if(!passwords.containsKey(username)) {
            passwords.put(username, password);
        }
        if(!passwords.get(username).equals(password)) {
            return Messages.NOT_AUTHENTICATED;
        }
        return Messages.AUTHENTICATED;
    }

    @Override
    public void add2L(Media media) {
        if(media == null)
            throw new IllegalArgumentException();
        add2L(media, DEFAULTQUEUE);
    }

    @Override
    public void add2L(Media media, String username) {
        if(media == null || username == null)
            throw new IllegalArgumentException();
        if(directory.containsKey(media.getInternalName()))
            media = directory.get(media.getInternalName());
        contents.push(username, media);
    }

    @Override
    public Media readL() {
        return readL(DEFAULTQUEUE);
    }

    @Override
    public Media readL(String username) {
        if(username == null)
            throw new IllegalArgumentException();
        return contents.pop(username);
    }

    @Override
    public Media peekL() {
        return peekL(DEFAULTQUEUE);
    }

    @Override
    public Media peekL(String username) {
        if(username == null)
            throw new IllegalArgumentException();
        return contents.peek(username);
    }

    @Override
    public String deleteL(String username) {
        if(username == null)
            throw new IllegalArgumentException();
        if(username.equals(DEFAULTQUEUE))
            return Messages.DELETEDEFAULT;
        if(contents.deleteList(username))
            return Messages.DELETED;
        return Messages.LIST_NOT_FOUND;
    }

    @Override
    public String getDirectoryList() {
        StringBuilder stringBuilder = new StringBuilder();
        if(directory.isEmpty()) return Messages.EMPTYDIRECTORY;
        directory.forEach((k, v) -> {
            stringBuilder.append(v.toString()).append(", ");
        });
        stringBuilder.replace(stringBuilder.length() - 2,
                stringBuilder.length(), "");
        return stringBuilder.toString();
    }

    @Override
    public Media retrieveMedia(String mediaID) throws FileNotFoundException {
        if(mediaID == null)
            throw new IllegalArgumentException();
        if(!directory.containsKey(mediaID))
            throw new FileNotFoundException(Messages.MEDIA_NOT_FOUND);
        return directory.get(mediaID);
    }

    @Override
    public String addLike(String mediaID) throws FileNotFoundException {
        Media requestedMedia = retrieveMedia(mediaID);
        requestedMedia.addLike();
        return Messages.ADDEDLIKE;
    }

    @Override
    public String addComment(String mediaID, String comment) throws FileNotFoundException {
        if(comment == null || comment.isEmpty() || comment.length() > 100)
            throw new IllegalArgumentException();
        Media requestedMedia = retrieveMedia(mediaID);
        requestedMedia.addComment(comment);
        return Messages.ADDEDCOMMENT;
    }

    @Override
    public String setCover(Media cover) {
        if(cover == null)
            throw new IllegalArgumentException();
        Media storedMedia;
        String mediaID = cover.getInternalName();
        try{
            storedMedia = retrieveMedia(mediaID);
            storedMedia.setCover(cover.getCover());
            return Messages.SETCOVER;
        } catch (FileNotFoundException e){
            return Messages.MEDIA_NOT_FOUND;
        }
    }

    @Override
    public boolean setClientStreamReceptor(InstagramClient client) throws RemoteException {
        return false;
    }

    @Override
    public String randomPlay() throws RemoteException {
        return "";
    }

    @Override
    public String startMedia(Media media) throws RemoteException, FileNotFoundException {
        return "";
    }
}