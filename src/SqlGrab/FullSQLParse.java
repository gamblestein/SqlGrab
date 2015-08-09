/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SqlGrab;

import java.io.File;
import java.nio.file.Files;
import org.sleuthkit.autopsy.coreutils.SQLiteDBConnect;
import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.sleuthkit.autopsy.datamodel.ContentUtils;
import org.sleuthkit.datamodel.AbstractFile;



public class FullSQLParse {
    
    private static final String GETTABLES = "Select name from sqlite_master where type = 'table'";
    
    static String[] tableNames;
    ArrayList list = new ArrayList();
    private static final String TEMPFILE = "tempfile.sqlite";
    
    
    public static ArrayList GetTables(AbstractFile file){
        CreateDBFile(file);
        ArrayList<String> returnList = new ArrayList();
        SQLiteDBConnect tempdbConnect = new SQLiteDBConnect();
        try{
           tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");
           ResultSet rs = tempdbConnect.executeQry(GETTABLES);
           while(rs.next())
           {
               returnList.add(rs.getString("name"));
           }       
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
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
           //SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc.sqlite:"+path.toString());          
           tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");
           ResultSet rs = tempdbConnect.executeQry(nameQuery);
           ft = new FillTable(rs);
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
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
           //SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc.sqlite:"+path.toString());          
           tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");
           rs = tempdbConnect.executeQry(nameQuery);
              
           
            ResultSetMetaData metaData=rs.getMetaData();
            int rowCount=0;
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
        catch (Exception e)
        {
            System.out.print(e.getMessage());
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
        catch (Exception ex){
            System.out.println("Error reading file: " + ex.getLocalizedMessage());
        }
        
        return TEMPFILE;
    }
    
}
