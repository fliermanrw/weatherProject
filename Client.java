import com.sun.deploy.xml.XMLParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable {
    private Socket clientSocket;
    private int clientID;
    private XMLFilter xmlFilter = new XMLFilter();

    public Client(Socket clientSocket, int clientID){
        this.clientID = clientID;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String input;

            while ((in.readLine() != null)) {
                //xmlFilter.filterData(in);
                xmlFilter.parseData(in);
            }
            System.out.println("ehm niks!");
        }


        catch (IOException e) {
            e.printStackTrace();
        }
        }
}
