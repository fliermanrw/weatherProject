import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private static final int listenPort = 7789;
    private static final int maxConnections = 9000;
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
           while(true){
               socket = serverSocket.accept();
               //liveConnections++;
               Client client = new Client(socket, allStations, writeJSON);
               ExecutorService executorService = Executors.newFixedThreadPool(800);

               executorService.execute(client);

               /*Thread thread = new Thread(client);
               thread.start();*/
           }


       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
