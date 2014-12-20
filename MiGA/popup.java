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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class popup extends JFrame{
    private JPanel panel;
    private JLabel message;
    
    public popup(String title,String str,boolean flag){
        setTitle(title);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
	        catch (Exception e) {}
        message = new JLabel();
        message.setText(str);
        message.setVisible(true);
        
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();
        
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = 0;
        panel.add(message,a);
        
        add(panel);
        setAlwaysOnTop(true);
        setVisible(flag);
        
        Globals.openframe=false;
        
    }
    
}
