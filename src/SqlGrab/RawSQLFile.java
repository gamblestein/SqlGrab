
package SqlGrab;
import org.sleuthkit.datamodel.AbstractFile;
import java.io.IOException;
import org.sleuthkit.datamodel.TskCoreException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class RawSQLFile {
    
    private static final long FILE_SIG_OFFSET = 0;
    private static final long FILE_SIG_LENGTH = 16;
    
    private static final long PAGE_SIZE_OFFSET = 16;
    private static final long PAGE_SIZE_LENGTH = 2;
    
    private static final long FILE_FLAG_LENGTH = 1;
    private static final int FILE_FLAG_VALUE = 13;
    
    
    private ArrayList<SQLPage> pages = new ArrayList();
    
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
            sqlFile.read(buf, PAGE_SIZE_OFFSET, PAGE_SIZE_LENGTH);   
        }
        catch(TskCoreException ex){
            System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
        }
        
        ByteBuffer wrapped = ByteBuffer.wrap(buf);
        short pageSize = wrapped.getShort();
        
        byte[] dataBlock = new byte[pageSize];
        
        for(int fileOffset = pageSize;fileOffset < sqlFile.getSize();fileOffset +=pageSize) {
            if(IsBtree(fileOffset)){
                
                try {  
                    sqlFile.read(dataBlock, fileOffset, pageSize);   
                }
                catch(TskCoreException ex){
                    System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
                }
        
                SQLPage sqlPage = new SQLPage(pageSize, dataBlock);
                sqlPage.CollectInformation();
                
                pages.add(sqlPage);
            }
        }
    }
    
    public String GetPageData()
    {
        String returnString = "";
        for(int i=0; i < pages.size(); i++){
            String temp = pages.get(i).GetRawData();
            if(temp != ""){
                returnString += "Page \n==================================================\n"+  temp + "\n";
            }
        }
        return returnString;
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
