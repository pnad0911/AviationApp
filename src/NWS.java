/**
 * @version 1.01 - 05/23/2014
 * @author Duy Pham
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 * @email pnad0911@gmail.com
 */
public class NWS
{
    private String strWeather;

    /**
     * Constructor
     * @param strVar =strWeather
     */
    public NWS(String strVar)
    {
        strWeather=strVar;
    }

    public  String getStationID(String strLine)
    {
        return strLine.substring(0,3);
    }



    /**
     * a method called getPos(strAlt)
     * @param strAlt - string representing the altitude
     * @return  returns an integer representing a position in the string
     */
    public int getPos(String strAlt)
    {
        // Declare my variables
        int intRet=0;
        //convert my string to an integer
        int intAlt=Integer.parseInt(strAlt);
        //using the switch statement
        //figure out the position
        switch (intAlt)
        {
            case 3000:
                intRet = 4;
                break;
            case 6000:
                intRet = 9;
                break;
            case 9000:
                intRet = 17;
                break;
            case 12000:
                intRet = 25;
                break;
            case 18000:
                intRet = 33;
                break;
            case 24000:
                intRet = 41;
                break;
            case 30000:
                intRet= 49;
                break;
            case 34000:
                intRet = 56;
                break;
            case 39000:
                intRet = 63;
                break;
        }
        return intRet;
    }

    /**
     * a method called getAltitudeWeather(String strAlt,String strZ)
     * @param strAlt - String
     * @param strZ - string representing the altitude
     * @return  returns an integer representing a position in the string
     */
    public String getAltitudeWeather(String strAlt,String strZ)
    {
        // get the position in the station weather string
        int intPos = getPos(strZ);
        if (strZ.equals("3000"))
        {
            //contains a four character string
            strAlt = strAlt.substring(4,8);
        }else if (strZ.equals("30000") | strZ.equals("34000") | strZ.equals("39000"))
        {
            //contains a six character string
            strAlt = strAlt.substring(intPos,intPos+6);

        }else
        {
            //contains a seven character string
            strAlt = strAlt.substring(intPos,intPos+7);

        }
        //// Return the string to the caller
        return strAlt;
    }

    /**
     * method called getWindDirection(String strLine,String strAlt)
     * @param strLine - String
     * @param strAlt - string representing the altitude
     * @return returns the wind direction
     */
    public String getWindDirection(String strLine,String strAlt)
    {
        // Declare Variables
        String strRet = "";
        // Get the Altitude Weather
        String strAltWea =getAltitudeWeather(strLine, strAlt);
        if (strAltWea.substring(0,2).equals("  "))
        {
            // the first two characters are blanks
            strRet = "N/A";
        }
        else
        {
            //first four characters "9900"?
            if (strAltWea.substring(0,4).equals("9900"))
            {
                strRet="Calm";
            }
            else
            {
                int intAltWea=Integer.parseInt(strAltWea.substring(0,2));
                //first two characters > 36?
                if (intAltWea>36)
                {
                    intAltWea=intAltWea-50;
                    strRet=Integer.toString(intAltWea)+"0";
                }
                else
                {
                    //return the first two characters with a "0" suffix
                    strRet=strAltWea.substring(0,2)+"0";
                }
            }
        }
        //// Return the string to the caller
        return strRet;
    }

    /**
     * method called getWindSpeed(String strLine,String strAlt)
     * @param strLine - String
     * @param strAlt - string representing the altitude
     * @return returns the wind speed
     */
    public String getWindSpeed(String strLine,String strAlt)
    {
        //declare varibale
        String strRet="";
        //Get the Altitude Weather
        String strAltWea=getAltitudeWeather(strLine,strAlt);
        //the third and fourth characters of the altitude weather are blanks
        if (strAltWea.substring(2,4).equals("  "))
        {
            strRet="N/A";
        }
        else
        {
            // the first four characters of the altitude weather are "9900"
            if (strAltWea.substring(0,4).equals("9900"))
            {
                strRet="Calm";
            }
            else
            {
                // the first two characters of the Altitude Weather are greater then 36
                int intAltWea=Integer.parseInt(strAltWea.substring(0,2));
                if (intAltWea>36)
                {
                    int intSpeed=Integer.parseInt(strAltWea.substring(2,4));
                    // add 100
                    intSpeed=intSpeed+100;
                    strRet=Integer.toString(intSpeed);
                }
                else
                {
                    //// return the second two characters of the altitude weather
                    strRet=strAltWea.substring(2,4);
                }
            }
        }
        return strRet;
    }

    /**
     * method called getWindTemp(String strLine,String strAlt)
     * @param strLine - String
     * @param strAlt - string representing the altitude
     * @return returns the wind temperature
     */
    public String getWindTemp(String strLine,String strAlt)
    {
        String strRet="";
        String strAltWea=getAltitudeWeather(strLine,strAlt);
        if (strAlt.equals("3000"))
        {
            strRet="N/A";
        }
        else
        {
            // the fifth character is a blank, return "N/A"
            if (strAltWea.substring(4,5).equals(" "))
            {
                strRet="N/A";
            }
            else if (strAlt.equals("6000")|strAlt.equals("9000")|strAlt.equals("12000")|strAlt.equals("18000")|strAlt.equals("24000"))
            {
                //the last three
                strRet=strAltWea.substring(4,7);
            }else
            {
                // the altitude is over 24000 feet, add a "-" as a prefix
                strRet="-"+strAltWea.substring(4,6);
            }
        }
        return strRet;
    }
}
