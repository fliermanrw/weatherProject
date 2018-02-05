// used https://www.journaldev.com/1191/java-stax-parser-example-read-xml-file

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.stream.XMLInputFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLFilter extends DefaultHandler {

    // hardcoded list of stations to filter (EU and Tunesia) @TODO Filter on EU en Tunesie apart // CSV oid inladen?
    //List<Integer> stationsEU = Arrays.asList(722480,104380,122320,81750,132230,710774,1315,130240,1223,123111,722489,167180,2130,1234,6565,3433,3566,3254,422,633,56,34,6733,5646,4234,5465,2354,653,12121,1213343,3,434,123,343);
    List<Integer> stationsTun = Arrays.asList(607140,607100,607150, 607200, 607250, 607280,607340,607350,607380,607400,607401,607403,607450,607480,60750,60760,607650,607690,607700,607750);

    Stations stations = new Stations();


    XMLReader xmlReader;

    // gebruikt om de stations te bekijken
    String stnNmbr;
    String visibNmbr;
    String date;
    String time;

    OutputWriter outputWriter = new OutputWriter();
    JSONWriter jsonWriter = new JSONWriter();

    public XMLFilter(){
        super();
        try {
            xmlReader = XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        // defaulthandler de errors laten verwerken
        xmlReader.setContentHandler(this);
        xmlReader.setErrorHandler(this);
        ///

    }

    public void parseData(BufferedReader in) throws IOException {
        try {
            xmlReader.parse(new InputSource(in));
            ArrayList<String> stationsEU = stations.getCountryList();
            System.out.println(stationsEU);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    //////////////////////

    /*// niet per se nodig ; geeft einde aan
    public void startDocument(){
        System.out.println("Begin..");
    }

    // niet per se nodig ; geeft einde aan
    public void endDocument(){
        System.out.println("Einde..");
    }*/

    // bools to go to character placement
    private boolean visibTun = false;
    private boolean stn = false;
    private boolean validStn = false;
    private boolean dateStn = false;
    private boolean timeStn = false;

    //checks if stn is in stations list
    public void checkStn(String stn){
        if(stationsTun.contains(Integer.valueOf(stn))){
            validStn = true;
            System.out.println("TRUE");
        }
    }

    // function from super
    public void startElement (String uri, String name, String qName, Attributes attributes){
        // belangrijk voor de STN validatie!!
        if(qName.equals("STN")){
            stn = true;
        }

        //////////////////// ALS GEVALIDEERD \\\\\\\\\\\\\\\\\\\\\\\\

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

    // function from super
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
        if(qName.equals("MEASUREMENT")){
            if(validStn){
                outputWriter.writeOut(stnNmbr, date, time, visibNmbr);
                jsonWriter.writeJSON(stnNmbr, date, time, visibNmbr);
            }

            // validStn op false -- klaar voor nieuw checkStn
            validStn = false;

        }
    }

    // function from super
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

    // more efficient than using function over and over again
    private String assembleChars(char ch[], int start, int length){
        StringBuilder strTemp = new StringBuilder();

        for (int i = start; i < start + length; i++) {
            strTemp.append(ch[i]);
        }

        return strTemp.toString();
    }





    /*public void filterData(BufferedReader in) throws IOException {
        try {
            XMLEventReader eventReader = inputFactory.createXMLEventReader(new BufferedReader(in));
            System.out.println();

            while(in.readLine() != null){
                XMLEvent xmlEvent = eventReader.nextEvent();
                if(xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if(startElement.getName().getLocalPart().equals("STN")){
                        System.out.println(startElement.toString());

                    }
                }
            }

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }*/
}
