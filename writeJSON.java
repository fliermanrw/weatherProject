import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class writeJSON {
    private Stations stationsclass = new Stations();
    // om alles netjes in mappen te kunnen doen?
    JSONObject stationsPerCountry = stationsclass.getCountryStations();

    private int counter = 0;
    private boolean timeToWriteHistory = false;


    public void writeToJSON(String stn, String date, String time, String visib, String cldc, String prpc){
        JSONObject station = new JSONObject();
        JSONObject stationData = new JSONObject();

        try {
            stationData.put("Date", date);
            stationData.put("Time", time);
            stationData.put("Visibility", visib);
            stationData.put("Cldc" , cldc);
            stationData.put("Prcp", prpc);

            station.put(stn,stationData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveJSON(station, stn);
    }

    public void jsonTest(JSONObject stationData, String stn){
        JSONObject station = new JSONObject();
        try {
            station.put(stn, stationData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveJSON(station, stn);
    }

    private void saveJSON(JSONObject station, String stn) {
        // check for one station how many times it has past this function (each 2 seconds)
        if(stn.equals("80001")){
             timeToWriteHistory = checkAppend();
        }

        // if passed e.g. 30 times timeToWriteHistory is true, write to separate history file
        // stays true untill the station has passed again
        if(timeToWriteHistory){
            appendHistory(station, stn);
            counter = 0;
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter("D:\\bullshit\\" + stn + ".json");
            bw = new BufferedWriter(fw);

            bw.write(station.toString());
            fw.write(station.toString());

            fw.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // increase or decrease counter either to increase/decrease the history writing speed
    private boolean checkAppend(){
        counter++;
        return counter == 29;
    }

    private void appendHistory(JSONObject station, String stn){
        File file = new File("D:\\bullshit\\History\\" + stn + ".json");
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(station.toString());
            fw.write(station.toString());
            bw.write("\r\n");
            fw.write("\r\n");

            fw.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
