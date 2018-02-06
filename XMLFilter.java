// used https://www.journaldev.com/1191/java-stax-parser-example-read-xml-file

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLFilter extends DefaultHandler {
    // TEMP hardcoded list of stations in TUN
    List<Integer> stationsTun = Arrays.asList(607140,607100,607150, 607200, 607250, 607280,607340,607350,607380,607400,607401,607403,607450,607480,60750,60760,607650,607690,607700,607750);
    private ArrayList<String> allStations = new ArrayList<>();


    private XMLReader xmlReader;
    //private OutputWriter outputWriter;
    private writeJSON writeJSON;


    // vars for station information
    private String stnNmbr;
    private String visibNmbr;
    private String date;
    private String time;


    // constructor of the XMLFilter
    // Will be a writeJSON instead of outputWriter, I guess
    public XMLFilter(ArrayList<String> allStations, writeJSON writeJSON){
        super();
        try {
            //inherit from Client class (so no more extra classes started/resources used when new thread starts)
            this.allStations = allStations;
            this.writeJSON = writeJSON;

            xmlReader = XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        // defaulthandler de errors laten verwerken
        xmlReader.setContentHandler(this);
        xmlReader.setErrorHandler(this);
    }

    // actual parser default handler
    public void parseData(BufferedReader in) throws IOException {
        try {
            xmlReader.parse(new InputSource(in));

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

    // function from super, find a start element like <STN> ...
    public void startElement (String uri, String name, String qName, Attributes attributes){
        // belangrijk voor de STN validatie!!!
        if(qName.equals("STN")){
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

        // as soon as all measurements are processed
        if(qName.equals("MEASUREMENT") && validStn){
            //outputWriter.writeOut(stnNmbr, date, time, visibNmbr);
            //outputWriter.writeOutPerCountry("Netherlands",stnNmbr,date,time,visibNmbr);
            writeJSON.writeToJSON(stnNmbr, date, time, visibNmbr);

            // validStn op false -- klaar voor nieuw checkStn
            validStn = false;
        }
    }

    // function from super, find characters between start and end element <STN> ...THIS... </STN>
    public void characters(char ch[], int start, int length) throws SAXException {
        // zet stn string in elkaar en dan door naar de checkStn voor validatie
        if(stn){
            stnNmbr = assembleChars(ch,start,length);
            checkStn(stnNmbr);
        }

        if(dateStn){
            date = assembleChars(ch,start,length);
        }

        if(timeStn){
            time = assembleChars(ch,start,length);
        }

        if(visibTun){
            visibNmbr = assembleChars(ch,start,length);
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
            //System.out.println("TRUE");

        }
    }


}
