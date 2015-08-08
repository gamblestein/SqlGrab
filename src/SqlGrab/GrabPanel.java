/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.IOException;
import org.openide.nodes.Node;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.TskCoreException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JViewport;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.datamodel.ContentUtils;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;

/**
 *
 * @author Stacey
 */
@ServiceProvider(service = DataContentViewer.class)
public class GrabPanel extends javax.swing.JPanel implements DataContentViewer {
    private static final String TEMPFILE = "tempfile.sqlite"; 
    /**
     * Creates new form GrabPanel
     */
    public GrabPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(GrabPanel.class, "GrabPanel.jScrollPane3.TabConstraints.tabTitle"), jScrollPane3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

//    public void addDataToTable(ResultSet rs){
//        try{
//           jTable2 = new JTable(buildTableModel(rs)); 
//        }
//        catch (Exception e){
//
//        }
//
//    }
    
    //Code borrowed from StackOverflow - all credit to author
    //http://stackoverflow.com/questions/10620448/most-simple-code-to-populate-jtable-from-resultset
    public static DefaultTableModel buildTableModel(ResultSet rs)
        throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
    
    
    @Override
    public void setNode(org.openide.nodes.Node selectedNode) {
        
        
        try{
            if (selectedNode == null) {
                setText("");
                return;    
            }
            AbstractFile file = selectedNode.getLookup().lookup(AbstractFile.class);
            if (file == null) {
                // non-content object passed in
                setText("");
                return;
            }
            
            if(file.canRead())
            {
                ArrayList<BlackboardArtifact> list = file.getAllArtifacts();
                try {
                        
                    if(RawSQLFile.IsSQLLite(file)){
                        RawSQLFile sqlFile = new RawSQLFile(file);
                        sqlFile.CreatePages();
                        FullParse(CreateDBFile(file));
                        jTextArea1.setText(sqlFile.GetPageData());
                    }
                }
                catch (IOException ex){
                    System.out.println("Error reading files from database: " + ex.getLocalizedMessage());
                }
            }

        } catch (Exception ex) {
            setText("Error reading file: " + ex.getLocalizedMessage());
        }
    }
    
    private void CreateTab(String tabName){
                
        JHorizontalFriendlyTable jTable = new JHorizontalFriendlyTable();
        JScrollPane jScrollPane = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        jTable.setModel(FullSQLParse.GetSqlData("Select * from " + tabName));
        jScrollPane.setViewportView(jTable);
        jScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, jTable.getTableHeader());
        
        
        
        Container tableParent = jTable.getParent();
        if(tableParent != null){
        
            if (jTable.getPreferredScrollableViewportSize().getWidth() > tableParent.getPreferredSize().getWidth())
              {
              jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
              jTable.doLayout();
            }
        }
        jTable.setRowHeight(20);
        jTable.setDragEnabled(false);
        jTable.getTableHeader().setReorderingAllowed(false);
        
        
        jTabbedPane1.addTab(tabName, jScrollPane); // NOI18N

    }
    
    private void FullParse(String filepath){
    
        FullSQLParse fp = new FullSQLParse(filepath);
        ArrayList<String> tabs = fp.GetTables();
        
        for(String tabname: tabs){
            CreateTab(tabname);
        }
    }  
    
    private String CreateDBFile(AbstractFile fileData){
        //String path = Case.getCurrentCase().getTempDirectory() + File.separator + TEMPFILE;
        try{
            ContentUtils.writeToFile(fileData, new File(TEMPFILE));
        }
        catch (Exception e){
            //TODO
        }
        
        return TEMPFILE;
    }
    
    
    // set the text in the lable in the JPanel
    private void setText(String str) {
        //this.setText(str);
    }

    @Override
    public String getTitle() {
        return "SQLite";
    }

    @Override
    public String getToolTip() {
        return "test";
    }

    @Override
    public DataContentViewer createInstance() {
        return new GrabPanel();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {
        setText("");
    }

    @Override
    public boolean isSupported(org.openide.nodes.Node node) {
        // get a Content datamodel object out of the node
        Content content = node.getLookup().lookup(Content.class);
        if (content == null) {
            return false;
        }
   
        // we only want files that are 1024 bytes or larger (for no good reason)
        if (content.getSize() < 1024) {
            return false;
        }
        return true;    
    }
    
    @Override
    public int isPreferred(org.openide.nodes.Node node) {
        // we return 1 since this module will operate on nearly all files
        return 1;
    }
}
