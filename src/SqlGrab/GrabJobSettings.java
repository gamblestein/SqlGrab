/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;

/**
 *
 * @author Stacey
 */
public class GrabJobSettings implements IngestModuleIngestJobSettings {

    private static final long serialVersionUID = 1L;
    private boolean skipKnownFiles = true;
    
    GrabJobSettings(){
    }
    
    @Override
    public long getVersionNumber(){
        return serialVersionUID;
    }
}
