import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Stations {

    private JSONObject countryStations = new JSONObject();
    private ArrayList<String> allStations = new ArrayList<>();

    // constructor
    public Stations(){
        readAllStations();
    }

    // first reads all countrynames and then creates array with all stations in EUU
    private void readAllStations(){
        ArrayList<String> allStations = getCountryList();

        for(String station: allStations){
            getCountryStation(station);
        }
    }

    // get all country names and add to arraylist
    private ArrayList<String> getCountryList(){
        ArrayList<String> countryList = new ArrayList<>();
        Scanner in = null;

        try {
            //in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\CountryList.txt"));
            in = new Scanner(new FileReader("/bin/applicatie/countries/CountryList.txt"));
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
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
            //in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\" + countryName + ".txt"));
            in = new Scanner(new FileReader("/bin/applicatie/countries/" + countryName + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNextLine()) {
            String line = in.nextLine();
            allStations.add(line);
        }

        in.close();
    }

    // gets all stations from txt file per String country
    public ArrayList<String> getTunisiaList(){
        ArrayList<String> stations = new ArrayList<>();
        Scanner in = null;

        try {
            //in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\Tunisia.txt"));
            in = new Scanner(new FileReader("/bin/applicatie/countries/Tunisia.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNextLine()) {
            String line = in.nextLine();
            stations.add(line);
        }

        in.close();

        return stations;
    }

    // public function to get the allStations arraylist
    public ArrayList<String> getEUStations(){
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
