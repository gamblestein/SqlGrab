/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SqlGrab;

import org.sleuthkit.autopsy.coreutils.SQLiteDBConnect;
import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.table.TableModel;



/**
 *
 * @author Stacey
 */
public class FullSQLParse {
    
    private static final String GETTABLES = "Select name from sqlite_master where type = 'table'";
    
    static String[] tableNames;
    ArrayList list = new ArrayList();
    SQLiteDBConnect tempdbConnect;
    
    public FullSQLParse(String filePath){
        try{
           //SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc.sqlite:"+path.toString());          
           tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");             
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        } 
    }
    
    
    public ArrayList GetTables(){
        ArrayList<String> returnList = new ArrayList();
        try{
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
        return returnList;
    }
    
    //Return this as a datasource
    public static FillTable GetSqlData(String nameQuery){
        Connection c = null;
        FillTable ft = null;
        
        try{
           //SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc.sqlite:"+path.toString());          
           SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");
           ResultSet rs = tempdbConnect.executeQry(nameQuery);
           ft = new FillTable(rs);
              
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return ft;
    }
    
    
}
