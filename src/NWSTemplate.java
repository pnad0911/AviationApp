/**
 * @author Duy Pham
 * @version 1.01 - 05/23/2014
 * @email pnad0911@gmail.com
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 */
public interface NWSTemplate
{
    /**
     * a method called getPos(strAlt)
     * @param strAlt - string representing the altitude
     * @return  returns an integer representing a position in the string
     */
    public int getPos(String strAlt);

    /**
     * a method called getAltitudeWeather(String strAlt,String strZ)
     * @param strAlt - String
     * @param strZ - string representing the altitude
     * @return  returns an integer representing a position in the string
     */
    public String getAltitudeWeather(String strAlt,String strZ);

    /**
     * method called getWindDirection(String strLine,String strAlt)
     * @param strLine - String
     * @param strAlt - string representing the altitude
     * @return returns the wind direction
     */
    public String getWindDirection(String strLine,String strAlt);

    /**
     * method called getWindSpeed(String strLine,String strAlt)
     * @param strLine - String
     * @param strAlt - string representing the altitude
     * @return returns the wind speed
     */
    public String getWindSpeed(String strLine,String strAlt);

    /**
     * method called getWindTemp(String strLine,String strAlt)
     * @param strLine - String
     * @param strAlt - string representing the altitude
     * @return returns the wind temperature
     */
    public String getWindTemp(String strLine,String strAlt);
}
