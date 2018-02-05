import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Stations {

    public ArrayList<String> getCountryList(){
        System.out.println("ik word aangeroepen");
        ArrayList<String> countryList = new ArrayList<>();
        Scanner in = null;

        try {
            in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\CountryList.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNextLine()) {
            countryList.add(in.toString());
        }
        in.close();

        System.out.println(countryList);
        return countryList;
    }

    public void getCountryStation(String countryName){
        StringBuilder sb = new StringBuilder();
        Scanner in = null;

        try {
            in = new Scanner(new FileReader("C:\\Users\\Ryan\\IdeaProjects\\ProjectWeatherData\\src\\countries\\" + countryName + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNext()) {
            sb.append(in.next());
        }
        in.close();


        String outString = sb.toString();
        System.out.println(outString);
    }
}
