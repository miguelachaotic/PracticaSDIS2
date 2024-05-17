package instagram.rmi.client.stream.unit;

import instagram.rmi.common.InstagramServer;

public class PlayStory {
    public static void main(String[] args) {

        String host;
        int port;
        int idNum;

        InstagramServer serverRemoto = (InstagramServer) java.rmi.Naming.lookup("rmi://" + host + ":" + port + "/instagramServer" + idNum);


    }
}
