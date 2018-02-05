import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class writeJSON {

    public void writeJSON(String stn, String date, String time, String visib){
        JSONObject station = new JSONObject();
        JSONObject stationData = new JSONObject();

        try {
            stationData.put("Date: ", date);
            stationData.put("Time: ", time);
            stationData.put("Visibility: ", visib);

            station.put(stn,stationData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveJSON(station, stn);
    }

    public void saveJSON(JSONObject station, String stn) {
        //FileWriter fw = new FileWriter("D:\\bullshit\\"+ countryName +"\\" + date + "--" + time + ".txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter("D:\\bullshit\\" + stn + ".json");
            bw = new BufferedWriter(fw);

            bw.write(station.toString());


            System.out.println(station);
            fw.write(station.toString());

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
