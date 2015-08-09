/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.sleuthkit.datamodel.AbstractFile;

/**
 *
 * @author Stacey
 */
public class SQLPage {
    
    private int pageSize;
    
    private String freeData = "";
    private String unAllocData = "";
    
    byte[] sqlPage;
        
    int freeBlock;
    int numCells;
    int cellOffset;
    int numFreebytes;
    
    private static final int BTREE_FREE_OFFSET_OFFSET = 1;
    private static final int BTREE_FREE_OFFSET_SIZE = 3;
    
    private static final int NUM_CELLS_OFFSET = 3;
    private static final int NUM_CELLS_SIZE = 5;
    
    private static final int CELL_OFFSET_OFFSET = 5;
    private static final int CELL_OFFSET_SIZE = 7;
    
    private static final int NUM_FREE_BYTES_OFFSET = 7;
    private static final int NUM_FREE_BYTES_SIZE = 1;

    private static final int HEADER_OFFSET = 1;
    
    public SQLPage(int size, byte[] sqlFile) {
        this.pageSize = size;
        this.sqlPage = sqlFile;
    }
    
    public void CollectInformation(){
        
        
        ByteBuffer wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, BTREE_FREE_OFFSET_OFFSET, BTREE_FREE_OFFSET_SIZE));
        freeBlock = wrapped.getShort();

        wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, NUM_CELLS_OFFSET, NUM_CELLS_SIZE));
        numCells = wrapped.getShort();
        
        wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, CELL_OFFSET_OFFSET, CELL_OFFSET_SIZE));
        cellOffset = wrapped.getShort();
        
        //wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, NUM_FREE_BYTES_OFFSET, NUM_FREE_BYTES_SIZE));
        //numFreebytes = wrapped.getInt();
        getFreeData();
        getUnAllocData();
    }
    
    public String GetRawData(){
        String ReturnString = "";
        if(!freeData.equals("")){
            ReturnString = "Free:\n" + freeData; 
        }
        if(!unAllocData.equals("")){
            ReturnString += "Unallocated:\n" + unAllocData;
        }
        return ReturnString;
    }
    
    private void getFreeData()
    {
        while(freeBlock != 0)
        {
            ByteBuffer wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, freeBlock, freeBlock+2));
            int NextFreeBlock = wrapped.getShort();
            
            wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, freeBlock+2, freeBlock+4));
            int FreeBlockSize = wrapped.getShort();
            
            byte[] free = Arrays.copyOfRange(sqlPage, freeBlock+4, freeBlock+FreeBlockSize);  
          

            String data = CleanString(free);

            if(!data.equals(""))
            {
                freeData += "FreeBlock:\n" + data +'\n';
            }
            freeBlock = NextFreeBlock;
        }
    }
    
    private String CleanString(byte[] input){
        try{
        return new String(input,"UTF-8").trim();//.replaceAll("[^\\p{L}\\p{Nd}]+"," ");
        }
        catch(Exception e){
            //todo
        }
        return "";
    }
    
    private void getUnAllocData(){
        int start = 8 + (numCells * 2);
        int length = cellOffset - start;
        
        
        byte[] unallocated = Arrays.copyOfRange(sqlPage, start, start+length);   
        String data = new String(unallocated);
        unAllocData = CleanString(unallocated);
    }
    
}
