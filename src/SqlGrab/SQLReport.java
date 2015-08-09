/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.report.GeneralReportModule;
import org.sleuthkit.autopsy.report.ReportProgressPanel;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.FsContent;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskData;

/**
 *
 * @author Stacey
 */
public class SQLReport implements GeneralReportModule {

    SQLReportPanel reportPanel;
    public static SQLReport instance = null;
    
    private SQLReport() {}
    
    public static synchronized SQLReport getDefault(){
        if(instance == null){
            instance = new SQLReport();
        }
        return instance;
    }
    
    
    @Override
    @SuppressWarnings("deprecation")
    public void generateReport(String basDir, ReportProgressPanel pnl) {
        pnl.setIndeterminate(false);
        pnl.start();
        pnl.updateStatusLabel("SQLReport");
        
        String reportPath = basDir + File.pathSeparator + "SQLGrab.txt";
        
        Case currCase = Case.getCurrentCase();
        SleuthkitCase skCase = currCase.getSleuthkitCase();
        BufferedWriter bfw;
        
        try{
        
            bfw = new BufferedWriter(new FileWriter(reportPath,true));
                        
            List<AbstractFile> fs = skCase.findFiles(skCase.getContentById(Integer.valueOf(reportPanel.GetContentID())), reportPanel.GetFileName());
            for (AbstractFile file : fs) {
                if(RawSQLFile.IsSQLLite(file))
                {
                    ArrayList<String[]> al = FullSQLParse.GetSqlData(file, reportPanel.GetInputText());                  
                    ParseOutput(al,bfw);
                }
            }
            bfw.close();
            
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally
        {
            
        }
        pnl.complete();
    }

    
    private void ParseOutput(ArrayList<String[]> list, BufferedWriter bfw)
    {
        
        try{
            String[] templist;
            for(int i=0; i<list.size(); i++){
                templist = list.get(i);
                for(int j=0;j<templist.length;j++){

                    bfw.write(templist[j] + "|");
                }
                bfw.newLine();
            }
        }
        catch(Exception ex)
        {
            //TODO
        }
    }
    
    @Override
    public JPanel getConfigurationPanel() {
        reportPanel = new SQLReportPanel();
        return reportPanel;
    }

    @Override
    public String getName() {
        return "SQLiteReport.getname.txt";
    }

    @Override
    public String getDescription() {
        return "SQLiteReport.getdescription.txt";
    }

    @Override
    public String getRelativeFilePath() {
        return "SQLGrab.txt";
    }
    
}
