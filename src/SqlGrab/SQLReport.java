/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
    public void generateReport(String string, ReportProgressPanel pnl) {
        pnl.setIndeterminate(false);
        pnl.start();
        pnl.updateStatusLabel("SQLReport");
        
        String reportPath = "SQLGrab.txt";
        
        Case currCase = Case.getCurrentCase();
        SleuthkitCase skCase = currCase.getSleuthkitCase();
        
        try{
        
            BufferedWriter bfw = new BufferedWriter(new FileWriter(reportPath,true));
            
            final String query = "type = " + TskData.TSK_DB_FILES_TYPE_ENUM.FS.getFileType() //NON-NLS
                    + " AND name != '.' AND name != '..'"; //NON-NLS

            pnl.updateStatusLabel(NbBundle.getMessage(this.getClass(), "ReportBodyFile.progress.loading"));
            List<AbstractFile> fs = skCase.findAllFilesWhere(query);
            for (AbstractFile file : fs) {
                if(RawSQLFile.IsSQLLite(file))
                {
                    FillTable table = FullSQLParse.GetSqlDataInTable(reportPanel.GetInputText());
                    
                    bfw.write(table.);
                }
            }
            
            
        } catch (Exception e){
            
        }
        pnl.complete();
    }

    
//    //http://www.codeproject.com/Tips/261752/Convert-DataTable-to-String-by-Extension-Method
//    public String ConvertDataTableToString(this DataTable dt)
//    {
//        StringBuilder stringBuilder = new StringBuilder();
//        dt.Rows.Cast<DataRow>().ToList().ForEach(dataRow =>
//        {
//            dt.Columns.Cast<DataColumn>().ToList().ForEach(column =>
//            {
//                stringBuilder.AppendFormat("{0}:{1} ", column.ColumnName, dataRow[column]);
//            });
//            stringBuilder.Append(Environment.NewLine);
//        });
//        return stringBuilder.ToString();
//    }
    
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
