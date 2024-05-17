package instagram.rmi.server;

import java.time.LocalDateTime;

public class Utils {

    public static String nowDate() {
        return LocalDateTime.now().toString();
    }

    public static void logMsg(String log, String msg) {
        System.out.println(log + ": " + msg);
    }
}
