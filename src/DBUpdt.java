import java.sql.*;
/**
 * @version 1.01 - 05/23/2014
 * @author Duy Pham
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 * @email pnad0911@gmail.com
 */
public class DBUpdt implements DBTemplate
{
    // Class Level Variables
    Connection dbConn;
    Statement  dbCmdText;
    ResultSet  dbRecordset;
    String     dbTable;
    String     dbKeyField;

    /**
     * Constructor
     */
    public DBUpdt()
    {

    }
    /**
     * The addRecord method checks to see if a specific record already exists
     * in the database and if it doesn't, it adds it.
     * If the record already exists, it just skips over all of the code
     *
     * @param strTable       - the table
     * @param strKeyName     - the key name
     * @param strKeyContents - the key contents of the key field
     * @return - true or fail;
     */
    @Override
    public Boolean addRecord(String strTable, String strKeyName, String strKeyContents)
    {
        String strSQL ="";
        Boolean blnStatus = false;
        try
        {
            // check to see if the record exists
            dbCmdText = dbConn.createStatement();
            strSQL = "SELECT * FROM " + strTable + " WHERE " + strKeyName + "='" + strKeyContents + "'";
            query(strSQL);
            if(!moreRecords())
            {
                // the record does not exist, therefore needs to be added to the table
                strSQL = "INSERT INTO " + strTable + " (" + strKeyName + ") VALUES ('" + strKeyContents + "')";
                execute(strSQL);
                status("Record added "+strSQL);
                blnStatus = true;
            }
            else
            {
                status("Record NOT added " + strSQL);
            }
        }
        catch (Exception e)
        {
            status("SELECT command failed " + strSQL);
        }
        return blnStatus;
    }

    /**
     * The close method basically tells the database to
     * write everything to disk and clear the cache
     */
    @Override
    public void close()
    {
        try
        {
            dbRecordset.close();
        }
        catch(Exception e)
        {
        }
    }

    /**
     * delete table
     * @param strDataName - name of database
     * @param strTable - table name
     * @return - Boolean
     */
    @Override
    public Boolean deleteAll(String strDataName,String strTable)
    {
        Boolean blnDel;
        try
        {
            Connection conn=DriverManager.getConnection("jdbc:derby:" + strDataName);
            Statement stmt=conn.createStatement();
            String strSQL="DROP TABLE "+strTable;
            stmt.execute(strSQL);
            blnDel=true;
        }
        catch (Exception e)
        {
            blnDel=false;
        }
        return blnDel;
    }

    /**
     * get String
     * @param strFieldName
     * @return String
     */
    @Override
    public String getField(String strFieldName)
    {
        String strRet="";
        try
        {
            strRet = dbRecordset.getString(strFieldName);
        }
        catch(Exception e)
        {
            status("Error");
        }
        return strRet;
    }

    /**
     * First you run the query, then you loop through the database checking each time to see
     * if there are any moreRecords()
     *
     * @return - returns TRUE if there are more records and FALSE if there are not
     */
    @Override
    public Boolean moreRecords()
    {
        Boolean blnStatus = false;
        try
        {
            blnStatus = dbRecordset.next();
        }
        catch (Exception e)
        {
            blnStatus = false;
        }
        return blnStatus; // only one RETURN in each function!
    }

    /**
     * the openConnection method accepts the Data Source Name of the database
     * then looks for a Java Derby connector
     *
     * @param strDataSourceName - the Data Source Name of the database
     */
    @Override
    public Boolean openConnection(String strDataSourceName)
    {
        Boolean blnStatus=false;
        try
        {
            dbConn=DriverManager.getConnection("jdbc:derby:" + strDataSourceName + ";create=true");
            if (dbConn == null)
            {
                status("Connection Failed");
            }
            else
            {
                status("Connection Successful");
                dbCmdText = dbConn.createStatement();
                blnStatus = true;
            }
        }
        catch (Exception e)
        {
            status("Problems opening connection");
        }
        return blnStatus;
    }



    /**
     * the query method asks the database for information
     * but does not make any changes to the database.
     *
     * @param strSQL - SQL
     */
    @Override
    public Boolean query(String strSQL)
    {
        Boolean blnStatus = false;
        try
        {
            dbRecordset = dbCmdText.executeQuery(strSQL);
            blnStatus = true;
        }
        catch (Exception ex)
        {
            status("Query Failed " + strSQL);
        }
        return blnStatus;
    }

    /**
     * To update a record with multiple fields, you simply call the method once for each field
     *
     * @param strTable         - the table
     * @param strKeyName       - the key name
     * @param strKeyContents   - the key contents of the key field
     * @param strFieldName     - the field name
     * @param strFieldContents - the contents of the key field
     * @return - true or false
     */
    @Override
    public Boolean setField(String strTable, String strKeyName, String strKeyContents, String strFieldName, String strFieldContents)
    {
        Boolean blnStatus = false;

        // goal is = UPDATE customer SET city='SAN DIEGO' WHERE customerID='2100'

        String strSQL = "UPDATE " + strTable + " SET " + strFieldName + "='"
                + strFieldContents + "' " +
                " WHERE " + strKeyName + "='" + strKeyContents + "' ";
        execute(strSQL);
        return blnStatus;
    }

    /**
     * In some cases you need to create a database from scratch.  To do that we need to have a function
     * that allows changes to the database
     *
     * @param strSQL - SQL
     * @return - true or false
     */
    @Override
    public Boolean execute(String strSQL)
    {
        Boolean blnStatus = false;

        try
        {
            dbCmdText.execute(strSQL);
            blnStatus = true;
        }
        catch (Exception ex)
        {
            status("Execute command failed " + strSQL);

        }
        return blnStatus;
    }

    /**
     * display the message
     * @param strMsg - message
     */
    public void status(String strMsg)
    {
        System.out.println(strMsg);
    }
}
