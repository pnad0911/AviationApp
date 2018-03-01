import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Duy Pham
 * @version 1.01 - 05/23/2014
 * @email pnad0911@gmail.com
 * @prgm.usage A19015
 * @assignment.number A19015
 * @see <a href='http://jcouture.net/cisc190/A19015.php'>Program Specification</a>
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 */

public class A19015 extends JDialog {
    private static String strData = "WEATHERDATA";
    private static String strLOCATIONS = "LOCATIONS";
    private static ArrayList<String> ary = new ArrayList<String>();
    private static ArrayList<String> ary2 = new ArrayList<String>();
    private JPanel contentPane;
    private JButton createTheLOCATIONSTableButton;
    private JButton populateWithWindsAloftButton;
    private JButton createTheUSATxtButton;
    private JButton updateLOCATIONSWithTheButton;
    private JButton updateStationNamesFromButton;
    private JButton updateLOCATIONSWithTheButton1;
    private JButton createAReportFileButton;
    private JButton buttonOK;
    private JButton buttonCancel;

    public A19015() {
        setContentPane(contentPane);
        setTitle("A19015");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        createTheLOCATIONSTableButton.addActionListener(new createTheLOCATIONSTable());
        populateWithWindsAloftButton.addActionListener(new populateWithWindsAloft());
        updateStationNamesFromButton.addActionListener(new updateStationNamesFrom());
        createTheUSATxtButton.addActionListener(new createTheUSATxt());
        updateLOCATIONSWithTheButton.addActionListener(new updateLOCATIONSWithThe());
        updateLOCATIONSWithTheButton1.addActionListener(new updateLOCATIONSWithThe1());
        createAReportFileButton.addActionListener(new createReportFile());
    }

