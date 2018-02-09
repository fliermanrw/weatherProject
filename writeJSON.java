import org.json.simple.JSONObject;
import java.io.*;

public class writeJSON {
    // writes history to
    private void appendHistory(JSONObject station, String stn){
        File file = new File("/mnt/nfs/history" + stn + ".json");
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(station.toString());
            fw.write(station.toString());
            bw.write("\r\n");

            bw.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    // test vars
    private JSONObject duizendStations = new JSONObject();
    private int duizendCounter = 0;

    public void finallyWrite(JSONObject moreStations, String stn){
        duizendStations.put(stn,moreStations);
        duizendCounter++;

        if(duizendCounter == 2500){
            writeOnceMore(duizendStations);
            duizendStations = new JSONObject();
            duizendCounter = 0;
        }
    }

    public void writeOnceMore(JSONObject duizendStations){
        String x = "stationsEU";
        File file = new File("/mnt/nfs/" + x + ".json");
        //File file = new File("D:\\bullshit\\" + x + ".json");
        try {
            fileWriteJSON(duizendStations, file);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void writeTunStation(JSONObject tunStation, String stn){
        File file = new File("/mnt/nfs/tun/" + stn + ".json");
        //File file = new File("D:\\bullshit\\tun\\" + stn + ".json");

        try {
            fileWriteJSON(tunStation, file);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void fileWriteJSON(JSONObject tunStation, File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(tunStation.toJSONString());
        bw.close();
    }
}
