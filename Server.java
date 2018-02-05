import java.net.*;
import java.io.*;

public class Server implements Runnable{
    private static final int listenPort = 7789;
    private static final int maxConnections = 900;
    private static int liveConnections = 0;
    private Socket socket;


    public Server() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run(){
       try {
           ServerSocket serverSocket = new ServerSocket(listenPort);

           // Listen for new connection
           while(liveConnections < maxConnections){
               socket = serverSocket.accept();
               liveConnections++;

               Client client = new Client(socket, liveConnections);
               Thread tread = new Thread(client);
               tread.start();
           }



       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