    public static void main(String[] args) throws Exception {

        //create a new database called WEATHERDATA
        DBMS dbms = new DBMS();
        dbms.openConnection(strData);
        //Delete any tables
        dbms.deleteAll(strData, strLOCATIONS);
        dbms.close();
        //System.out.println(stringSQL(strLOCATIONS,"data/schema.txt"));
        A19015 dialog = new A19015();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * Create stringSQL
     *
     * @param strTable    - table name
     * @param strFileName - file name
     * @return - String SQL
     * @throws Exception
     */
    public static String stringSQL(String strTable, String strFileName) throws Exception {
        INET net = new INET();
        String strPage;
        //check to see if the file exists on disk
        Boolean boTest = net.fileExist(strFileName);
        //if false
        if (boTest == false) {
            //download from the internet
            strPage = net.getURLRaw("http://faculty.sdmiramar.edu/jcouture/2014sp/cisc190/webct/manual/schema-locations.txt");
            // save
            net.saveToFile(strFileName, strPage);

        }
        String str = "";
        File myFile = new File(strFileName);
        if (myFile.exists()) {
            int intCount = 0;
            Scanner inputFile = new Scanner(myFile);
            //reading the schema FROM THE INTERNET which will in turn create an SQL command to create the table
            StringBuilder strTGD = new StringBuilder("CREATE TABLE " + strTable + " (");
            while (inputFile.hasNext()) {
                String strLine;
                strLine = inputFile.nextLine();
                if (intCount > 9) {
                    String[] tokens = strLine.split(" ");
                    strTGD.append(tokens[0] + " ");
                    int intChar = strLine.indexOf("char");
                    String strL = "";
                    if (intChar > 0) {
                        strL = strLine.substring(intChar, intChar + 8);
                    }
                    StringBuilder strL1 = new StringBuilder(strL);
                    if (strL.indexOf("l") > -1) {
                        strL1.deleteCharAt(strL.indexOf("l"));
                    }
                    strTGD.append(strL1.toString().toUpperCase());
                    strTGD.append(", ");
                }
                intCount++;
            }
            strTGD.replace(strTGD.length() - 2, strTGD.length() - 1, ")");
            str = strTGD.toString();
        }

        return str;
    }

    /**
     * Read the Winds Aloft data FROM THE INTERNET and populate your LOCATIONS table
     *
     * @param strFileName - file name
     * @throws Exception
     */
    private static void LoadWindsAloftData(String strFileName) throws Exception {
        String strVar = "";
        INET net = new INET();
        DBMS db = new DBMS();
        NWS nwsfb = new NWS(strVar);
        String strTest;
        String strPage;
        //check to see if the file exists on disk
        Boolean boTest = net.fileExist(strFileName);
        if (boTest == false) {
            //download the Winds Aloft data from the NWS and save it
            strPage = net.getURLRaw("http://www.aviationweather.gov/products/nws/all");
            //extract the winds aloft data from the web page
            strTest = net.getPREData(strPage);
            net.saveToFile(strFileName, strTest);

        }
        File myFile = new File(strFileName);
        if (myFile.exists()) {
            String strLine;
            Scanner inputFile = new Scanner(myFile);
            int intCount = 0;
            db.openConnection(strData);
            while (inputFile.hasNext()) {
                //read the file line by line
                strLine = inputFile.nextLine();
                if (intCount > 6) {
                    //add station ID
                    db.addRecord(strLOCATIONS, "stationid", nwsfb.getStationID(strLine));
                    //update the "windsaloft" field
                    db.setField(strLOCATIONS, "stationid", nwsfb.getStationID(strLine), "windsaloft", strLine);
                    db.close();
                }
                intCount++;
            }
        }
    }

    /**
     * a method called LoadWorldData(strFileName) that will check to see
     * if the world.txt file exists on disk using your INET object
     * update the station name, state
     *
     * @param strFileName - world.txt
     * @throws Exception
     */
    private static void LoadWorldData(String strFileName) throws Exception {
        INET net = new INET();
        DBMS dbms = new DBMS();
        String strPage;
        //check to see if the WORLD.TXT file exists on disk
        Boolean boTest = net.fileExist(strFileName);
        if (boTest == false) {
            //download it and save it
            strPage = net.getURLRaw("http://weather.noaa.gov/data/nsd_bbsss.txt");
            net.saveToFile(strFileName, strPage);
        }
        File myFile = new File(strFileName);
        if (myFile.exists()) {
            String strLine;
            Scanner inputFile = new Scanner(myFile);
            dbms.openConnection(strData);
            while (inputFile.hasNext()) {
                //read the file one record at a time
                strLine = inputFile.nextLine();
                try {
                    String[] tokens = strLine.split(";");
                    String strKid = tokens[2].substring(1, 4);
                    //check to see if the station exists in the database
                    if (dbms.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid  = '" + strKid + "'") == true) {
                        while (dbms.moreRecords()) {
                            int intE = tokens[3].indexOf("'");
                            while (intE > 0) {
                                StringBuilder str = new StringBuilder(tokens[3]);
                                str.replace(intE, intE + 1, ",");
                                intE = str.indexOf("'", intE + 1);
                                tokens[3] = str.toString();
                            }
                            //update the station name, state
                            ary.add(tokens[2].trim());
                            ary2.add(tokens[2].substring(1, 4));
                            //Station Name is in proper case
                            dbms.setField(strLOCATIONS, "stationid", strKid, "stationname", net.properCase(tokens[3]));
                            dbms.setField(strLOCATIONS, "stationid", strKid, "state", tokens[4]);
                        }
                        dbms.close();
                    }

                } catch (Exception q) {

                }
            }
        }
    }

    /**
     * update lattitude,longitude,altitude
     *
     * @param strFileName - file name
     * @throws Exception
     */
    private static void LatLongAltdata(String strFileName) throws Exception {
        DBMS dbms = new DBMS();
        File myFile = new File(strFileName);
        if (myFile.exists()) {
            String strLine;
            Scanner inputFile = new Scanner(myFile);
            dbms.openConnection(strData);
            while (inputFile.hasNext()) {
                //read the file one record at a time
                strLine = inputFile.nextLine();

                try {
                    String[] tokens = strLine.split(";");
                    String strKid = tokens[2].substring(1, 4);
                    //check to see if the station exists in the database
                    if (dbms.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid  = '" + strKid + "'") == true) {
                        while (dbms.moreRecords()) {
                            // latitude, longitude and elevation fields
                            dbms.setField(strLOCATIONS, "stationid", strKid, "latitude", tokens[7]);
                            dbms.setField(strLOCATIONS, "stationid", strKid, "longitude", tokens[8]);
                            dbms.setField(strLOCATIONS, "stationid", strKid, "elevation", tokens[11]);
                        }
                        dbms.close();
                    }

                } catch (Exception q) {

                }
            }
        }
    }

    /**
     * a method called UpdtDatabase() that will call UpdateStationInfo() for each station
     * from the database that is in California
     */
    private static void UpdtDatabase() {
        DBMS db = new DBMS();
        db.openConnection(strData);
        //stations in CA
        db.query("SELECT * FROM " + strLOCATIONS + " WHERE state  = '" + "CA" + "'");
        while (db.moreRecords()) {
            String strS = db.getField("stationid");
            UpdateStationInfo(strS, true);
        }

    }

    /**
     * a method called UpdateStationInfo(strStationID,blnUpdate) that will accept the station id and
     * a boolean value as parameters
     * update the form fields and download the weather condition image
     * and update the image control on the form.
     *
     * @param strStationID - station ID
     * @param blnUpdate    - true or false
     */
    private static void UpdateStationInfo(String strStationID, Boolean blnUpdate) {
        DBMS db = new DBMS();
        db.openConnection(strData);
        //blnUpdate is true
        if (blnUpdate == true) {
            try {
                XMLRead xml = new XMLRead();
                xml.loadPage("http://w1.weather.gov/xml/current_obs/K" + strStationID.trim() + ".xml");
                db.setField(strLOCATIONS, "stationid", strStationID, "pressure", xml.getField("pressure_mb"));
                db.setField(strLOCATIONS, "stationid", strStationID, "temperature", xml.getField("temp_c"));
                db.setField(strLOCATIONS, "stationid", strStationID, "dewpoint", xml.getField("dewpoint_f"));
                db.setField(strLOCATIONS, "stationid", strStationID, "humidity", xml.getField("relative_humidity"));
                db.setField(strLOCATIONS, "stationid", strStationID, "windspeed", xml.getField("wind_mph"));
                //db.setField(strLOCATIONS, "stationid", strStationID, "winddirection", xml.getField("wind_dir"));

            } catch (Exception r) {
                db.setField(strLOCATIONS, "stationid", strStationID, "pressure", "");
                db.setField(strLOCATIONS, "stationid", strStationID, "temperature", "");
                db.setField(strLOCATIONS, "stationid", strStationID, "dewpoint", "");
                db.setField(strLOCATIONS, "stationid", strStationID, "humidity", "");
                db.setField(strLOCATIONS, "stationid", strStationID, "windspeed", "");
                //db.setField(strLOCATIONS, "stationid", strStationID, "winddirection", "");
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class createTheLOCATIONSTable implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DBMS dbms = new DBMS();
            dbms.openConnection(strData);
            try {
                dbms.execute(stringSQL(strLOCATIONS, "data/schema.txt"));
            } catch (Exception s) {

            }

            dbms.close();
        }
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class populateWithWindsAloft implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DBMS dbms = new DBMS();
            dbms.openConnection(strData);
            try {
                LoadWindsAloftData("data/FBIN.txt");
            } catch (Exception s) {

            }

            dbms.close();
        }
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class updateStationNamesFrom implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try {
                LoadWorldData("data/world.txt");
            } catch (Exception s) {

            }


        }
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class createTheUSATxt implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try {
                File myFile = new File("data/world.txt");
                if (myFile.exists()) {
                    String strLine;
                    PrintWriter outputFile = new PrintWriter("data/USA.txt");
                    Scanner inputFile = new Scanner(myFile);
                    //Open and Read the WORLD.TXT file one record at a time in a loop
                    while (inputFile.hasNext()) {
                        strLine = inputFile.nextLine();
                        String[] tokens = strLine.split(";");
                        //if have a station ID (technically it is the ICAO Location Indicator) that starts with the letter "K"
                        if (tokens[2].substring(0, 1).equals("K")) {
                            outputFile.println(strLine);
                        }
                    }
                    outputFile.close();
                }
            } catch (Exception s) {

            }
        }
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class updateLOCATIONSWithThe implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try {
                LatLongAltdata("data/USA.txt");
            } catch (Exception s) {

            }
        }
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class updateLOCATIONSWithThe1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try {
                UpdtDatabase();
            } catch (Exception s) {

            }
        }
    }

    /**
     * Public inner class that handle the event when
     * the user clicks a buttons
     */
    public class createReportFile implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int intCount = 0;
            NWS nws = new NWS("");
            //check to see if file exist
            File myFile = new File("data/FBIN.txt");
            if (myFile.exists()) {
                Scanner input = null;
                try {
                    // write a file to disk using the filename
                    input = new Scanner(myFile);
                } catch (Exception egg) {
                    egg.printStackTrace();
                }
                File file = new File("data/OUTPUT.txt");
                PrintWriter output = null;
                try {
                    // write a file to disk using the filename
                    output = new PrintWriter(file);
                } catch (Exception egg) {
                    egg.printStackTrace();
                }
                //write the header record
                output.println("ICAO,Station Name, State, Latitude, Longitude, Altitude, Winds Aloft,Surface TempC, Surface Humidity, WindDir, WindSp, WindTemp");
                while (input.hasNext()) {
                    String strLine = input.nextLine();
                    if (intCount > 6) {
                        //data for each location to a file
                        if (ary2.indexOf(strLine.substring(0, 3)) >= 0) {
                            int intF = ary2.indexOf(strLine.substring(0, 3));
                            output.print(ary.get(intF) + ",");
                            DBMS db = new DBMS();
                            db.openConnection(strData);
                            db.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + ary2.get(intF) + "'");
                            while (db.moreRecords()) {
                                output.print(db.getField("stationname").trim() + "," + db.getField("state").trim() + ",");
                            }
                            db.close();
                        } else {
                            output.print(",,,");
                        }
                        DBMS db1 = new DBMS();
                        db1.openConnection(strData);
                        db1.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + strLine.substring(0, 3) + "'");
                        while (db1.moreRecords()) {
                            try {
                                output.print(db1.getField("latitude").trim() + ",");
                            } catch (Exception i) {
                                output.print(",");
                            }
                        }
                        db1.close();
                        DBMS db2 = new DBMS();
                        db2.openConnection(strData);
                        db2.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + strLine.substring(0, 3) + "'");
                        while (db2.moreRecords()) {
                            try {
                                output.print(db2.getField("longitude").trim() + ",");
                            } catch (Exception i) {
                                output.print(",");
                            }
                        }
                        db2.close();
                        DBMS db3 = new DBMS();
                        db3.openConnection(strData);
                        db3.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + strLine.substring(0, 3) + "'");
                        while (db3.moreRecords()) {
                            try {
                                output.print(db3.getField("elevation").trim() + ",");
                            } catch (Exception i) {
                                output.print(",");
                            }
                        }
                        db3.close();
                        DBMS db4 = new DBMS();
                        db4.openConnection(strData);
                        db4.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + strLine.substring(0, 3) + "'");
                        while (db4.moreRecords()) {
                            try {
                                output.print(db4.getField("windsaloft").trim() + ",");
                            } catch (Exception i) {
                                output.print(",");
                            }
                        }
                        db4.close();
                        DBMS db5 = new DBMS();
                        db5.openConnection(strData);
                        db5.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + strLine.substring(0, 3) + "'");
                        while (db5.moreRecords()) {
                            try {
                                output.print(db5.getField("temperature").trim() + ",");
                            } catch (Exception i) {
                                output.print(",");
                            }
                        }
                        db5.close();
                        DBMS db6 = new DBMS();
                        db6.openConnection(strData);
                        db6.query("SELECT * FROM " + strLOCATIONS + " WHERE stationid ='" + strLine.substring(0, 3) + "'");
                        while (db6.moreRecords()) {
                            try {
                                output.print(db6.getField("humidity").trim() + ",");
                            } catch (Exception i) {
                                output.print(",");
                            }
                        }
                        db6.close();
                        //Extract the data at the 30,000 foot level for that station
                        output.print(nws.getWindDirection(strLine, "30000") + "," + nws.getWindSpeed(strLine, "30000") + "," + nws.getWindTemp(strLine, "30000"));
                        output.println("");
                    }
                    intCount++;
                }
                output.close();
            }
        }
    }
}
