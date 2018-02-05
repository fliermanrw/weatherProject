import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutputWriter {
    FileWriter fw = null;
    BufferedWriter bw = null;

    public void writeOut(String stn, String date, String time, String visib) {

        try {
            fw = new FileWriter("D:\\bullshit\\text" + stn + ".txt");
            bw = new BufferedWriter(fw);
            bw.write("STN: " + stn + "\r\n");
            bw.write("DATE: " + date + "\r\n");
            bw.write("TIME: " + time + "\r\n");
            bw.write("VISIB " + visib + "\r\n");

            bw.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
