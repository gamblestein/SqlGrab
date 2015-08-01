/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import java.util.List;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.autopsy.ingest.DataSourceIngestModuleProgress;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.autopsy.casemodule.services.Services;
import org.sleuthkit.autopsy.casemodule.services.FileManager;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.TskCoreException;
import java.io.IOException;
import java.util.ArrayList;
import org.openide.util.Exceptions;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.BlackboardAttribute.ATTRIBUTE_TYPE;
import org.sleuthkit.datamodel.Content;


/**
 *
 * @author Stacey
 */
public class GrabModule implements org.sleuthkit.autopsy.ingest.DataSourceIngestModule{

    private IngestJobContext context = null;
    
    Case case1;
    SleuthkitCase skc;
    
    
    @Override
    public void startUp(IngestJobContext ijc) throws IngestModuleException {
        this.context = ijc;
        case1 = Case.getCurrentCase();
        skc = case1.getSleuthkitCase();
        
    }
    
    @Override
    public ProcessResult process(Content cntnt, DataSourceIngestModuleProgress dsimp) {
        
        ProcessResult result = ProcessResult.OK;
        
        Services serv = new Services(skc);
        FileManager fm = serv.getFileManager();
        
        try {
        
            List<AbstractFile> allFiles = fm.findFiles(cntnt, "%");
            for(AbstractFile file: allFiles){
                if(file.canRead())
                {
                    ArrayList<BlackboardArtifact> list = file.getAllArtifacts();
                    try {
                        
                        if(SQLFile.IsSQLLite(file)){
                            SQLFile sqlFile = new SQLFile(file);
                            parseDBFile(sqlFile);
                        }
                    }
                    catch (IOException ex){
                        System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
                    }
                }
                
                // check header
                // parse file if DB
                // add to sql list
            }
            createDisplayObjects();
            
        }
        catch (TskCoreException ex){
            System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
            result = ProcessResult.ERROR;
        }
        
        return result;
    }
    
    private void parseDBFile(SQLFile file){
        

    }
    
    private void createDisplayObjects(){
        BlackboardArtifact art;
        try {
            art = skc.newBlackboardArtifact(BlackboardArtifact.ARTIFACT_TYPE.TSK_EXTRACTED_TEXT, 7);
            
            //art = skc.newBlackboardArtifact(BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT, 7);
            //BlackboardAttribute attr2 = new BlackboardAttribute()
            BlackboardAttribute attr = new BlackboardAttribute(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_SET_NAME.getTypeID(),"GrabModule","test");
            art.addAttribute(attr);

            BlackboardAttribute attr2 = new BlackboardAttribute(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_TEXT_FILE.getTypeID(),"GrabModule","test2");
            art.addAttribute(attr2);
            
            
        } catch (TskCoreException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        
        


        
    }
    
    private void displayObjects(){
        
    }
}
