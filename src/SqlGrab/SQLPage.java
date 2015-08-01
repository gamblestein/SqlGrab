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
    
    byte[] sqlPage;
        
    int freeBlock;
    int numCells;
    int cellOffset;
    int numFreebytes;
    
    private static final int BTREE_FREE_OFFSET_OFFSET = 1;
    private static final int BTREE_FREE_OFFSET_SIZE = 2;
    
    private static final int NUM_CELLS_OFFSET = 3;
    private static final int NUM_CELLS_SIZE = 2;
    
    private static final int CELL_OFFSET_OFFSET = 5;
    private static final int CELL_OFFSET_SIZE = 2;
    
    private static final int NUM_FREE_BYTES_OFFSET = 7;
    private static final int NUM_FREE_BYTES_SIZE = 1;

    private static final int HEADER_OFFSET = 8;
    
    public SQLPage(int size, byte[] sqlFile) {
        this.pageSize = size;
        this.sqlPage = sqlFile;
    }
    
    public void CollectHeader(){
        
        
        ByteBuffer wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, BTREE_FREE_OFFSET_OFFSET, BTREE_FREE_OFFSET_SIZE));
        freeBlock = wrapped.getInt();

        wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, NUM_CELLS_OFFSET, NUM_CELLS_SIZE));
        numCells = wrapped.getInt();
        
        wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, CELL_OFFSET_OFFSET, CELL_OFFSET_SIZE));
        cellOffset = wrapped.getInt();
        
        wrapped = ByteBuffer.wrap(Arrays.copyOfRange(sqlPage, NUM_FREE_BYTES_OFFSET, NUM_FREE_BYTES_SIZE));
        numFreebytes = wrapped.getInt();
        
    }
    
    private void getFreeBlock(){
        int start = 8 + (numCells * 2);
        int length = cellOffset - start;
        
        byte[] cells = Arrays.copyOfRange(sqlPage, HEADER_OFFSET, numCells*2);
        byte[] unallocated = Arrays.copyOfRange(sqlPage, start + numCells*2 + 1, length);
        
        String data = new String(unallocated);
        //sqlPage.read(buf, FILE_SIG_OFFSET, FILE_SIG_LENGTH);
    }
    
}
