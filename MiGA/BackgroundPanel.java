

package MiGA;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import javax.swing.JPanel;

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
class BackgroundPanel extends JPanel{
    // The Image to store the background image in.
        Image img;
        public BackgroundPanel(){
            // Loads the background image and stores in img object.
            img = Toolkit.getDefaultToolkit().createImage("MiGAgif.gif");
        }

        public void paint(Graphics g){
            // Draws the img to the BackgroundPanel.
            g.drawImage(img, 0, 0, this);
        }
    }
