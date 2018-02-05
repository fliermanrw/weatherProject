import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {
    private FileWriter fw = null;
    private BufferedWriter bw = null;

    public void writeOut(String stn, String date, String time, String visib) {
        try {
            time = reWriteDubbelePunt(time);
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

    private String reWriteDubbelePunt(String time){
        return time.replace(":","-");
    }


    // @TODO Arraylist // jsonobject met alle stations van een country die dan wordt weggeschreven
    public void writeOutPerCountry(String countryName, String station, String date, String time, String visib) {
        // testings
        countryName = "Netherlands";

        try {
            time = reWriteDubbelePunt(time);
            fw = new FileWriter("D:\\bullshit\\"+ countryName +"\\" + date + "--" + time + ".txt");
            bw = new BufferedWriter(fw);

            bw.write("STN: " + station + "\r\n");
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
