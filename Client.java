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
    private writeJSON writeJSON;


    public Client(Socket clientSocket, ArrayList<String> allStations, writeJSON writeJSON){
        this.clientSocket = clientSocket;
        this.allStations = allStations;
        this.writeJSON = writeJSON;
    }

    @Override
    public void run(){
        try {
            //capture stream and parse it
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            XMLFilter xmlFilter = new XMLFilter(allStations, writeJSON);

            while ((in.readLine()) != null) {
                //xmlFilter.parseData(in);
                xmlFilter.parseData(in);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
