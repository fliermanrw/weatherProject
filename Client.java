import com.sun.deploy.xml.XMLParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {
    private Socket clientSocket;
    private int clientID;
    private ArrayList<String> allStations;
    private OutputWriter outputWriter;


    public Client(Socket clientSocket, int clientID, ArrayList<String> allStations, OutputWriter outputWriter){
        this.clientID = clientID;
        this.clientSocket = clientSocket;
        this.allStations = allStations;
        this.outputWriter = outputWriter;
    }

    @Override
    public void run(){
        try {
            XMLFilter xmlFilter = new XMLFilter(allStations, outputWriter);

            //capture stream and parse it
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while ((in.readLine() != null)) {
                xmlFilter.parseData(in);
            }
            System.out.println("ehm niks!");
        }


        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
