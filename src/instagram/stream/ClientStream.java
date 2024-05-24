package instagram.stream;

import instagram.media.Globals;
import instagram.media.Media;


import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

/**
 * Stream class ClientStream
 *
 * @author hector
 */

public class ClientStream implements Runnable {


    private final String serverStreaming;
    private final int serverStreamingPort;
    private final Thread activeMediaPlayer;

    private final String FILE_TO_RECEIVE;
    private final int FILE_PACKET_SIZE;

    public ClientStream(Media media, String server, int port, Thread player) {
        FILE_TO_RECEIVE = Globals.path_destination + media.getName() +
                Globals.file_extension;
        FILE_PACKET_SIZE = Globals.tx_packet_size_bytes;
        serverStreaming = server;
        serverStreamingPort = port;
        activeMediaPlayer = player;
    }

    public void run() {
        System.out.println("Client port = " + serverStreamingPort);
        int bytesRead;
        int currentBytes = 0;

        SSLSocket socket = null;
        SSLSocketFactory factoria =
                (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            System.out.println("Connecting to " + serverStreaming + " in port: " + serverStreamingPort);
            socket = (SSLSocket) factoria.createSocket(serverStreaming, serverStreamingPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            System.out.println(">> Stream connecting to" + serverStreaming + ":" + serverStreamingPort);
            inputStream = socket.getInputStream();
            File file = new File(FILE_TO_RECEIVE);
            file.getParentFile().mkdirs();
            file.createNewFile(); // if file already exists will do nothing

            // receive file
            byte[] bytes = new byte[FILE_PACKET_SIZE];
            fileOutputStream = new FileOutputStream(FILE_TO_RECEIVE);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            while ((bytesRead = inputStream.read(bytes)) > 0 && (activeMediaPlayer.isAlive())) {
                System.err.print(String.format("\033[%dA", 1)); // Move up
                System.err.print("\033[2K"); // Erase line content
                System.err.print("Rcv " + bytesRead + "(pos:" + currentBytes + ") ");
                bufferedOutputStream.write(bytes, 0, bytesRead);
                currentBytes += bytesRead;
                System.err.println(">> Written " + bytesRead + "(" + currentBytes + "bytes)");
            }

            bufferedOutputStream.flush();
            System.out.println(">> File " + FILE_TO_RECEIVE + " downloaded (" + currentBytes + " bytes read)");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
                bufferedOutputStream.close();
                fileOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ClientStream closed");
    }
}
