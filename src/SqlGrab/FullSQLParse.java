/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SqlGrab;

import org.sleuthkit.autopsy.coreutils.SQLiteDBConnect;
import java.sql.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



/**
 *
 * @author Stacey
 */
public class FullSQLParse {
    
    private static final String GETTABLES = "Select name from sqlite_master where type = 'table'";
    
    static String[] tableNames;
    ArrayList list = new ArrayList();
    
    //Return this as a datasource
    public static void GetSqlData(String filePath){
        Connection c = null;
        
        Path path = Paths.get(filePath);
        
        try{
           //SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc.sqlite:"+path.toString());          
           SQLiteDBConnect tempdbConnect = new SQLiteDBConnect("org.sqlite.JDBC","jdbc:sqlite:tempfile.sqlite");
           ResultSet rs = tempdbConnect.executeQry(GETTABLES);
           int colCount = rs.getMetaData().getColumnCount();
           
           F
           
           
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        
    }
    
    
}
