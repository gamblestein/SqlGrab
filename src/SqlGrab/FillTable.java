//https://enginoz.wordpress.com/2010/04/07/how-to-fill-jtable-with-sql-query/

package SqlGrab;

import java.sql.*;
import java.util.*;
import javax.swing.table.*;
 
public class FillTable extends AbstractTableModel{

    private int rowCount;
    private int columnCount;
    
    private final ArrayList data=new ArrayList();
    private final ArrayList header=new ArrayList();

    public FillTable(ResultSet rs) throws Exception
    {
        setRS(rs);
    }

    private void setRS(ResultSet _rs) throws Exception
    {
        ResultSetMetaData metaData=_rs.getMetaData();
        columnCount=metaData.getColumnCount();
        
        while(_rs.next()){
            Object[] row=new Object[columnCount];

            for(int j=0;j<columnCount;j++){
                header.add(metaData.getColumnName(j+1));
                row[j]=_rs.getObject(j+1);
            }
            data.add(row);
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
        return (String) header.get(columnIndex);
    }
}
