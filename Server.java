import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server implements Runnable{
    private static final int listenPort = 7789;
    private static final int maxConnections = 900;
    private static int liveConnections = 0;
    private Socket socket;

    private Stations stations = new Stations();

    public Server() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run(){
       try {
           ServerSocket serverSocket = new ServerSocket(listenPort);

           // initiate all stations and pass it to the XMLFilter
           ArrayList<String> allStations = stations.getAllStations();
           writeJSON writeJSON = new writeJSON();


           // Listen for new connection
           while(liveConnections < maxConnections){
               socket = serverSocket.accept();
               liveConnections++;

               Client client = new Client(socket, liveConnections, allStations, writeJSON);
               Thread thread = new Thread(client);
               thread.start();
           }



       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
