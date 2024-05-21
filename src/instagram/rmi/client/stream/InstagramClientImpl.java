package instagram.rmi.client.stream;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import instagram.media.Globals;
import instagram.media.Media;
import instagram.media.MediaPlayer;
import instagram.rmi.common.InstagramClient;
import instagram.stream.ClientStream;

public class InstagramClientImpl extends UnicastRemoteObject implements InstagramClient {

    private Thread playerThread;


    public InstagramClientImpl() throws RemoteException {
        super();
    }


    @Override
    public boolean launchMediaPlayer(Media media) throws RemoteException, FileNotFoundException {
        try{
            MediaPlayer mediaPlayer = new MediaPlayer(
                    Globals.player_command,
                    Globals.player_abs_filepath + media.getName() + Globals.file_extension,
                    Globals.player_delay_ms
            );
            playerThread = new Thread(mediaPlayer, "mediaPlayer");
            playerThread.start();

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isMediaPlayerActive() throws RemoteException {
        return playerThread.isAlive();
    }

    @Override
    public void startStream(Media transferedMedia, String ip, int port) throws RemoteException, FileNotFoundException {
        ClientStream clientStream = new ClientStream(transferedMedia, ip, port, playerThread);
        new Thread(clientStream, "clientStream").start();
    }
}
