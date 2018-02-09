import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private static final int listenPort = 7789;
    private Socket socket;

    private Stations stations = new Stations();

    public Server() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(listenPort);

            // initiate all stations and pass it to the XMLFilter
            ArrayList<String> euStations = stations.getEUStations();
            ArrayList<String> tunisiaStations = stations.getTunisiaList();
            writeJSON writeJSON = new writeJSON();

                // Listen for new connection
                while (true) {
                    socket = serverSocket.accept();
                    Runnable client = new Client(socket, euStations, tunisiaStations, writeJSON);

                    // Threadpool used for multithreading
                    ExecutorService executorService = Executors.newFixedThreadPool(10000);
                    executorService.execute(client);
                }

        }catch(IOException e){
                //e.printStackTrace();

            }
        }
}

