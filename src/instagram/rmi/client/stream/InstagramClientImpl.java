package instagram.rmi.client.stream;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import instagram.media.Media;
import instagram.rmi.common.InstagramClient;

public class InstagramClientImpl extends UnicastRemoteObject implements InstagramClient {


    public InstagramClientImpl() throws RemoteException {
        super();

    }

    public InstagramClientImpl(int port) throws RemoteException {
        super(port);

    }


    @Override
    public boolean launchMediaPlayer(Media media) throws RemoteException, FileNotFoundException {
        System.out.println(media);
        return false;
    }

    @Override
    public boolean isMediaPlayerActive() throws RemoteException {
        System.out.println("isMediaPlayerActive");
        return false;
    }

    @Override
    public void startStream(Media transferedMedia, String ip, int port) throws RemoteException, FileNotFoundException {
        System.out.println("startStream");
    }
}
