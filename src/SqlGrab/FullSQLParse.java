/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SqlGrab;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.sleuthkit.autopsy.coreutils.SQLiteDBConnect;
import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.sleuthkit.autopsy.datamodel.ContentUtils;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.autopsy.coreutils.Logger;
import java.util.logging.Level;



public class FullSQLParse {
    
    //Finals
    private static final String GETTABLES = "Select name from sqlite_master where type = 'table'";
    private static final String TEMPFILE = "tempfile.sqlite";
    private static final String DBTYPE = "org.sqlite.JDBC";
    private static final String DBCONNECT = "jdbc:sqlite:tempfile.sqlite";
    private static final String NAME = "name";
    
    //Globals
    static String[] tableNames;
    ArrayList list = new ArrayList();
    private static Logger logger = Logger.getLogger(FullSQLParse.class.getName());
    
    
    
    public static ArrayList GetTables(AbstractFile file){
        CreateDBFile(file);
        
        ArrayList<String> returnList = new ArrayList();
        SQLiteDBConnect tempdbConnect = new SQLiteDBConnect();
        
        try{
           tempdbConnect = new SQLiteDBConnect(DBTYPE,DBCONNECT);
           ResultSet rs = tempdbConnect.executeQry(GETTABLES);
           while(rs.next())
           {
               returnList.add(rs.getString(NAME));
           }       
        }
        catch (SQLException e)
        {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
        finally
        {
            tempdbConnect.closeConnection();;
        }
        return returnList;
    }
    
    //Return this as a datasource
    public static FillTable GetSqlDataInTable(AbstractFile file, String nameQuery){
        FillTable ft = null;
        CreateDBFile(file);
        
        SQLiteDBConnect tempdbConnect = new SQLiteDBConnect();
        try{
           tempdbConnect = new SQLiteDBConnect(DBTYPE,DBCONNECT);
           ResultSet rs = tempdbConnect.executeQry(nameQuery);
           ft = new FillTable(rs);
        }
        catch (Exception e)
        {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
        finally
        {
            tempdbConnect.closeConnection();;
        }
        return ft;
    }
    
    //Return this as a datasource
    public static ArrayList GetSqlData(AbstractFile file, String nameQuery){
        ResultSet rs = null;
        CreateDBFile(file);
        ArrayList<String[]> data = new ArrayList();
        
        SQLiteDBConnect tempdbConnect = new SQLiteDBConnect();
        
        try{
           tempdbConnect = new SQLiteDBConnect(DBTYPE,DBCONNECT);
           rs = tempdbConnect.executeQry(nameQuery);
              
           
            ResultSetMetaData metaData=rs.getMetaData();
            int columnCount=metaData.getColumnCount();
            //Return to begining after header parse

            while(rs.next()){
                String[] row=new String[columnCount];

                for(int j=0;j<columnCount;j++){
                    if(rs.getObject(j+1) != null){
                        row[j]= (String) rs.getObject(j+1).toString();
                    }
                    else
                    {
                        row[j] = "";
                    }
                }
                data.add(row);
            }
        }
        catch (SQLException e)
        {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
        finally
        {
            tempdbConnect.closeConnection();;
        }
        return data;
    }
    
    private static String CreateDBFile(AbstractFile fileData){
         
        try{
            Path path =Paths.get(TEMPFILE);
            Files.delete(path);
            File file = new File(TEMPFILE);
            ContentUtils.writeToFile(fileData, file);
            
        }
        catch (IOException ex){
            logger.log(Level.WARNING, ex.getLocalizedMessage());
        }
        
        return TEMPFILE;
    }
    
}
