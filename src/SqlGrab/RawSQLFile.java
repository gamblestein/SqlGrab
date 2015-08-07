/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;
import org.sleuthkit.datamodel.AbstractFile;
import java.io.IOException;
import org.sleuthkit.datamodel.TskCoreException;
import java.nio.ByteBuffer;

/**
 *
 * @author Stacey
 */
public class RawSQLFile {
    
    private static final long FILE_SIG_OFFSET = 0;
    private static final long FILE_SIG_LENGTH = 16;
    
    private static final long FILE_PAGE_OFFSET = 16;
    private static final long FILE_PAGE_LENGTH = 2;
    
    private static final long FILE_FLAG_LENGTH = 1;
    private static final int FILE_FLAG_VALUE = 13;
    
    private static final int FILE_PAGES_OFFSET = 100;
    
    private SQLPage[] pages;
    
    private static final String FILE_SIG = "SQLite";
    
    private AbstractFile sqlFile;
    
    public static boolean IsSQLLite(AbstractFile file) throws IOException{
        
        boolean result = false;
        
        if(!file.canRead()){
            throw new IOException();
        }
        
        byte[] buf = new byte[16];
        try {
            file.read(buf, FILE_SIG_OFFSET, FILE_SIG_LENGTH);
            String str = new String(buf);
            
            if(str.contains(FILE_SIG))
            {
                result = true;
            }
        }
        catch (TskCoreException ex){
            System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
        }
        
        return result;
    }
    
    public RawSQLFile(AbstractFile File){
        this.sqlFile = File;
    }
    
    public void CreatePages(){
        
        byte[] buf = new byte[2];

        
        try {  
            sqlFile.read(buf, FILE_PAGE_OFFSET, FILE_PAGE_LENGTH);   
        }
        catch(TskCoreException ex){
            System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
        }
        
        ByteBuffer wrapped = ByteBuffer.wrap(buf);
        short pageSize = wrapped.getShort();
        
        pages = new SQLPage[((int) sqlFile.getSize()/pageSize)];
        byte[] dataBlock = new byte[pageSize];
        
        for(int fileOffset = FILE_PAGES_OFFSET;fileOffset < sqlFile.getSize();fileOffset +=pageSize) {
            if(IsBtree(fileOffset)){
                
                try {  
                    sqlFile.read(dataBlock, fileOffset, pageSize);   
                }
                catch(TskCoreException ex){
                    System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
                }
        
                SQLPage sqlPage = new SQLPage(pageSize, dataBlock);
                sqlPage.CollectHeader();
                 
                pages[fileOffset%pageSize] = sqlPage;
            }
        }
    }
    
    private boolean IsBtree(long offset){
        byte[] buf = new byte[1];
        boolean result = false;
        
        try {  
            sqlFile.read(buf, offset, FILE_FLAG_LENGTH);   
        }
        catch(TskCoreException ex){
            System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
        }
        
        if(((int) buf[0]) == FILE_FLAG_VALUE){
            result = true;
        }
        return result;
    }
    
    
}
