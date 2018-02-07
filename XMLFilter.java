// used https://www.journaldev.com/1191/java-stax-parser-example-read-xml-file

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamConstants;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

public class XMLFilter extends DefaultHandler {
    private ArrayList<String> allStations = new ArrayList<>();
    private JSONObject stationData = new JSONObject();

    private writeJSON writeJSON;
    private SAXParser saxParser;

    // vars for station information
    private String stnNmbr;

    private DefaultHandler dh = new DefaultHandler();


    // constructor of the XMLFilter
    // Will be a writeJSON instead of outputWriter, I guess
    public XMLFilter(ArrayList<String> allStations, writeJSON writeJSON) {
        super();
        try {
            //inherit from Client class (so no more extra classes started/resources used when new thread starts)
            this.allStations = allStations;
            this.writeJSON = writeJSON;

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParser = saxParserFactory.newSAXParser();

        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    // actual parser default handler
    public void parseData(BufferedReader in) throws IOException {
        try {
            saxParser.parse(new InputSource(in),dh);

        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    // bool for stn validation
    private boolean validStn = false;

    // bools to go to character placement
    private boolean visibTun = false;
    private boolean stn = false;
    private boolean dateStn = false;
    private boolean timeStn = false;
    private boolean cldcStn = false;
    private boolean prcpStn= false;

    // function from super, find a start element like <STN> ...
    public void startElement (String uri, String name, String qName, Attributes attributes){
        // belangrijk voor de STN validatie!!!
        if(qName.equalsIgnoreCase("STN")){
            System.out.println("PRINT");
            stn = true;
        }

        //////////////////// ALS GEVALIDEERD \\\\\\\\\\\\\\\\\\\\\\\\\

        if(validStn){
            if(qName.equals("VISIB")){
                visibTun = true;
            }
            if(qName.equals("DATE")){
                dateStn = true;
            }
            if(qName.equals("TIME")){
                timeStn = true;
            }
            if(qName.equals("VISIB")){
                visibTun = true;
            }
            if(qName.equals("CLDC")){
                cldcStn = true;
            }
            if(qName.equals("PRCP")){
                prcpStn = true;
            }
        }
    }

    // function from super, find end element like .... </STN>
    public void endElement(String uri, String name, String qName){
        if(qName.equals("STN")){
            stn = false;
        }

        if(qName.equals("DATE")){
            dateStn = false;
        }

        if(qName.equals("TIME")){
            timeStn = false;
        }

        if(qName.equals("VISIB")){
            visibTun = false;
        }

        if(qName.equals("CLDC")){
            cldcStn = false;
        }

        if(qName.equals("PRCP")){
            prcpStn = false;
        }

        // as soon as all measurements are processed
        if(qName.equals("MEASUREMENT") && validStn){
            writeJSON.jsonTest(stationData, stnNmbr);

            // validStn op false -- klaar voor nieuw checkStn
            validStn = false;
            stationData = new JSONObject();
        }
    }

    // function from super, find characters between start and end element <STN> ...THIS... </STN>
    public void characters(char ch[], int start, int length) throws SAXException {
        String temp;
        // zet stn string in elkaar en dan door naar de checkStn voor validatie
        if(stn){
            stnNmbr = assembleChars(ch,start,length);
            checkStn(stnNmbr);
        }

        try {
            if(dateStn){
                temp = assembleChars(ch,start,length);
                stationData.put("Date", temp);
                //date = assembleChars(ch,start,length);
            }

            if(timeStn){
                temp = assembleChars(ch,start,length);
                stationData.put("Time", temp);
                //time = assembleChars(ch,start,length);
            }

            if(visibTun){
                temp = assembleChars(ch,start,length);
                stationData.put("Visibility", temp);
                //visibNmbr = assembleChars(ch,start,length);
            }

            if(cldcStn){
                temp = assembleChars(ch,start,length);
                stationData.put("cldc", temp);
                //cldc = assembleChars(ch,start,length);
            }

            if(prcpStn){
                temp = assembleChars(ch,start,length);
                stationData.put("prcp", temp);
                //prcp = assembleChars(ch,start,length);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    //checks if STN is in stationslist
    private void checkStn(String stn){
        if(allStations.contains(stn)){
            validStn = true;
        }
    }



}
