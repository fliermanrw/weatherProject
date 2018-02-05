import org.json.JSONException;
import org.json.JSONObject;

public class JSONWriter {

    public void writeJSON(String stn, String date, String time, String visib){
        JSONObject stations = new JSONObject();
        JSONObject stationData = new JSONObject();

        try {
            stationData.put("Date: ", date);
            stationData.put("Time: ", time);
            stationData.put("Visibility: ", visib);

            stations.put(stn,stationData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(stations);
    }
}
