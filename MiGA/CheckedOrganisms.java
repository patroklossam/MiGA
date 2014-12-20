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
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class CheckedOrganisms extends JFrame {
    
    private JLabel message; 
    private JButton next;
    private String organism;
    private String timestamp;
    private String label;
    private Stack updated;
    private Stack for_update;
    private List<JLabel> labs;
    private String[] organisms;
    private JPanel panel;
    private JOptionPane msg;
    
    public CheckedOrganisms(Stack orgs, Stack timestamps) {
        setTitle("MiGA");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Image im = Toolkit.getDefaultToolkit().getImage("ssr.png");
        this.setIconImage(im);
        
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
	        catch (Exception e) {}
        
        updated = new Stack();
        for_update = new Stack();
        
        labs = new ArrayList<JLabel>();
        
        
        msg= new JOptionPane();
        int org_count=orgs.size();
        
        int columns= org_count/32;
        int rest= org_count%32;
        
        while (orgs.isEmpty() == false) {
            label="<html>";
            for(int j=0;j<32;j++){
                if(orgs.isEmpty() == false){
                    organism = orgs.pop().toString();
                    try{
                        timestamp = timestamps.pop().toString();
                        label+="<p><b>" + organism+"</b> last updated on: #<i>"+timestamp+" </i># </p>";
                        updated.push(organism);
                    }
                    catch(NullPointerException npe){
                        label+="<p text=\"#FF0000\"><b>" +organism+"</b> ** </p>";
                        for_update.push(organism);
                    }
                }
            }
            label.concat("</html>");
            labs.add(new JLabel(label));
        }
        
        
        
        
        message = new JLabel("(**) needs update to continue");
        message.setForeground(Color.red);
        Globals.update = new JButton("Update");
        Globals.update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Stack yes = (Stack) updated.clone();
                Stack no = (Stack) for_update.clone();
                
                updateframe frame = new updateframe(yes,no);
                Globals.openframe = true;
                
                Globals.update.setEnabled(false);
            }
        });
        
        organisms = new String[updated.size()+for_update.size()];
        updated.copyInto(organisms);
        for_update.copyInto(organisms);
        
        next = new JButton("Next");
        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(!Globals.openframe){
                        StatsSelection frame = new StatsSelection(organisms,true);
                    /*
                     * se auto to frame o xrhsths tha dinei ts parametrous 
                     * sxetika me ts SSRs kai ta statistika pou thelei na tou emfanistoun.
                     */
                    dispose();
                    
                }
            }
        });
        
        
        
        panel = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Previously selected species");
        panel.setBorder(title);
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();

        
        
        int i=0,j=0,element=0;
        
        for(j=0;j<columns;j++){
                a.fill = GridBagConstraints.CENTER;
                a.weightx = 0.5;
                a.weighty = 0.5;
                a.gridx = j;
                a.gridy = -1;
                panel.add(labs.get(element), a);
                element++;
        }
        if(rest!=0){
            a.fill = GridBagConstraints.CENTER;
            a.weightx = 0.5;
            a.weighty = 0.5;
            a.gridx = j;
            a.gridy = -1;
            panel.add(labs.get(element), a);
        }
        


        
        
        JPanel panel2 = new JPanel();
        
        
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        
        if(CheckForStar()){
            a.fill = GridBagConstraints.CENTER;
            a.weightx = 0.5;
            a.weighty = 0.5;
            a.gridx = 0;
            a.gridy = -2;
            panel2.add(message, a);
        }
        panel2.add(Globals.update, b);
        panel2.add(next, b);
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(panel, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = -1;
        add(panel2, c);
        
        
        setVisible(true);
        
         
        
    }
    
    private boolean CheckForStar(){
        boolean found=false;
        for(int i=0;i<labs.size();i++){
            if(labs.get(i).getText().contains("**")){
                found=true;
                break;
            }
        }
        return found;
    }
    
    private boolean CheckAllWithStar(){
        int count = 0;
        for(int i=0;i<labs.size();i++){
            if(labs.get(i).getText().contains("**")){
                count++;
            }
        }
        if (count==labs.size())
            return true;
        else
            return false;
    }
    
    
}
