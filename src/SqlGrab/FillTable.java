//https://enginoz.wordpress.com/2010/04/07/how-to-fill-jtable-with-sql-query/

package SqlGrab;

import java.sql.*;
import java.util.*;
import javax.swing.table.*;
 
public class FillTable extends AbstractTableModel{

    /**
    *
    */
    private static final long serialVersionUID = -912060609250881296L;
    private int rowCount;
    private int columnCount;
    private ArrayList data=new ArrayList();
    private ArrayList header=new ArrayList();

    public FillTable(ResultSet _rs) throws Exception
    {
        setRS(_rs);
    }

    public void setRS(ResultSet _rs)
    {
        try
        {
            ResultSetMetaData metaData=_rs.getMetaData();
            rowCount=0;
            columnCount=metaData.getColumnCount();
            //Return to begining after header parse

            while(_rs.next()){
                Object[] row=new Object[columnCount];

                for(int j=0;j<columnCount;j++){
                    header.add(metaData.getColumnName(j+1));
                    row[j]=_rs.getObject(j+1);
                }
                data.add(row);
                rowCount++;
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public int getColumnCount(){
        return columnCount;
    }
    
    @Override
    public int getRowCount(){
        return rowCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        Object[] row=(Object[]) data.get(rowIndex);
        return row[columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex){
        try{
            return (String) header.get(columnIndex);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
