import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {
    private Socket clientSocket;
    private ArrayList<String> euStations;
    private ArrayList<String> tunisiaStations;
    private writeJSON writeJSON;


    public Client(Socket clientSocket, ArrayList<String> allStations, ArrayList<String> tunisiaStations, writeJSON writeJSON){
        this.clientSocket = clientSocket;
        this.euStations = allStations;
        this.tunisiaStations = tunisiaStations;
        this.writeJSON = writeJSON;
    }

    @Override
    public void run(){
        try {
            XMLFilter xmlFilter = new XMLFilter(euStations, tunisiaStations, writeJSON);

            //capture stream and parse it
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while ((in.readLine()) != null) {
                xmlFilter.parseData(in);
            }
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
