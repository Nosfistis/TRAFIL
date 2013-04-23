/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Simulations.TclDesignerPanel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilities.PathLocator;

/**
 *
 * @author Drakoulelis
 */
public class SimulationPropertiesWindow extends javax.swing.JFrame {

    private static TRAFIL tr;
    private static LinkListWindow linkWindow;
    private static TclDesignerPanel tclDesigner;

    /**
     * Creates new form SimulationPropertiesWindow
     */
    public SimulationPropertiesWindow(TRAFIL tr) {
	initComponents();
	this.setVisible(false);
	SimulationPropertiesWindow.tr = tr;
	SimulationPropertiesWindow.linkWindow = tr.getLinkWindow();
	tclDesigner = tr.getTclDesigner();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        simStartButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        outputFileField = new javax.swing.JTextField();
        simEndField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        exportScriptButton = new javax.swing.JButton();

        setTitle("Simulation Parameters");

        simStartButton.setText("Save & Simulate");
        simStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simStartButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Output file name:");

        simEndField.setText("0");

        jLabel3.setText("End time:");

        jLabel2.setText(".tcl");

        exportScriptButton.setText("Save");
        exportScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportScriptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(exportScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(simEndField)
                            .addComponent(simStartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(outputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(simEndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simStartButton)
                    .addComponent(exportScriptButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simStartButtonActionPerformed
	this.setVisible(false);
	String outputContent = TclDesignerPanel.getScript();

	if (outputContent.equals("error")) {
	    return;
	}

	outputContent += "#Call the finish procedure after  seconds of simulation time\n";
	outputContent += "$ns at " + simEndField.getText() + " \"finish\"\n\n";

	outputContent += "#Run the simulation\n"
		+ "$ns run";

	outputContent = outputContent.replaceFirst("tracefile.tr", outputFileField.getText() + ".tr");

	try {
	    String path = PathLocator.getDesignOutputPath(System.getProperty("user.dir")) + outputFileField.getText() + ".tcl";
	    File file = new File(path);
	    file.createNewFile();
	    BufferedWriter out = new BufferedWriter(new FileWriter(file));
	    out.write(outputContent);
	    out.close();
	    tr.executeNS2Simulation(path);
	} catch (IOException ex) {
	    Logger.getLogger(SimulationPropertiesWindow.class.getName()).log(Level.SEVERE, null, ex);
	}
    }//GEN-LAST:event_simStartButtonActionPerformed

    private void exportScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportScriptButtonActionPerformed
	this.setVisible(false);
	String outputContent = TclDesignerPanel.getScript();

	if (outputContent.equals("error")) {
	    return;
	}

	outputContent += "#Call the finish procedure after  seconds of simulation time\n";
	outputContent += "$ns at " + simEndField.getText() + " \"finish\"\n\n";

	outputContent += "#Run the simulation\n"
		+ "$ns run";

	outputContent = outputContent.replaceFirst("tracefile.tr", outputFileField.getText() + ".tr");

	try {
	    String path = PathLocator.getDesignOutputPath(System.getProperty("user.dir")) + outputFileField.getText() + ".tcl";
	    File file = new File(path);
	    file.createNewFile();
	    BufferedWriter out = new BufferedWriter(new FileWriter(file));
	    out.write(outputContent);
	    out.close();
	} catch (IOException ex) {
	    Logger.getLogger(SimulationPropertiesWindow.class.getName()).log(Level.SEVERE, null, ex);
	}

	javax.swing.JOptionPane.showMessageDialog(this, "Tcl script file saved under TRAFIL/SimulationDesignOutputs folder.", "Script saved!", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_exportScriptButtonActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exportScriptButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField outputFileField;
    private javax.swing.JTextField simEndField;
    private javax.swing.JButton simStartButton;
    // End of variables declaration//GEN-END:variables
}
