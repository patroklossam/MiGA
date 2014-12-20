/** 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 *
 * Copyright ownership: Ioannis Kavakiotis, Patroklos Samaras, Antonios Voulgaridis
 */

/**
 *
 * @authors Ioannis Kavakiotis, Patroklos Samaras, Antonios Voulgaridis
 */
package MiGA;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.io.*;
import java.util.Stack;
import java.util.List;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class updatepop extends JFrame{
    private Process p;
    private JLabel msg;
    
    public updatepop(String org, String chrom, boolean flag){
        
        setTitle("Wait for update");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        Image im = Toolkit.getDefaultToolkit().getImage("ssr.png");
        this.setIconImage(im);
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
	        catch (Exception e) {}
        
        this.setAlwaysOnTop(true);
        
        
        if(chrom!=null){
            msg = new JLabel("<html>Currently updating organism: <b>"+org+"</b> Chromosome: <i>"+chrom+"</i></html>");
        }
        else{
            msg = new JLabel("<html>Currently updating organism: <b>"+org+"</b></html>");
        }
        
        
        JPanel panel = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Update may take some hours (depends on genome)");
        panel.setBorder(title);
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();
        
        a.fill = GridBagConstraints.HORIZONTAL;
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = 0;
        panel.add(new JLabel("<html><h3>Waiting...</h3></html>"), a);
        
        
        try {
            if(flag){
                p = Runtime.getRuntime().exec("cmd /c cd organisms && perl chromosome_downloader "+ org +" "+chrom);
            }else{
                p = Runtime.getRuntime().exec("cmd /c cd organisms && perl download_gene_for "+ org);
            }
            
            try {
                p.waitFor();
                dispose();
            } catch (InterruptedException ex) {
                Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
