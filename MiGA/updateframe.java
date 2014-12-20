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
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class updateframe extends JFrame {

    private JButton update;
    private JButton back;
    private Stack organisms;
    private List<JCheckBox> species;
    private Process p;
    private String organism;
    private JCheckBox sp_box;
    private BufferedReader stdin;
    private JLabel label2;
    private String[] chromosomes;

    public updateframe(Stack updated, Stack notupdated) {
        setTitle("MiGA - Update selection");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Image im = Toolkit.getDefaultToolkit().getImage("ssr.png");
        this.setIconImage(im);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        this.setAlwaysOnTop(true);

        organisms = new Stack();
        //organisms = (Stack) updated.clone();

        species = new ArrayList<JCheckBox>();

        while (updated.isEmpty() == false) {
            String org = updated.pop().toString();
            Globals.species.add(org);
            sp_box = new JCheckBox(org + " ");
            species.add(sp_box);
        }


        while (notupdated.isEmpty() == false) {
            String org = notupdated.pop().toString();
            Globals.species.add(org);
            JCheckBox sp_box = new JCheckBox(org + " **");
            sp_box.setForeground(Color.red);
            //System.out.println(sp_box.getText());
            species.add(sp_box);
        }
        label2 = new JLabel("(**) needs update to continue");
        label2.setForeground(Color.red);



        update = new JButton("Update");
        update.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (checkSelected()) {
                    for (int i = 0; i < species.size(); i++) {
                        if (species.get(i).isSelected()) {
                            organism = species.get(i).getText();
                            String[] orgns = organism.split(" ");
                            organisms.push(orgns[0]);
                        }
                    }

                    while (organisms.isEmpty() == false) {
                        organism = organisms.pop().toString();


                        try {
                            p = Runtime.getRuntime().exec("cmd /c cd organisms && perl db_org_reset " + organism);
                            try {
                                p.waitFor();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE,
                                        null, ex);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE,
                                    null, ex);
                        }



                        try {
                            if (CheckForKaryotype(organism)) {
                                try {
                                    ChromsParser parse = new ChromsParser(organism);
                                    chromosomes = parse.getChroms();
                                    try {
                                        int j = Integer.parseInt(chromosomes[0]);
                                        for (int i = 0; i < chromosomes.length; i++) {
                                            if (i == 0 && j > 1) {
                                                int c = 1;
                                                while (c <= j) {
                                                    updateFromEnsembl(organism, Integer.toString(c++));
                                                }
                                            } else {
                                                updateFromEnsembl(organism, chromosomes[i]);
                                            }
                                        }
                                    } catch (NumberFormatException ex) {
                                        for (int i = 0; i < chromosomes.length; i++) {
                                            updateFromEnsembl(organism, chromosomes[i]);
                                        }
                                    }
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SQLException ex) {
                                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                File f = new File("organisms/" + organism + "/data/");
                                if (f.exists()) {
                                    deleteDir(f);
                                }
                                try {
                                    int j = Integer.parseInt(chromosomes[0]);
                                    for (int i = 0; i < chromosomes.length; i++) {
                                        if (i == 0 && j > 1) {
                                            int c = 1;
                                            while (c <= j) {
                                                try {
                                                    genomeforsearch(organism, Integer.toString(c));
                                                    c++;
                                                } catch (FileNotFoundException ex) {
                                                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        } else {
                                            try {
                                                genomeforsearch(organism, chromosomes[i]);
                                            } catch (FileNotFoundException ex) {
                                                Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (IOException ex) {
                                                Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    for (int i = 0; i < chromosomes.length; i++) {
                                        try {
                                            genomeforsearch(organism, chromosomes[i]);
                                        } catch (FileNotFoundException ex1) {
                                            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex1);
                                        } catch (IOException ex1) {
                                            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex1);
                                        }
                                    }
                                }
                            } else {
                                updateFromEnsembl(organism, null);
                                try {
                                    File f = new File("organisms/" + organism + "/data/");
                                    if (f.exists()) {
                                        deleteDir(f);
                                    }
                                    genomeforsearch(organism, null);
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                        }


                        try {
                            p = Runtime.getRuntime().exec("cmd /c cd organisms && perl db_org_update " + organism);
                            System.out.println("updating db");
                            try {
                                p.waitFor();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }


                    Globals.openframe = false;
                    dispose();
                }
            }
        });
        back = new JButton("Back");
        back.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Globals.update.setEnabled(true);
                Globals.openframe = false;
                dispose();
            }
        });



        JPanel panel = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Select for Update");
        panel.setBorder(title);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();


        int i = 0, j = 0;
        int element = 0;

        int columns = species.size() / 16;
        int rest = species.size() % 16;

        for (j = 0; j < columns; j++) {
            for (i = 0; i < 16; i++) {

                a.fill = GridBagConstraints.HORIZONTAL;
                a.weightx = 0.5;
                a.weighty = 0.5;
                a.gridx = j;
                a.gridy = -2 - i;
                panel.add(species.get(element), a);
                element++;
            }
        }
        for (int temp = 0; temp < rest; temp++) {
            a.fill = GridBagConstraints.HORIZONTAL;
            a.weightx = 0.5;
            a.weighty = 0.5;
            a.gridx = j;
            a.gridy = -2 - temp;
            panel.add(species.get(element), a);
            element++;
        }


        JPanel panel2 = new JPanel();

        panel2.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();

        a.fill = GridBagConstraints.CENTER;
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = -2;
        panel2.add(label2, a);

        panel2.add(update, b);
        panel2.add(back, b);


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

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    private boolean checkSelected() {
        boolean found = false;
        for (int i = 0; i < species.size(); i++) {
            if (species.get(i).isSelected()) {
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean CheckForKaryotype(String org) throws SQLException, ClassNotFoundException {

        boolean found = false;
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
        Statement st = con.createStatement();
        st.executeUpdate("use LoBiD");

        ResultSet check = st.executeQuery("SELECT karyotype FROM organism WHERE name = '" + org + "'");

        check.next();
        found = check.getBoolean(1);
        con.close();
        return found;
    }

    private void updateFromEnsembl(String org, String chrom) throws ClassNotFoundException, SQLException {
        boolean eof = false;
        try {
            if (chrom != null) {
                p = Runtime.getRuntime().exec("cmd /c cd organisms && perl chromosome_downloader " + org + " " + chrom);
            } else {
                p = Runtime.getRuntime().exec("cmd /c cd organisms && perl download_gene_for " + org);
            }

            try {
                p.waitFor();
                if (chrom != null) {
                    p = Runtime.getRuntime().exec("cmd /c cd organisms && perl chromosome_downloader_slice " + org + " " + chrom);
                    p.waitFor();
                }


                dispose();
            } catch (InterruptedException ex) {
                Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void genomeforsearch(String org, String chrom) throws FileNotFoundException, IOException {
        boolean eof = false;
        if (chrom != null) {
            stdin = new BufferedReader(new FileReader("organisms/" + org + "/chrom-" + chrom + "-slices.txt"));
            int line = 0;
            while (!eof) {
                String s = stdin.readLine();
                ++line;
                if (s == null) {
                    eof = true;
                    break;
                } else {
                    Sequence seq = new Sequence(s);
                    seq.toFile(org, chrom, line, true, null);
                }
            }

            stdin.close();
        } else {

            String loc = "organisms/" + org + "/slice-1.txt";
            File file = new File(loc);
            int slice = 1;
            while (file.exists()) {
                String s = "";
                eof = false;

                stdin = new BufferedReader(new FileReader(loc));
                int line = 0;
                while (!eof) {

                    s = stdin.readLine();
                    if (s != null) {
                        line++;
                        Sequence seq = new Sequence(s);
                        seq.toFile(org, Integer.toString(slice), line, true, null);
                    } else {
                        eof = true;
                        slice++;
                        loc = "organisms/" + org + "/slice-" + slice + ".txt";
                        file = new File(loc);
                        stdin.close();
                    }
                }
            }

        }


    }
}
