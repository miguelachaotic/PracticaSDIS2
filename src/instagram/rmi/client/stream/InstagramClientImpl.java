package instagram.rmi.client.stream;

import instagram.media.*;
import instagram.media.MediaPlayer;
import instagram.rmi.common.InstagramClient;
import instagram.stream.ClientStream;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

public class InstagramClientImpl extends java.rmi.server.UnicastRemoteObject implements InstagramClient {
    private Thread playerThread;

    public InstagramClientImpl() throws java.rmi.RemoteException{
        super();
    }

    @Override
    public boolean launchMediaPlayer(Media media) throws RemoteException, FileNotFoundException {
        try {
            MediaPlayer mediaplayer = new MediaPlayer(
                    Globals.player_command,
                    Globals.player_abs_filepath+media.getName()+
                            Globals.file_extension,
                    Globals.player_delay_ms
            );
            playerThread = new Thread(mediaplayer, "mediaPlayer");
            playerThread.start();
        }catch (Exception e){ e.printStackTrace(); return false; }
        return true;
    }

    @Override
    public boolean isMediaPlayerActive() throws RemoteException {
        return playerThread.isAlive();
    }

    @Override
    public void startStream(Media transferedMedia, String ip, int port) throws RemoteException, FileNotFoundException {
        ClientStream cs = new ClientStream(transferedMedia, ip, port, playerThread);
        new Thread(cs, "clientstream").start();
    }
}
