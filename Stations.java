import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Stations {

    JSONObject countryStations = new JSONObject();
    ArrayList<String> allStations = new ArrayList<>();

    // constructor
    public Stations(){
        readAllStations();
    }

    // first reads all countrynames and then creates array with all stations in EU
    private void readAllStations(){
        ArrayList<String> allStations = getCountryList();

        for(String station: allStations){
            getCountryStation(station);

            // insert JSON object country so the stations can be linked to the countryname later on
            try {
                countryStations.put(station,getCountryStationJSON(station));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // get all country names and add to arraylist
    private ArrayList<String> getCountryList(){
        ArrayList<String> countryList = new ArrayList<>();
        Scanner in = null;

        try {
            in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\CountryList.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNextLine()) {
            String line = in.nextLine();
            countryList.add(line);
        }
        in.close();

        return countryList;
    }

    // gets all stations from txt file per String country
    private void getCountryStation(String countryName){
        Scanner in = null;

        try {
            in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\" + countryName + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNextLine()) {
            String line = in.nextLine();
            allStations.add(line);
        }

        in.close();
    }

    private JSONArray getCountryStationJSON(String countryName){
        JSONArray jsonArray = new JSONArray();
        Scanner in = null;

        try {
            in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\" + countryName + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNextLine()) {
            String line = in.nextLine();
            jsonArray.put(line);
        }

        return jsonArray;
    }

    // public function to get the allStations arraylist
    public ArrayList<String> getAllStations(){
        if(allStations.isEmpty()){
            readAllStations();
        }

        return allStations;
    }

    // getter for the countries in JSON
    public JSONObject getCountryStations() {
        return countryStations;
    }
}
