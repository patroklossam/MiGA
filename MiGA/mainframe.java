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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.*;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;

public class mainframe extends JFrame{

    private JLabel label;
    private JButton Next;
    private JLabel coop;

    public mainframe (){
        setTitle(" MiGA  (Microsatellite Genome Analysis)");
        setSize(600,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image im = Toolkit.getDefaultToolkit().getImage("ssr.png");
        this.setIconImage(im);
        
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
	        catch (Exception e) {}
        

        label=new JLabel("<html><h1>Welcome</h1></html>");

        Next = new JButton("Next");

        Next.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
                try {
                    input_frame frame = new input_frame();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(mainframe.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(mainframe.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
            }
        });
        
        coop = new JLabel("<html><p align=center><i>This program was written by<br/> Samaras Patroklos and <br/>Voulgaridis Antonis <br/>during their Thesis 2011-2012  </i></p></html> ");

        
        //δημιουργια πλεγματος και στοιχιση
        
        BackgroundPanel bg = new BackgroundPanel();
        
        GroupLayout bgLayout = new GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(Next)
                .addContainerGap(225, Short.MAX_VALUE))
            .addComponent(bg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Next)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        /*
        bg.setSize(600, 500);
        bg.setLayout(new GridBagLayout());
        GridBagConstraints a=new GridBagConstraints();

        a.fill=GridBagConstraints.CENTER;
	a.weightx=1;
        a.weighty=3;
        a.gridx=0;
	a.gridy=0;
        bg.add(label,a);
        
        a.fill=GridBagConstraints.PAGE_END;
	a.weightx=1;
        a.weighty=0.5;
        a.gridx=0;
	a.gridy=-1;
        bg.add(Next,a);

        a.fill=GridBagConstraints.CENTER;
	a.weightx=1;
        a.weighty=0.5;
        a.gridx=0;
	a.gridy=-2;
        bg.add(coop,a);
        
        /*
        setLayout(new GridBagLayout());
        GridBagConstraints b=new GridBagConstraints();
        b.fill=GridBagConstraints.HORIZONTAL;
	b.weightx=1;
        b.weighty=0.5;
        b.gridx=0;
	b.gridy=0;
        add(bg,b);

        b.fill=GridBagConstraints.PAGE_END;
	b.weightx=1;
        b.weighty=0.5;
        b.gridx=0;
	b.gridy=-1;
        add(Next,b);*/
        

        setVisible(true);
        
       


    }

    

}
