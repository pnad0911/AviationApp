import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.01 - 05/23/2014
 * @author Duy Pham
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 * @email pnad0911@gmail.com
 */
public class INET implements INETTemplate
{

    /**
     * Accept a full file path and name and determine if it exists on disk.
     * If it does, return a true. If it does not exist, return a false.
     *
     * @param strFileName -
     * @return
     */
    @Override
    public Boolean fileExist(String strFileName)
    {
        Boolean bo;
        File f = new File(strFileName);
        if(f.exists())
        {
            bo=true;
        }
        else
        {
            bo=false;
        }
        return bo;
    }

    /**
     * * Accept a full file path and name as a parameter and
     * check to see if it exists using the fileExists method.
     * If it does exist, read the file from disk and return the contents as a string.
     * If the file does not exist, return a null.
     *
     * @param strFileName - a full file path and name
     * @return - Contents from the file as a string.
     * @throws Exception
     */
    @Override
    public String getFromFile(String strFileName) throws Exception
    {
        ArrayList<String> list = new ArrayList<String>();
        String strLine;
        String strRe="";
        if(fileExist(strFileName) == true)
        {
            BufferedReader inputFile;
            inputFile = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(strFileName)));
            while(inputFile.ready())
            {
                strLine = inputFile.readLine();
                list.add(strLine);
            }
            for (String s : list)
            {
                strRe += s + "\n";
            }
        }
        else
        {
            strRe="File does not exist";
        }
        return strRe;
    }

    /**
     * Accept an HTML web page as a string and
     * extract the data that is between the PRE tags using the getRegEx function.
     * If nothing is found, return a null.
     *
     * @param strPage - a normal HTML page as a string
     * @return - Extracted Contents - only the data between two PRE tags or null.
     */
    @Override
    public String getPREData(String strPage)
    {
        String strS=getRegEx(strPage,"<PRE>(.)*</PRE>");
        return strS.substring(strS.indexOf('\n')+1,strS.lastIndexOf('\n'));
    }

    /**
     * Extract a small string from a larger one using a Regular Expression and
     * the pattern specified as a parameter.
     * If nothing is found, return a null.
     *
     * @param strInput   - large string
     * @param strPattern - formatted Regular Expression
     * @return - a small string that is the result of the Reg Ex search
     */
    @Override
    public String getRegEx(String strInput, String strPattern)
    {
        String strRet = "";
        Pattern pattern = Pattern.compile(strPattern,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(strInput);

        while (matcher.find())
        {
            strRet = strRet + "\n" + matcher.group();
        }
        if (strRet.length() < 1 )
        {
            strRet = "String Not Found";
        }
        return strRet.trim();
    }

    /**
     * Accept a string URL, create a string using the StringBuilder class and
     * return it as one long string. If the page does not exist, return a null.
     *
     * @param strURL - URL as a string
     * @return -  the entire web page as a string
     * @throws Exception
     */
    @Override
    public String getURLRaw(String strURL) throws Exception
    {
        StringBuilder stbContent = new StringBuilder("");
        try
        {
            URL myWebAddress= new URL(strURL);
            URLConnection myConn = myWebAddress.openConnection();
            InputStream myStrIn = myConn.getInputStream();
            BufferedReader inputFile = new BufferedReader(new InputStreamReader(myStrIn));
            while(inputFile.ready())
            {
                // read a line and append it to strContent
                String strRecord = inputFile.readLine() + "\r\n";
                stbContent.append(strRecord);
            }
        }
        catch (MalformedURLException errnum)
        {
            // display error if URL is messed up
            System.out.println(errnum.getMessage());
        }
        catch (IOException errnum)
        {
            // display error if Internet connection is flaky
            System.out.println(errnum.getMessage());
        }
        // At this point strContent contains the
        //    raw HTML code of your web page
        //    or just a blank
        return stbContent.toString();  // return string builder as a string
    }

    /**
     * Accept a string and convert the whole string into lower case then
     * upper case only the first letter of each word.
     *
     * @param strInput - string to be converted
     * @return - string with every word's first letter capitalized
     */
    @Override
    public String properCase(String strInput)
    {
        String strTest;
        String strLow= new String (strInput);
        //convert the whole string into lower case
        strLow=strLow.toLowerCase();
        char[] Case = strLow.toCharArray();
        boolean f = false;
        //upper case only the first letter of each word.
        for (int i = 0; i < Case.length; i++)
        {
            if (f==false && Character.isLetter(Case[i]))
            {
                Case[i] = Character.toUpperCase(Case[i]);
                f = true;
            } else if (Character.isWhitespace(Case[i]) || Case[i]==';' || Case[i]=='\'' || Case[i]=='-')
            {
                f = false;
            }
        }
        return String.valueOf(Case);
    }

    /**
     * Accept a string and save it to a local file
     *
     * @param strFileName - a full file path and name.
     * @param strContent  - string to be saved to disk.
     * @throws Exception
     */
    @Override
    public void saveToFile(String strFileName, String strContent) throws Exception
    {
        PrintWriter outputFile = new PrintWriter(strFileName);
        outputFile.print(strContent);
        outputFile.close();
    }
}
