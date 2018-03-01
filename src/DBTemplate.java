/**
 * @version 1.01 - 05/23/2014
 * @author Duy Pham
 * @see <br><a href='http://docs.oracle.com/javase/7/docs/technotes/guides/Javadoc/index.html'>Javadoc Documentation</a>
 * @email pnad0911@gmail.com
 */

public interface DBTemplate
{
    /**
     * The addRecord method checks to see if a specific record already exists
     * in the database and if it doesn't, it adds it.
     * If the record already exists, it just skips over all of the code
     * @param strTable - the table
     * @param strKeyName - the key name
     * @param strKeyContents - the key contents of the key field
     * @return - true or fail;
     */
    public Boolean addRecord(String strTable,
                             String strKeyName,
                             String strKeyContents);

    /**
     * The close method basically tells the database to
     * write everything to disk and clear the cache
     */
    public void close();

    /**
     * delete table
     * @param strTable - table name
     * @return
     */
    public Boolean deleteAll(String strDataName,String strTable);

    /**
     *
     * @param strFieldName
     * @return
     */
    public String getField(String strFieldName);

    /**
     *First you run the query, then you loop through the database checking each time to see
     * if there are any moreRecords()
     * @return - returns TRUE if there are more records and FALSE if there are not
     */
    public Boolean moreRecords();

    /**
     * the openConnection method accepts the Data Source Name of the database
     * then looks for a Java Derby connector
     * @param strDataSourceName - the Data Source Name of the database
     */
    public Boolean openConnection(String strDataSourceName);

    /**
     * the query method asks the database for information
     * but does not make any changes to the database.
     * @param strSQL - SQL
     */
    public Boolean query(String strSQL);

    /**
     * To update a record with multiple fields, you simply call the method once for each field
     * @param strTable - the table
     * @param strKeyName - the key name
     * @param strKeyContents - the key contents of the key field
     * @param strFieldName - the field name
     * @param strFieldContents - the contents of the key field
     * @return - true or false
     */
    public Boolean setField(String strTable,
                            String strKeyName,
                            String strKeyContents,
                            String strFieldName,
                            String strFieldContents);

    /**
     * In some cases you need to create a database from scratch.  To do that we need to have a function
     * that allows changes to the database
     * @param strSQL - SQL
     * @return - true or false
     */
    public Boolean execute(String strSQL);
}
