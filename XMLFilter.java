import org.json.simple.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.ArrayList;


public class XMLFilter extends DefaultHandler {
    private ArrayList<String> euStations = new ArrayList<>();
    private ArrayList<String> tunisiaStations = new ArrayList<>();
    private JSONObject stationData = new JSONObject();

    private writeJSON writeJSON;
    private XMLReader xmlReader;

    // vars for station information
    private String stnNmbr;

    // constructor of the XMLFilter
    public XMLFilter(ArrayList<String> allStations, ArrayList<String> tunisiaStations, writeJSON writeJSON) {
        super();
        try {
            //inherit from Client class (so no more extra classes started/resources used when new thread starts)
            this.euStations = allStations;
            this.tunisiaStations = tunisiaStations;
            this.writeJSON = writeJSON;

            xmlReader = XMLReaderFactory.createXMLReader();

        } catch (SAXException e) {
            //e.printStackTrace();
        }

        // defaultHandler
        xmlReader.setContentHandler(this);
        xmlReader.setErrorHandler(this);
    }

    // actual parser with/from default xml handler
    public void parseData(BufferedReader in) throws IOException {
        try {
            xmlReader.parse(new InputSource(in));
        } catch (SAXException e) {
            //e.printStackTrace();
        }
    }


    // bool for stn validation
    private boolean validEUStn = false;
    private boolean validTUNstn = false;

    // bools to go to character placement
    private boolean visibTun = false;
    private boolean stn = false;
    private boolean dateStn = false;
    private boolean timeStn = false;
    private boolean cldcStn = false;
    private boolean prcpStn= false;
    private boolean wndspdStn = false;

    // function from super, find a start element like <STN> ...
    public void startElement (String uri, String name, String qName, Attributes attributes) {
        // belangrijk voor de STN validatie!!!
        if (qName.equals("STN")) {
            stn = true;
        }

        //////////////////// ALS GEVALIDEERD \\\\\\\\\\\\\\\\\\\\\\\\\

        if (validTUNstn || validEUStn) {
            if (qName.equals("DATE")) {
                dateStn = true;
            }
            if (qName.equals("TIME")) {
                timeStn = true;
            }
        }

        if (validEUStn) {
            if (qName.equals("CLDC")) {
                cldcStn = true;
            }
        }

       if(validTUNstn) {
           if (qName.equals("VISIB")) {
               visibTun = true;
           }

           if (qName.equals("PRCP")) {
               prcpStn = true;
           }

           if(qName.equals("WDSP")){
               wndspdStn = true;
           }
       }
    }

    // function from super, find end element like .... </STN>
    public void endElement(String uri, String name, String qName) {
        if (qName.equals("STN")) {
            stn = false;
        }

        if (qName.equals("DATE")) {
            dateStn = false;
        }

        if (qName.equals("TIME")) {
            timeStn = false;
        }

        if (qName.equals("VISIB")) {
            visibTun = false;
        }

        if (qName.equals("CLDC")) {
            cldcStn = false;
        }

        if (qName.equals("PRCP")) {
            prcpStn = false;
        }

        if (qName.equals("WDSP")) {
            wndspdStn = false;
        }

        // as soon as all measurements are processed
        if (qName.equals("MEASUREMENT") && (validEUStn || validTUNstn)) {
            if (validEUStn) {
                synchronized (writeJSON) {
                    writeJSON.finallyWrite(stationData, stnNmbr);
                }
            } else {
                writeJSON.writeTunStation(stationData, stnNmbr);
            }

            // validStn to false -- ready for new checkStn
            validEUStn = false;
            validTUNstn = false;
            stationData = new JSONObject();
        }
    }

    // function from super, find characters between start and end element <STN> ...THIS... </STN>
    public void characters(char ch[], int start, int length) throws SAXException {
        // zet stn string in elkaar en dan door naar de checkStn voor validatie
        if(stn){
            stnNmbr = assembleChars(ch,start,length);
            checkStn(stnNmbr);
        }

        //////////////////// ALS GEVALIDEERD \\\\\\\\\\\\\\\\\\\\\\\\\

        if(dateStn){
            stationData.put("Date", assembleChars(ch,start,length));
        }

        if(timeStn){
            stationData.put("Time", assembleChars(ch,start,length));
        }

        if(visibTun){
            stationData.put("Visibility", assembleChars(ch,start,length));
        }

        if(cldcStn){
            stationData.put("cldc", assembleChars(ch,start,length));
        }

        if(prcpStn){
            stationData.put("prcp", assembleChars(ch,start,length));
        }

        if(wndspdStn){
            stationData.put("wdsp", assembleChars(ch,start,length));
        }
    }

    // Assembles the chars gotten from the inputstream and returns a String
    private String assembleChars(char ch[], int start, int length){
        StringBuilder strTemp = new StringBuilder();

        for (int i = start; i < start + length; i++) {
            strTemp.append(ch[i]);
        }

        return strTemp.toString();
    }

    //checks if STN is in either TUN or EU stn
    private void checkStn(String stn){
        if(tunisiaStations.contains(stn)){
            validTUNstn = true;
        } else if(euStations.contains(stn)){
            validEUStn = true;
        }

        if(validEUStn || validTUNstn){
            stationData.put("stn", stn);

        }
    }
}
