package instagram.stream;

import instagram.media.Globals;
import instagram.rmi.common.InstagramClient;
import instagram.rmi.server.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket

/**
* Stream class ServerStream
* @author hector
*/
  
public class ServerStream implements Runnable{

  private String fileToSend;
  private int serverSocketPort;
  private InstagramClient client;
  private String streamLog = Globals.log_path+"streams"+Globals.log_extension;
  
  public ServerStream(String fileToSend, InstagramClient client){
    this.fileToSend = fileToSend;
    this.client = client;
  }

  public int getServerSocketPort() {
    return serverSocketPort;
  }

  public void run(){
    try {
      ServerSocket servsock = new ServerSocket(0);
      this.serverSocketPort = servsock.getLocalPort();
      System.out.println("--Stream Waiting...");
      Socket sock = servsock.accept();
      System.out.println("--Accepted connection : " + sock+"\n");
      Utils.logMsg(streamLog, Utils.nowDate()+Globals.log_stream+sock);
      OutputStream os = null;
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      try {
        os = sock.getOutputStream();
        
        // send file
        File myFile = new File(this.fileToSend);
        byte[] mybytearray = new byte[Globals.tx_packet_size_bytes];
        fis = new FileInputStream(myFile);
        bis = new BufferedInputStream(fis);
        
        int count, i = 0, total = 0;
        while ((count = bis.read(mybytearray)) > 0 && client.isMediaPlayerActive()) {
          System.err.print(String.format("\033[%dA",1)); // Move up
          System.err.print("\033[2K"); // Erase line content
          System.err.println("--" + i + ": Sending " + count + "bytes (" + total + ")");
          os.write(mybytearray, 0, count);
          Thread.sleep(Globals.delivery_delay_ms);
          i++;
          total += count;
        }
        os.flush();
        System.out.println("--Done. (Sent total: " + total + ")");
        Utils.logMsg(streamLog, Utils.nowDate()+Globals.log_stream_end+total);
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }finally {
        os.close();
        fis.close();
        bis.close();
        servsock.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
