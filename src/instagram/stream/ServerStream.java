package instagram.stream;

import instagram.media.Globals;
import instagram.rmi.common.InstagramClient;
import instagram.rmi.server.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
* Stream class ServerStream
* @author hector
*/
  
public class ServerStream implements Runnable{

  private final String fileToSend;
  private int serverSocketPort;
  private final InstagramClient client;
  private final String streamLog = Globals.log_path+"streams"+Globals.log_extension;
  
  public ServerStream(String fileToSend, InstagramClient client){
    this.fileToSend = fileToSend;
    this.client = client;
  }

  public int getServerSocketPort() {
    return serverSocketPort;
  }

  public void run(){
    try {
      ServerSocket servsock = new ServerSocket(Globals.server_port);
      this.serverSocketPort = servsock.getLocalPort();
      System.out.println("--Stream Waiting...");
      Socket socket = servsock.accept();
      System.out.println("--Accepted connection : " + socket+"\n");
      Utils.logMsg(streamLog, Utils.nowDate()+Globals.log_stream+socket);
      OutputStream outputStream = null;
      FileInputStream fileInputStream = null;
      BufferedInputStream bufferedInputStream = null;
      try {
        outputStream = socket.getOutputStream();
        
        // send file
        File file = new File(this.fileToSend);
        byte[] bytes = new byte[Globals.tx_packet_size_bytes];
        fileInputStream = new FileInputStream(file);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        
        int count, i = 0, total = 0;
        while ((count = bufferedInputStream.read(bytes)) > 0 && client.isMediaPlayerActive()) {
          System.err.print(String.format("\033[%dA",1)); // Move up
          System.err.print("\033[2K"); // Erase line content
          System.err.println("--" + i + ": Sending " + count + "bytes (" + total + ")");
          outputStream.write(bytes, 0, count);
          Thread.sleep(Globals.delivery_delay_ms);
          i++;
          total += count;
        }
        outputStream.flush();
        System.out.println("--Done. (Sent total: " + total + ")");
        Utils.logMsg(streamLog, Utils.nowDate()+Globals.log_stream_end+total);
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }finally {
        assert outputStream != null;
        assert bufferedInputStream != null;
        outputStream.close();
        fileInputStream.close();
        bufferedInputStream.close();
        servsock.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
