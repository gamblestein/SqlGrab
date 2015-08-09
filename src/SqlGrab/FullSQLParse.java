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
    
    
    public static ArrayList GetTables(){
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
    public static FillTable GetSqlDataInTable(String nameQuery){
        FillTable ft = null;
        
        try{
            ft = new FillTable(GetSqlData(nameQuery));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
              
        return ft;
    }
    
    //Return this as a datasource
    public static ResultSet GetSqlData(String nameQuery){
        Connection c = null;
        ResultSet rs = null;
        
        SQLiteDBConnect tempdbConnect = new SQLiteDBConnect();
        try{
           //SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc.sqlite:"+path.toString());          
           tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");
           rs = tempdbConnect.executeQry(nameQuery);
              
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        finally
        {
            tempdbConnect.closeConnection();;
        }
        return rs;
    }
    
    
}
