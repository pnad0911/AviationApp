/**
 * @version 1.01 - 05/23/2014
 * @author Duy Pham
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 * @email pnad0911@gmail.com
 */
public interface INETTemplate
{
    /**
     * Accept a full file path and name and determine if it exists on disk.
     * If it does, return a true. If it does not exist, return a false.
     * @param strFileName -
     * @return
     */
    public Boolean fileExist(String strFileName);

    /**
     * * Accept a full file path and name as a parameter and
     * check to see if it exists using the fileExists method.
     * If it does exist, read the file from disk and return the contents as a string.
     * If the file does not exist, return a null.
     * @param strFileName - a full file path and name
     * @return - Contents from the file as a string.
     * @throws java.lang.Exception
     */
    java.lang.String getFromFile(java.lang.String strFileName)
            throws java.lang.Exception;

    /**
     * Accept an HTML web page as a string and
     * extract the data that is between the PRE tags using the getRegEx function.
     * If nothing is found, return a null.
     * @param strPage - a normal HTML page as a string
     * @return - Extracted Contents - only the data between two PRE tags or null.
     */
    public String getPREData(java.lang.String strPage);

    /**
     * Extract a small string from a larger one using a Regular Expression and
     * the pattern specified as a parameter.
     * If nothing is found, return a null.
     * @param strInput - large string
     * @param strPattern - formatted Regular Expression
     * @return - a small string that is the result of the Reg Ex search
     */
    public String getRegEx(java.lang.String strInput, java.lang.String strPattern);


    /**
     *  Accept a string URL, create a string using the StringBuilder class and
     * return it as one long string. If the page does not exist, return a null.
     * @param strURL - URL as a string
     * @return -  the entire web page as a string
     * @throws java.lang.Exception
     */
    java.lang.String getURLRaw(java.lang.String strURL)
            throws java.lang.Exception;

    /**
     * Accept a string and convert the whole string into lower case then
     * upper case only the first letter of each word.
     *
     *
     * @param strInput - string to be converted
     * @return - string with every word's first letter capitalized
     */
    public String properCase(String strInput);


    /**
     * Accept a string and save it to a local file
     * @param strFileName - a full file path and name.
     * @param strContent - string to be saved to disk.
     * @throws java.lang.Exception
     */
    void saveToFile(java.lang.String strFileName,
                    java.lang.String strContent)
            throws java.lang.Exception;


}
