import java.net.*;                    // URL, URLConnection */
import javax.xml.parsers.*;           // Document Builder */
import java.io.*;                     // InputStream
import org.w3c.dom.*;                 // Document, NodeList
import javax.xml.transform.*;         // Transformer, Transformer Factory
import javax.xml.transform.dom.*;     // DOMSource
/**
 * A class used to provide simple methods
 * for downloading and extracting data from XML pages.
 * @version 1.01 - 05/23/2014
 * @author Duy Pham
 * @email pnad0911@gmail.com
 * @prgm.usage XMLRead xml = new XMLRead();
 */
public class XMLRead implements XMLTemplate
{
    /** Once the XML file is read, it is stored in this object. */
    private Document document;
    /** This is a class level variable that maintains the current record (node)
     number in use.  It is set by find()and used by getField().
     */
    private int intRecNum;
    /** The constructor only initializes intRecNum equal to a minus one. */
    public XMLRead()
    {
        intRecNum = -1;
    }
    /**
     This function accepts a URL to an XML page and stores it in a
     Document object.  The class variable intRecNum is also set to -1.
     */
    public void loadPage(String strURL) throws Exception
    {
        String strFileName="";
        String strContent="";
        Status("readXML: URL=" + strURL);

        URL myWebAddress= new URL(strURL);
        Status("readXML: URL is set up");
        URLConnection myConnection = myWebAddress.openConnection();
        Status("readXML: Connection opened");
        InputStream myStream = myConnection.getInputStream();
        Status("readXML: Get URL done");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Status("readXML: Factory built");
        DocumentBuilder builder = factory.newDocumentBuilder();
        Status("readXML: Builder built");
        document = builder.parse(myStream);
        Status("readXML: Document is done");
        intRecNum = 0;
    }
    /**
     This function looks for a table name in the XML document
     and returns the first child node.
     */
    public String setTable(String strTable) throws Exception
    {
        NodeList list = document.getElementsByTagName(strTable);
        Node node = list.item(0);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(node);
        return source.getNode().getFirstChild().toString();
    }
    /**
     This function will search for a value represented by strKey in
     a field indicated by strKeyField.
     <ul>
     <li>intRecNum is set to a minus one if it did not find the key</li>
     <li>intRecNum is set to the index number (position) if it did find the key.
     </ul>
     */
    public int find(String strKeyName, String strKeyContents) throws Exception
    {
        intRecNum = -1;  // Class Level Variable
        // create an array of nodes of strKeyFields
        NodeList list = document.getElementsByTagName(strKeyName);
        // start searching at the beginning
        int intCount = 0;
        // flag to stop the search once found
        boolean blnContinue = true;
        while (intCount < list.getLength() && blnContinue)
        {
            // list.item contains an array of nodes made up of Fields.
            // getFirstChild() gets the first child node (which is the record itself)
            // getNodeValue() gets the text contents of that field
            if (list.item(intCount).getFirstChild().getNodeValue().equals(strKeyContents))
            {
                intRecNum = intCount;
                blnContinue = false;
            }
            intCount++;
        }
        return intRecNum;
    }
    /**
     This method extracts the contents of the field at the
     the current intRecNum position.
     */
    public String getField(String strFieldName) throws Exception
    {
        String strRet="";
        NodeList list = document.getElementsByTagName(strFieldName);
        if (intRecNum > -1 && intRecNum < list.getLength())
        {
            strRet = list.item(intRecNum).getFirstChild().getNodeValue().trim();
        }
        return strRet;
    }
    /**
     This method sets the current record to intRec and then
     extracts the contents of that field at that record.
     */
    public String getField(String strField, int intRec) throws Exception
    {
        if (intRec > -1)
        {
            intRecNum = intRec; // Class Variable
            return getField(strField);
        }
        else
        {
            return "";
        }
    }
    /**
     This method returns the number of records in the XML
     file after a getElementsByTagName() search
     */
    public int getRecordCount(String strFieldName)
    {
        NodeList list = document.getElementsByTagName(strFieldName);
        return list.getLength();
    }
    /**
     This method is just used to display data during testing.
     */
    public  void Status(String strVar)
    {
        System.out.println("XMLRead: " + strVar);
    }

}


