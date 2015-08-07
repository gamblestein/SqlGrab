/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlGrab;

import java.awt.Component;
import org.openide.nodes.Node;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.TskCoreException;

/**
 *
 * @author Stacey
 */
@ServiceProvider(service = DataContentViewer.class)
public class GrabPanel extends javax.swing.JPanel implements DataContentViewer {

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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(GrabPanel.class, "GrabPanel.jScrollPane2.TabConstraints.tabTitle"), jScrollPane2); // NOI18N

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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setNode(org.openide.nodes.Node selectedNode) {
        try{
            if (selectedNode == null) {
                setText("");
                return;    
            }
            Content content = selectedNode.getLookup().lookup(Content.class);
                if (content == null) {
                // non-content object passed in
                setText("");
                return;
            }

            setText("Doing Analysis");
            byte buffer[] = new byte[1024];
            int len = content.read(buffer, 0, 1024);
            int count = 0;
            for (int i = 0; i < len; i++) {
                if (buffer[i] == 0x00) {
                    count++;
                }
        }
        setText(count + " out of " + len + " bytes were 0x00");
        } catch (TskCoreException ex) {
            setText("Error reading file: " + ex.getLocalizedMessage());
        }
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