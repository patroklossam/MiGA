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

import matrixparser.parser;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.List;

import matrixparser.parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.commons.lang3.StringUtils;

public class StatsSelection extends JFrame {

    private parser matrix;
    
    private JLabel select;
    private JLabel minimumssrlen;
    private JLabel gapmax;
    private JLabel gapcomp;
    private JLabel minlenpregap;
    private JCheckBox box1;
    private JCheckBox box2;
    private JCheckBox box3;
    private JCheckBox com;
    private JCheckBox incom;
    private JTextField score;
    private JTextField max;
    private JTextField minpregap;
    private JTextField maxgapcomp;
    private JPanel panel1;
    private JButton show;
    private JOptionPane msg;
    private JPanel panel2;
    private JPanel panelup;
    private JPanel paneldown;
    private String[] chromosomes;
    private List<String> chromosomelist;
    
    // 18/11/2013 added indexer to reduce RAM usage
    private Indexer indexer;
    
    private List<String> SSR;
    private List<Integer> repeats;
    private List<Integer> EndOfSsr;
    private List<Integer> start;
    private JButton quit;
    private JButton selectsp;
    private JPanel paneldownleft;
    private JPanel paneldownright;
    private JCheckBox mono;
    private JCheckBox di;
    private JCheckBox tri;
    private JCheckBox tetra;
    private JCheckBox penta;
    private JCheckBox hexa;
    private Stack nums;
    private JPanel paneltop;
    private JTabbedPane tab;
    private JPanel panelshow;
    private JLabel startlab;
    private JLabel endlab;
    private JLabel titlelab;
    private JTextField startnum;
    private JTextField endnum;
    private JTextField titlef;
    private JCheckBox flk;
    private JLabel flankst;
    private JTextField flankstn;
    private JLabel flankend;
    private JTextField flankendn;
    private JButton retrieve;
    private JTextArea result;
    private JScrollPane sbrText;
    private JLabel lab;
    private JPanel panelshowup;
    private JPanel panelshowd;
    private List<Integer> countmono;
    private List<Integer> countdi;
    private List<Integer> counttri;
    private List<Integer>  counttetra;
    private List<Integer>  countpenta;
    private List<Integer>  counthexa;
    private List<Integer>  countmonore;
    private List<Integer>  countdire;
    private List<Integer>  counttrire;
    private List<Integer>  counttetrare;
    private List<Integer>  countpentare;
    private List<Integer>  counthexare;
    private List<Integer>  Amono, Tmono, Gmono, Cmono;
    private List<Integer>  Adi, Tdi, Gdi, Cdi;
    private List<Integer>  Atri, Ttri, Gtri, Ctri;
    private List<Integer>  Atetra, Ttetra, Gtetra, Ctetra;
    private List<Integer>  Apenta, Tpenta, Gpenta, Cpenta;
    private List<Integer>  Ahexa, Thexa, Ghexa, Chexa;
    private Calendar calendar;
    private Date now;
    
    private HashMap<String,motifStats> map;
    
    // standardization
    private JPanel std;
    private ButtonGroup standard;
    private JRadioButton no_st;
    private JRadioButton part_st;
    private JRadioButton full_st;
    
    //about us components
    
    private JPanel about;
    private JLabel contact;
    
    

    public StatsSelection(final String[] organisms, final boolean flag) {

        setTitle("MiGA");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image im = Toolkit.getDefaultToolkit().getImage("ssr.png");
        this.setIconImage(im);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        
        countmono = new ArrayList<Integer>();
        countdi = new ArrayList<Integer>();
        counttri = new ArrayList<Integer>();
        counttetra = new ArrayList<Integer>();
        countpenta = new ArrayList<Integer>();
        counthexa = new ArrayList<Integer>();
        countmonore = new ArrayList<Integer>();
        countdire = new ArrayList<Integer>();
        counttrire = new ArrayList<Integer>();
        counttetrare = new ArrayList<Integer>();
        countpentare = new ArrayList<Integer>();
        counthexare = new ArrayList<Integer>();
        Amono = new ArrayList<Integer>(); Tmono = new ArrayList<Integer>(); Gmono = new ArrayList<Integer>(); Cmono = new ArrayList<Integer>();
        Adi = new ArrayList<Integer>(); Tdi = new ArrayList<Integer>(); Gdi = new ArrayList<Integer>(); Cdi = new ArrayList<Integer>();
        Atri = new ArrayList<Integer>(); Ttri = new ArrayList<Integer>(); Gtri = new ArrayList<Integer>(); Ctri = new ArrayList<Integer>();
        Atetra = new ArrayList<Integer>(); Ttetra = new ArrayList<Integer>(); Gtetra = new ArrayList<Integer>(); Ctetra = new ArrayList<Integer>();
        Apenta = new ArrayList<Integer>(); Tpenta = new ArrayList<Integer>(); Gpenta = new ArrayList<Integer>(); Cpenta = new ArrayList<Integer>();
        Ahexa = new ArrayList<Integer>(); Thexa = new ArrayList<Integer>(); Ghexa = new ArrayList<Integer>(); Chexa = new ArrayList<Integer>();
        
        for(int i=0;i<organisms.length;i++){
            countmono.add(0);
            countdi.add(0);
            counttri.add(0);
            counttetra.add(0);
            countpenta.add(0);
            counthexa.add(0);
            countmonore.add(0);
            countdire.add(0);
            counttrire.add(0);
            counttetrare.add(0);
            countpentare.add(0);
            counthexare.add(0);
            Amono.add(0); Tmono.add(0); Gmono.add(0); Cmono.add(0);
            Adi.add(0); Tdi.add(0); Gdi.add(0); Cdi.add(0);
            Atri.add(0); Ttri.add(0); Gtri.add(0); Ctri.add(0);
            Atetra.add(0); Ttetra.add(0); Gtetra.add(0); Ctetra.add(0);
            Apenta.add(0); Tpenta.add(0); Gpenta.add(0); Cpenta.add(0);
            Ahexa.add(0); Thexa.add(0); Ghexa.add(0); Chexa.add(0);
        }
        

        lab = new JLabel("<html><b><p>To retrieve the sequence you want</p><p>simply copy and paste in the fields below</p><p>the data you were given in your result's file</p></b></html>");

        startlab = new JLabel("Start:");
        endlab = new JLabel("End:");
        titlelab = new JLabel("Chromosome or field:");

        startnum = new JTextField();
        startnum.setColumns(5);
        endnum = new JTextField();
        endnum.setColumns(5);
        titlef = new JTextField();
        titlef.setColumns(30);

        flankst = new JLabel("Flanking region before: ");
        flankst.setEnabled(false);
        flankst.setVisible(false);
        flankend = new JLabel("Flanking region after: ");
        flankend.setEnabled(false);
        flankend.setVisible(false);

        flankstn = new JTextField();
        flankstn.setColumns(5);
        flankstn.setEnabled(false);
        flankstn.setVisible(false);

        flankendn = new JTextField();
        flankendn.setColumns(5);
        flankendn.setEnabled(false);
        flankendn.setVisible(false);


        result = new JTextArea("", 6, 90);
        result.setText(" ");
        result.setEditable(false);
        result.setLineWrap(true);
        result.setAutoscrolls(true);
        sbrText = new JScrollPane(result);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        flk = new JCheckBox("Flanking Regions");
        flk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (flk.isSelected()) {
                    flankst.setEnabled(true);
                    flankst.setVisible(true);
                    flankend.setEnabled(true);
                    flankend.setVisible(true);
                    flankstn.setEnabled(true);
                    flankstn.setVisible(true);
                    flankendn.setEnabled(true);
                    flankendn.setVisible(true);

                }
                if (!flk.isSelected()) {
                    flankst.setEnabled(false);
                    flankst.setVisible(false);
                    flankend.setEnabled(false);
                    flankend.setVisible(false);
                    flankstn.setEnabled(false);
                    flankstn.setVisible(false);
                    flankendn.setEnabled(false);
                    flankendn.setVisible(false);


                }
            }
        });



        retrieve = new JButton("Retrieve");
        retrieve.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int start = 0;
                int end = 0;

                BufferedReader in = null;
                String location = "";
                String newlocation = "";
                String bufferpre = "";
                String bufferpost = "";
                String pre = "";
                String post = "";
                String mid = "";
                try {
                    String[] locationarray = titlef.getText().split("data/");
                    location = locationarray[0] + locationarray[1];
                    newlocation = "";
                    if (location.contains("local")) {
                        in = new BufferedReader(new FileReader(location + ".txt"));
                    } else if (location.contains("organisms")) {
                        String[] loc = location.split("/");
                        try {
                            if (CheckForKaryotype(loc[1])) {
                                newlocation = loc[0] + "/" + loc[1] + "/chrom-" + loc[2] + "-slices.txt";
                            } else {
                                newlocation = loc[0] + "/" + loc[1] + "/slice-" + loc[2] + ".txt";
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        in = new BufferedReader(new FileReader(newlocation));
                    }
                } catch (FileNotFoundException ex) {
                    msg.showMessageDialog(paneldown, "Wrong field", "Error", JOptionPane.ERROR_MESSAGE);
                }
                int rest = Integer.parseInt(startnum.getText()) % 20000;
                int lines = Integer.parseInt(startnum.getText()) / 20000;
                lines++;
                String buffer1 = "";

                for (int c = 0; c < lines; c++) {
                    try {
                        buffer1 = in.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (flk.isSelected()) {
                    boolean tfs = false, tfe = false;
                    int fs = 0, fe = 0;
                    try {
                        fs = Integer.parseInt(flankstn.getText());
                        tfs = true;
                    } catch (NumberFormatException ex) {
                        tfs = false;
                    }

                    try {
                        fe = Integer.parseInt(flankendn.getText());
                        tfe = true;
                    } catch (NumberFormatException ex) {
                        tfe = false;
                    }
                    if (tfs && tfe) {

                        start = rest - Integer.parseInt(flankstn.getText());
                        end = rest + Integer.parseInt(endnum.getText()) - Integer.parseInt(startnum.getText()) + Integer.parseInt(flankendn.getText());
                        try {
                            in = new BufferedReader(new FileReader(newlocation));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (start < 0 && lines == 1) {
                            start = 0;
                        } else if (start < 0 && lines > 1) {
                            for (int j = 0; j < lines - 1; j++) {
                                try {
                                    bufferpre = in.readLine();
                                } catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            pre = bufferpre.substring(20000 + start);
                            mid = buffer1.substring(0, end);
                            try {
                                in.close();
                            } catch (IOException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        int cl = 0;
                        try {
                            cl = countlines(newlocation);
                        } catch (IOException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            in = new BufferedReader(new FileReader(newlocation));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (end > 20000 && lines == cl - 1) {
                            if (end - 20000 >= rest) {
                                end = rest;
                                mid = buffer1.substring(start);
                                post = bufferpost;
                            } else {
                                for (int j = 0; j < lines - 1; j++) {
                                    try {
                                        bufferpost = in.readLine();
                                    } catch (IOException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                mid = buffer1.substring(start);
                                post = bufferpre.substring(0, end - 20000);
                                try {
                                    in.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        } else if (end > 20000 && lines < cl - 1) {
                            for (int j = 0; j < lines + 1; j++) {
                                try {
                                    bufferpost = in.readLine();
                                } catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            mid = buffer1.substring(start);
                            post = bufferpost.substring(0, end - 20000);
                            try {
                                in.close();
                            } catch (IOException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        if (start >= 0 && (end <= 20000 || end <= rest)) {
                            mid = buffer1.substring(start, end);
                        }

                    } else {
                        if (!tfs) {
                            msg.showMessageDialog(paneldown, "Flanking region start is empty.\nFill in the gap or uncheck the\nflanking regions checkbox", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        if (!tfe) {
                            msg.showMessageDialog(paneldown, "Flanking region end is empty.\nFill in the gap or uncheck the\nflanking regions checkbox", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    //
                }
                if (!flk.isSelected()) {
                    start = rest;
                    end = rest + Integer.parseInt(endnum.getText()) - Integer.parseInt(startnum.getText());
                    try {
                        in = new BufferedReader(new FileReader(newlocation));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (start < 0 && lines == 1) {
                        start = 0;
                    } else if (start < 0 && lines > 1) {
                        for (int j = 0; j < lines - 1; j++) {
                            try {
                                bufferpre = in.readLine();
                            } catch (IOException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        pre = bufferpre.substring(start);
                        mid = buffer1.substring(0, end);
                        try {
                            in.close();
                        } catch (IOException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    int cl = 0;
                    try {
                        cl = countlines(newlocation);
                    } catch (IOException ex) {
                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        in = new BufferedReader(new FileReader(newlocation));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (end > 20000 && lines == cl - 1) {
                        if (end - 20000 >= rest) {
                            end = rest;
                            mid = buffer1.substring(start);
                            post = bufferpost;
                        } else {
                            for (int j = 0; j < lines - 1; j++) {
                                try {
                                    bufferpost = in.readLine();
                                } catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            mid = buffer1.substring(start);
                            post = bufferpre.substring(0, end - 20000);
                            try {
                                in.close();
                            } catch (IOException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else if (end > 20000 && lines < cl - 1) {
                        for (int j = 0; j < lines + 1; j++) {
                            try {
                                bufferpost = in.readLine();
                            } catch (IOException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        mid = buffer1.substring(start);
                        post = bufferpost.substring(0, end - 20000);
                        try {
                            in.close();
                        } catch (IOException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (start >= 0 && (end <= 20000 || end <= rest)) {
                        mid = buffer1.substring(start, end);
                    }
                }

                result.setText(pre + mid + post);
            }
        });



        mono = new JCheckBox("Mononucleotide");
        di = new JCheckBox("Dinucleotide");
        tri = new JCheckBox("Trinucleotide");
        tetra = new JCheckBox("Tetranucleotide");
        penta = new JCheckBox("Pentanucleotide");
        hexa = new JCheckBox("Hexanucleotide");

        SSR = new ArrayList<String>();
        repeats = new ArrayList<Integer>();
        EndOfSsr = new ArrayList<Integer>();
        start = new ArrayList<Integer>();

        select = new JLabel("Select type: ");


        minimumssrlen = new JLabel("Minimum SSR length(bp)");
        minimumssrlen.setVisible(false);
        score = new JTextField();
        score.setColumns(5);
        score.setVisible(false);


        msg = new JOptionPane();


        gapmax = new JLabel("Maximum Mismatch length for Imperfect SSRs(bp)");
        gapmax.setVisible(false);
        max = new JTextField();
        max.setColumns(5);
        max.setVisible(false);

        minlenpregap = new JLabel("Minimum SSR length before given Mismatch length(bp)");
        minlenpregap.setVisible(false);
        minpregap = new JTextField();
        minpregap.setColumns(5);
        minpregap.setVisible(false);


        gapcomp = new JLabel("Maximum Inter-repeat R for Compound SSRs(bp)");
        gapcomp.setVisible(false);
        maxgapcomp = new JTextField();
        maxgapcomp.setColumns(5);
        maxgapcomp.setVisible(false);



        box1 = new JCheckBox("Perfect");
        box2 = new JCheckBox("Imperfect");
        box3 = new JCheckBox("Compound");
        com = new JCheckBox("Perfect Compound");
        incom = new JCheckBox("Imperfect Compound");

        box1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (box2.isSelected() || box3.isSelected()) {
                    if (box1.isSelected()) {
                        std.setEnabled(true);
                        no_st.setEnabled(true);
                        part_st.setEnabled(true);
                        full_st.setEnabled(true);
        
                        if (!box3.isSelected()) {
                            minimumssrlen.setVisible(true);
                            score.setVisible(true);
                        }
                    }
                    if (!box1.isSelected()) {
                        std.setEnabled(false);
                        no_st.setEnabled(false);
                        part_st.setEnabled(false);
                        full_st.setEnabled(false);
                        if (!box3.isSelected()) {
                            minimumssrlen.setVisible(false);
                            score.setVisible(false);
                        }
                    }
                } else {
                    if (box1.isSelected()) {
                        std.setEnabled(true);
                        no_st.setEnabled(true);
                        part_st.setEnabled(true);
                        full_st.setEnabled(true);
                        panel2.setVisible(true);
                        minimumssrlen.setVisible(true);
                        score.setVisible(true);
                    }
                    if (!box1.isSelected()) {
                        std.setEnabled(false);
                        no_st.setEnabled(false);
                        part_st.setEnabled(false);
                        full_st.setEnabled(false);
                        panel2.setVisible(false);
                        minimumssrlen.setVisible(false);
                        score.setVisible(false);
                    }

                }
            }
        });

        box2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (box1.isSelected() || box3.isSelected()) {
                    if (box2.isSelected()) {
                        gapmax.setVisible(true);
                        max.setVisible(true);

                        minlenpregap.setVisible(true);
                        minpregap.setVisible(true);
                    }
                    if (!box2.isSelected()) {

                        gapmax.setVisible(false);
                        max.setVisible(false);

                        minlenpregap.setVisible(false);
                        minpregap.setVisible(false);

                    }
                } else {
                    if (box2.isSelected()) {
                        panel2.setVisible(true);
                        gapmax.setVisible(true);
                        max.setVisible(true);

                        minlenpregap.setVisible(true);
                        minpregap.setVisible(true);
                    }
                    if (!box2.isSelected()) {
                        panel2.setVisible(false);
                        gapmax.setVisible(false);
                        max.setVisible(false);

                        minlenpregap.setVisible(false);
                        minpregap.setVisible(false);
                    }
                }
            }
        });

        box3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (box1.isSelected() || box2.isSelected()) {
                    if (box3.isSelected()) {
                        paneldownleft.setVisible(true);
                        if (!box1.isSelected()) {
                            minimumssrlen.setVisible(true);
                            score.setVisible(true);
                        }
                        gapcomp.setVisible(true);
                        maxgapcomp.setVisible(true);
                        com.setVisible(true);
                        incom.setVisible(true);

                    }
                    if (!box3.isSelected()) {
                        paneldownleft.setVisible(false);
                        gapcomp.setVisible(false);
                        maxgapcomp.setVisible(false);
                        if (!box1.isSelected()) {
                            minimumssrlen.setVisible(false);
                            score.setVisible(false);
                            com.setVisible(false);
                            incom.setVisible(false);
                        }
                    }
                } else {
                    if (box3.isSelected()) {
                        paneldownleft.setVisible(true);
                        panel2.setVisible(true);
                        minimumssrlen.setVisible(true);
                        score.setVisible(true);

                        gapcomp.setVisible(true);
                        maxgapcomp.setVisible(true);
                        com.setVisible(true);
                        incom.setVisible(true);
                    }
                    if (!box3.isSelected()) {
                        paneldownleft.setVisible(false);
                        panel2.setVisible(false);
                        minimumssrlen.setVisible(false);
                        score.setVisible(false);
                        gapcomp.setVisible(false);
                        maxgapcomp.setVisible(false);
                        com.setVisible(false);
                        incom.setVisible(false);
                    }
                }
            }
        });
        /*
        incom.addActionListener(new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
        if (incom.isSelected()) {
        if (!box2.isSelected()) {
        gapmax.setVisible(true);
        max.setVisible(true);
        
        minlenpregap.setVisible(true);
        minpregap.setVisible(true);
        }
        }
        if (!incom.isSelected()) {
        if (!box2.isSelected()) {
        gapmax.setVisible(false);
        max.setVisible(false);
        
        minlenpregap.setVisible(false);
        minpregap.setVisible(false);
        }
        }
        }
        });*/
        
        std = new JPanel();
        no_st = new JRadioButton("Not Standardized");
        part_st = new JRadioButton("Partial Standardized");
        full_st = new JRadioButton("Full Standardized");
        no_st.setSelected(true);
        no_st.setEnabled(false);
        part_st.setEnabled(false);
        full_st.setEnabled(false);
        
        standard = new ButtonGroup();
        standard.add(no_st);
        standard.add(part_st);
        standard.add(full_st);

        show = new JButton("Run");
        show.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {


                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                if (!box1.isSelected() && !box2.isSelected() && !box3.isSelected()) {
                    msg.showMessageDialog(paneldown, "Please select a type", "Error", JOptionPane.ERROR_MESSAGE);
                }
                for (int i = 0; i < organisms.length; i++) {
                    File f = new File("organisms/" + organisms[i] + "/stats/");
                    if (f.exists()) {
                        f.delete();
                    }
                }

                calendar = Calendar.getInstance();
                now = calendar.getTime();
                if (box1.isSelected()) {
                    if (!score.getText().isEmpty()) {
                        boolean isnumber = false;
                        int minlen = 0;
                        try {
                            minlen = Integer.parseInt(score.getText());
                            isnumber = true;
                        } catch (NumberFormatException ex) {
                            isnumber = false;
                        }
                        if (isnumber) {
                            try {
                                getPerfectSSRs(organisms, minlen, flag);

                                for (int i = 0; i < organisms.length; i++) {
                                    
                                    map = new HashMap<String,motifStats>();
                                    
                                    String location = "";
                                    String location2 = "";
                                    PrintWriter out = null;
                                    
                                    // 18/11/2013 added starting here
                                    String filetype = "";
                                    String filepro = "";
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                    //List<String> files = getFiles(organisms[i], minlen, flag);
                                    
                                    
                                    // 18/11/2013 added ending here
                                    PrintWriter stats = null;
                                    PrintWriter html = null;
                                    PrintWriter motifstats = null;
                                    PrintWriter motifhtml = null;
                                    DataOutputStream lt=null;
                                    if (filetype.contains("organisms")) {
                                        
                                        
                                        File f = new File("organisms/" + organisms[i] + "/stats/");
                                        if (!f.exists()) {
                                            f.mkdir();
                                        }
                                        
                                        stats = new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                                        motifstats = new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "motif_statistics" +  now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                                        motifhtml = new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "motif_statistics" +  now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                        
                                        html =  new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                         
                                        lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".perf")));
                                        
                                        File fi = new File("organisms/" + organisms[i] + "/results/");
                                        if (!fi.exists()) {
                                            fi.mkdir();
                                        }
                                        String toopen = "organisms/" + organisms[i] + "/results/allPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                                        location = toopen;
                                        location2 = "organisms/" + organisms[i] + "/stats/" + "motif_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                                        out = new PrintWriter(toopen);

                                        out.println("Results for organism: " + organisms[i] + "\t Search Parameters --> Minimum SSR Length (bp): " + minlen);
                                        out.println("   SSR      repeats             start-end  length  Path(../organism/data/chromosome)");
                                        
                                        
                                    } else if (filetype.contains("local")) {
                                        File f = new File("local/" + organisms[i] + "/stats/");
                                        if (!f.exists()) {
                                            f.mkdir();
                                        }

                                        stats = new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                                        motifstats = new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "motif_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                                        motifhtml = new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "motif_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                        lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".perf")));
                                        html =  new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                        
                                        
                                        File fi = new File("local/" + organisms[i] + "/results/");
                                        if (!fi.exists()) {
                                            fi.mkdir();
                                        }
                                        String toopen = "local/" + organisms[i] + "/results/allPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                                        location = toopen;
                                        location2 = "local/" + organisms[i] + "/stats/" + "motif_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                                        out = new PrintWriter(toopen);

                                        out.println("Results for project: " + organisms[i] + "\t Search Parameters --> Minimum SSR Length (bp): " + minlen);
                                        out.println("   SSR      repeats             start-end  length  Path(../organism/data/chromosome)");
                                        out.println();
                                    }


                                    if (mono.isSelected()) {
                                        
                                        
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                    //List<String> files = getFiles(organisms[i], minlen, flag);
                                        //for (int j = 0; j < files.size(); j++) {
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                            
                                            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files  + "_" + minlen + "_monoPerfect.temp")));
                                            boolean eof = false;
                                            while (!eof) {
                                                try {
                                                    
                                                    String ssr = in.readUTF();
                                                    int repeats = in.readInt();
                                                    int end = in.readInt();
                                                    out.println(cell(ssr,6)+"  " + cell(repeats,11) + "  "+cell(Integer.toString(end - repeats * ssr.length()+1)+"-"+Integer.toString(end+1),20)+"  "+cell(repeats * ssr.length(),6) +"  " +files.substring(0, files.lastIndexOf('.')));
                                               
                                                    // map for motifstats 
                                                    if(!map.containsKey(ssr)){
                                                        motifStats m = new motifStats(ssr,repeats);
                                                        map.put(ssr, m);
                                                    }else{
                                                        map.get(ssr).update(repeats);
                                                    }
                                                
                                                } catch (EOFException exc) {
                                                    eof = true;
                                                }
                                            }
                                            in.close();
                                        }
                                    }
                                    if (di.isSelected()) {
                                        //for (int j = 0; j < files.size(); j++) {
                                        
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                    //List<String> files = getFiles(organisms[i], minlen, flag);
                                         while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        
                                            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + minlen + "_diPerfect.temp")));
                                            boolean eof = false;
                                            while (!eof) {
                                                try {
                                                    String ssr = in.readUTF();
                                                    int repeats = in.readInt();
                                                    int end = in.readInt();
                                                    //out.println("SSR: " + ssr + " repeats: " + repeats + " start-end " + (end - repeats * ssr.length()) + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                                    out.println(cell(ssr,6)+"  " + cell(repeats,11) + "  "+cell(Integer.toString(end - repeats * ssr.length()+1)+"-"+Integer.toString(end+1),20)+"  "+cell(repeats * ssr.length(),6) +"  " +files.substring(0, files.lastIndexOf('.')));
                                                
                                                    if(!map.containsKey(ssr)){
                                                        motifStats m = new motifStats(ssr,repeats);
                                                        map.put(ssr, m);
                                                    }else{
                                                        map.get(ssr).update(repeats);
                                                    }
                                                
                                                } catch (EOFException exc) {
                                                    eof = true;
                                                }
                                            }
                                            in.close();
                                        }
                                    }
                                    if (tri.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                        //for (int j = 0; j < files.size(); j++) {
                                         while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        
                                            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + minlen + "_triPerfect.temp")));
                                            boolean eof = false;
                                            while (!eof) {
                                                try {
                                                    String ssr = in.readUTF();
                                                    int repeats = in.readInt();
                                                    int end = in.readInt();
                                                    //out.println("SSR: " + ssr + " repeats: " + repeats + " start-end " + (end - repeats * ssr.length()) + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                                    out.println(cell(ssr,6)+"  " + cell(repeats,11) + "  "+cell(Integer.toString(end - repeats * ssr.length()+1)+"-"+Integer.toString(end+1),20)+"  "+cell(repeats * ssr.length(),6) +"  " +files.substring(0, files.lastIndexOf('.')));
                                                
                                                    if(!map.containsKey(ssr)){
                                                        motifStats m = new motifStats(ssr,repeats);
                                                        map.put(ssr, m);
                                                    }else{
                                                        map.get(ssr).update(repeats);
                                                    }
                                                
                                                } catch (EOFException exc) {
                                                    eof = true;
                                                }
                                            }
                                            in.close();
                                        }
                                    }
                                    if (tetra.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                         while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        
                                            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + minlen + "_tetraPerfect.temp")));
                                            boolean eof = false;
                                            while (!eof) {
                                                try {
                                                    String ssr = in.readUTF();
                                                    int repeats = in.readInt();
                                                    int end = in.readInt();
                                                   // out.println("SSR: " + ssr + " repeats: " + repeats + " start-end " + (end - repeats * ssr.length()) + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                                    out.println(cell(ssr,6)+"  " + cell(repeats,11) + "  "+cell(Integer.toString(end - repeats * ssr.length()+1)+"-"+Integer.toString(end+1),20)+"  "+cell(repeats * ssr.length(),6) +"  " +files.substring(0, files.lastIndexOf('.')));
                                                
                                                    if(!map.containsKey(ssr)){
                                                        motifStats m = new motifStats(ssr,repeats);
                                                        map.put(ssr, m);
                                                    }else{
                                                        map.get(ssr).update(repeats);
                                                    }
                                                
                                                } catch (EOFException exc) {
                                                    eof = true;
                                                }
                                            }
                                            in.close();
                                        }
                                    }
                                    if (penta.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                         while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        
                                            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + minlen + "_pentaPerfect.temp")));
                                            boolean eof = false;
                                            while (!eof) {
                                                try {
                                                    String ssr = in.readUTF();
                                                    int repeats = in.readInt();
                                                    int end = in.readInt();
                                                  //  out.println("SSR: " + ssr + " repeats: " + repeats + " start-end " + (end - repeats * ssr.length()) + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                                    out.println(cell(ssr,6)+"  " + cell(repeats,11) + "  "+cell(Integer.toString(end - repeats * ssr.length()+1)+"-"+Integer.toString(end+1),20)+"  "+cell(repeats * ssr.length(),6) +"  " +files.substring(0, files.lastIndexOf('.')));
                                                
                                                    if(!map.containsKey(ssr)){
                                                        motifStats m = new motifStats(ssr,repeats);
                                                        map.put(ssr, m);
                                                    }else{
                                                        map.get(ssr).update(repeats);
                                                    }
                                                
                                                } catch (EOFException exc) {
                                                    eof = true;
                                                }
                                            }
                                            in.close();
                                        }
                                    }
                                    if (hexa.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                         while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        
                                            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + minlen + "_hexaPerfect.temp")));
                                            boolean eof = false;
                                            while (!eof) {
                                                try {
                                                    String ssr = in.readUTF();
                                                    int repeats = in.readInt();
                                                    int end = in.readInt();
                                                   // out.println("SSR: " + ssr + " repeats: " + repeats + " start-end " + (end - repeats * ssr.length()) + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                                    out.println(cell(ssr,6)+"  " + cell(repeats,11) + "  "+cell(Integer.toString(end - repeats * ssr.length()+1)+"-"+Integer.toString(end+1),20)+"  "+cell(repeats * ssr.length(),6) +"  " +files.substring(0, files.lastIndexOf('.')));
                                                
                                                    if(!map.containsKey(ssr)){
                                                        motifStats m = new motifStats(ssr,repeats);
                                                        map.put(ssr, m);
                                                    }else{
                                                        map.get(ssr).update(repeats);
                                                    }
                                                
                                                } catch (EOFException exc) {
                                                    eof = true;
                                                }
                                            }
                                            in.close();
                                        }
                                    }

                                    out.close();
                                    Runtime.getRuntime().exec("notepad " + location);
                                    
                                    DecimalFormat round = new DecimalFormat("#.###");
                                    
                                    html.println("<html><h1>******* Perfect SSRs *******</h1>");
                                    html.println("<h4>Results for project: " + organisms[i] + "</h4><h4>Search Parameters --> Minimum SSR Length (bp): " + minlen +"</h4>"); 
                                    html.println("<table border=\"1\"><tr><td><b>motif</b></td><td><b>count</b></td><td><b>bp</b></td><td><b>A%</b></td><td><b>T%</b></td><td><b>C%</b></td><td><b>G%</b></td><td><b>Relative Frequency</b></td><td><b>Abundance</b></td><td><b>Relative Abundance</b></td></tr>");
                                    stats.println("******* Perfect SSRs *******");
                                    stats.println("Results for project: " + organisms[i] + "\n Search Parameters --> Minimum SSR Length (bp): " + minlen);

                                    stats.println(" ____________________________________________________________________________________________________________ ");
                                    stats.println("|       |       |            |       |       |       |       |   Relative    |               |   Relative    |");
                                    stats.println("| motif | count |     bp     |   A%  |   T%  |   C%  |   G%  |   Frequency   |   Abundance   |   Abundance   |");
                                    stats.println("|=======|=======|============|=======|=======|=======|=======|===============|===============|===============|");
                                    int totalcount = 0;
                                    long bpcount = 0;
                                    int Aperc = 0;
                                    int Tperc = 0;
                                    int Gperc = 0;
                                    int Cperc = 0;
                                    float relfreq = 0;
                                    float abfreq = 0;
                                    long seqcount = 0;
                                    if (mono.isSelected()) {
                                        totalcount += countmono.get(i);
                                        bpcount += countmonore.get(i);
                                    }
                                    if (di.isSelected()) {
                                        totalcount += countdi.get(i);
                                        bpcount += countdire.get(i) * 2;
                                    }
                                    if (tri.isSelected()) {
                                        totalcount += counttri.get(i);
                                        bpcount += counttrire.get(i) * 3;
                                    }
                                    if (tetra.isSelected()) {
                                        totalcount += counttetra.get(i);
                                        bpcount += counttetrare.get(i) * 4;
                                    }
                                    if (penta.isSelected()) {
                                        totalcount += countpenta.get(i);
                                        bpcount += countpentare.get(i) * 5;
                                    }
                                    if (hexa.isSelected()) {
                                        totalcount += counthexa.get(i);
                                        bpcount += counthexare.get(i) * 6;
                                    }
                                    try {
                                        Class.forName("com.mysql.jdbc.Driver");
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Connection con = null;
                                    try {
                                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
                                    } catch (SQLException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Statement st = null;
                                    try {
                                        st = con.createStatement();
                                    } catch (SQLException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    st.executeUpdate("use lobid");
                                    
                                    seqcount=0;
                                    
                                    if (filetype.contains("organisms")) {
                                        ResultSet rs = st.executeQuery("SELECT end FROM slices INNER JOIN organism WHERE slices.org_id=organism.org_id AND organism.name='" + organisms[i] + "'");
                                        while (rs.next()) {
                                            seqcount += Long.parseLong(rs.getString(1));
                                        }
                                    } else if (filetype.contains("local")) {
                                        BufferedReader in = new BufferedReader(new FileReader("local/" + organisms[i] + "/index.txt"));
                                        int count = countlines("local/" + organisms[i] + "/index.txt");
                                        for (int c = 0; c < count; c++) {
                                            String temp = in.readLine();
                                            BufferedReader tmp = new BufferedReader(new FileReader("local/" + organisms[i] + "/" + temp + ".txt"));
                                            
                                            boolean eof=false;
                                            while (!eof) {
                                                String s = tmp.readLine();
                                                if(s!=null){
                                                    seqcount+=s.length();
                                                }
                                                else{
                                                    eof = true;
                                                }
                                            }
                                            tmp.close();
                                        }
                                    }
                                    
                                    



                                    if (mono.isSelected()) {
                                        Aperc += Amono.get(i);
                                        Tperc += Tmono.get(i);
                                        Gperc += Gmono.get(i);
                                        Cperc += Cmono.get(i);
                                        //lt.writeInt(countmono);lt.writeInt(countmonore);lt.writeFloat((float)Amono*100/countmonore);lt.writeFloat((float)Tmono*100/countmonore);lt.writeFloat((float)Gmono*100/countmonore);lt.writeFloat((float)Cmono*100/countmonore);lt.writeFloat((float) countmono / totalcount);lt.writeFloat((float) countmonore / seqcount);lt.writeFloat((float) countmonore / bpcount);
                                        stats.printf("|mono   |" + cell(Integer.toString(countmono.get(i)), 7) + "|" + cell(Integer.toString(1 * countmonore.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) countmono.get(i) / totalcount, 15) + "|" + cell((float) countmonore.get(i) / seqcount, 15) + "|" + cell((float) countmonore.get(i) / bpcount, 15) + "|\n", cell((float) (Amono.get(i) * 100) / (countmonore.get(i)), 7), cell((float) (Tmono.get(i) * 100) / (countmonore.get(i)), 7), cell((float) (Cmono.get(i) * 100) / (countmonore.get(i)), 7), cell((float) (Gmono.get(i) * 100) / (countmonore.get(i)), 7));
                                        stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                        html.println("<tr><td><b>mono</b></td><td>"+countmono.get(i)+"</td><td>"+(1 * countmonore.get(i))+"</td><td>"+round.format((float) (Amono.get(i) * 100) / (countmonore.get(i)))+"</td><td>"+round.format((float) (Tmono.get(i) * 100) / (countmonore.get(i)))+"</td><td>"+round.format((float) (Cmono.get(i) * 100) / (countmonore.get(i)))+"</td><td>"+round.format((float) (Gmono.get(i) * 100) / (countmonore.get(i)))+"</td><td>"+round.format((float) countmono.get(i) / totalcount)+"</td><td>"+round.format((float) countmonore.get(i) / seqcount)+"</td><td>"+round.format((float) countmonore.get(i) / bpcount)+"</td></tr>");
                                    }
                                    if (di.isSelected()) {
                                        Aperc += Adi.get(i);
                                        Tperc += Tdi.get(i);
                                        Gperc += Gdi.get(i);
                                        Cperc += Cdi.get(i);

                                        //lt.writeInt(countdi);lt.writeInt(countdire*2);lt.writeFloat((float)Adi*100/countdire*2);lt.writeFloat((float)Tdi*100/countdire*2);lt.writeFloat((float)Gdi*100/countdire*2);lt.writeFloat((float)Cdi*100/countdire*2);lt.writeFloat((float) countdi / totalcount);lt.writeFloat((float) countdire*2 / seqcount);lt.writeFloat((float) countdire*2 / bpcount);
                                        stats.printf("|di     |" + cell(Integer.toString(countdi.get(i)), 7) + "|" + cell(Integer.toString(countdire.get(i) * 2), 12) + "|%s|%s|%s|%s|" + cell((float) countdi.get(i) / totalcount, 15) + "|" + cell((float) countdire.get(i) * 2 / seqcount, 15) + "|" + cell((float) countdire.get(i) * 2 / bpcount, 15) + "|\n", cell((float) (Adi.get(i) * 100) / (countdire.get(i) * 2), 7), cell((float) (Tdi.get(i) * 100) / (countdire.get(i) * 2), 7), cell((float) (Cdi.get(i) * 100) / (countdire.get(i) * 2), 7), cell((float) (Gdi.get(i) * 100) / (countdire.get(i) * 2), 7));
                                        stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                        html.println("<tr><td><b>di</b></td><td>"+countdi.get(i)+"</td><td>"+(2 *countdire.get(i))+"</td><td>"+round.format((float) (Adi.get(i) * 100) / (2*countdire.get(i)))+"</td><td>"+round.format((float) (Tdi.get(i) * 100) / (2*countdire.get(i)))+"</td><td>"+round.format((float) (Cdi.get(i) * 100) / (2*countdire.get(i)))+"</td><td>"+round.format((float) (Gdi.get(i) * 100) / (2*countdire.get(i)))+"</td><td>"+round.format((float) countdi.get(i) / totalcount)+"</td><td>"+round.format((float) 2*countdire.get(i) / seqcount)+"</td><td>"+round.format((float) 2*countdire.get(i) / bpcount)+"</td></tr>");
                                    }
                                    if (tri.isSelected()) {
                                        Aperc += Atri.get(i);
                                        Tperc += Ttri.get(i);
                                        Gperc += Gtri.get(i);
                                        Cperc += Ctri.get(i);
                                        //lt.writeInt(counttri);lt.writeInt(counttrire*3);lt.writeFloat((float)Atri*100/counttrire*3);lt.writeFloat((float)Ttri*100/counttrire*3);lt.writeFloat((float)Gtri*100/counttrire*3);lt.writeFloat((float)Ctri*100/counttrire*3);lt.writeFloat((float) counttri / totalcount);lt.writeFloat((float) counttrire*3 / seqcount);lt.writeFloat((float) counttrire*3 / bpcount);
                                        stats.printf("|tri    |" + cell(Integer.toString(counttri.get(i)), 7) + "|" + cell(Integer.toString(counttrire.get(i) * 3), 12) + "|%s|%s|%s|%s|" + cell((float) counttri.get(i) / totalcount, 15) + "|" + cell((float) counttrire.get(i) * 3 / seqcount, 15) + "|" + cell((float) counttrire.get(i) * 3 / bpcount, 15) + "|\n", cell((float) (Atri.get(i) * 100) / (counttrire.get(i) * 3), 7), cell((float) (Ttri.get(i) * 100) / (counttrire.get(i) * 3), 7), cell((float) (Ctri.get(i) * 100) / (counttrire.get(i) * 3), 7), cell((float) (Gtri.get(i) * 100) / (counttrire.get(i) * 3), 7));
                                        stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                        html.println("<tr><td><b>tri</b></td><td>"+counttri.get(i)+"</td><td>"+(3 *counttrire.get(i))+"</td><td>"+round.format((float) (Atri.get(i) * 100) / (3*counttrire.get(i)))+"</td><td>"+round.format((float) (Ttri.get(i) * 100) / (3*counttrire.get(i)))+"</td><td>"+round.format((float) (Ctri.get(i) * 100) / (3*counttrire.get(i)))+"</td><td>"+round.format((float) (Gtri.get(i) * 100) / (3*counttrire.get(i)))+"</td><td>"+round.format((float) counttri.get(i) / totalcount)+"</td><td>"+round.format((float) 3*counttrire.get(i) / seqcount)+"</td><td>"+round.format((float) 3*counttrire.get(i) / bpcount)+"</td></tr>");
                                    }
                                    if (tetra.isSelected()) {
                                        Aperc += Atetra.get(i);
                                        Tperc += Ttetra.get(i);
                                        Gperc += Gtetra.get(i);
                                        Cperc += Ctetra.get(i);
                                        //lt.writeInt(counttetra);lt.writeInt(counttetrare*4);lt.writeFloat((float)Atetra*100/counttetrare*4);lt.writeFloat((float)Ttetra*100/counttetrare*4);lt.writeFloat((float)Gtetra*100/counttetrare*4);lt.writeFloat((float)Ctetra*100/counttetrare*4);lt.writeFloat((float) counttetra / totalcount);lt.writeFloat((float) counttetrare*4 / seqcount);lt.writeFloat((float) counttetrare*4 / bpcount);
                                        stats.printf("|tetra  |" + cell(Integer.toString(counttetra.get(i)), 7) + "|" + cell(Integer.toString(counttetrare.get(i) * 4), 12) + "|%s|%s|%s|%s|" + cell((float) counttetra.get(i) / totalcount, 15) + "|" + cell((float) counttetrare.get(i) * 4 / seqcount, 15) + "|" + cell((float) counttetrare.get(i) * 4 / bpcount, 15) + "|\n", cell((float) (Atetra.get(i) * 100) / (counttetrare.get(i) * 4), 7), cell((float) (Ttetra.get(i) * 100) / (counttetrare.get(i) * 4), 7), cell((float) (Ctetra.get(i) * 100) / (counttetrare.get(i) * 4), 7), cell((float) (Gtetra.get(i) * 100) / (counttetrare.get(i) * 4), 7));
                                        stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                        html.println("<tr><td><b>tetra</b></td><td>"+counttetra.get(i)+"</td><td>"+(4 *counttetrare.get(i))+"</td><td>"+round.format((float) (Atetra.get(i) * 100) / (4*counttetrare.get(i)))+"</td><td>"+round.format((float) (Ttetra.get(i) * 100) / (4*counttetrare.get(i)))+"</td><td>"+round.format((float) (Ctetra.get(i) * 100) / (4*counttetrare.get(i)))+"</td><td>"+round.format((float) (Gtetra.get(i) * 100) / (4*counttetrare.get(i)))+"</td><td>"+round.format((float) counttetra.get(i) / totalcount)+"</td><td>"+round.format((float) 4*counttetrare.get(i) / seqcount)+"</td><td>"+round.format((float) 4*counttetrare.get(i) / bpcount)+"</td></tr>");
                                    }
                                    if (penta.isSelected()) {
                                        Aperc += Apenta.get(i);
                                        Tperc += Tpenta.get(i);
                                        Gperc += Gpenta.get(i);
                                        Cperc += Cpenta.get(i);
                                        //lt.writeInt(countpenta);lt.writeInt(countpentare*5);lt.writeFloat((float)Apenta*100/countpentare*5);lt.writeFloat((float)Tpenta*100/countpentare*5);lt.writeFloat((float)Gpenta*100/countpentare*5);lt.writeFloat((float)Cpenta*100/countpentare*5);lt.writeFloat((float) countpenta / totalcount);lt.writeFloat((float) countpentare*5 / seqcount);lt.writeFloat((float) countpentare*5 / bpcount);
                                        stats.printf("|penta  |" + cell(Integer.toString(countpenta.get(i)), 7) + "|" + cell(Integer.toString(countpentare.get(i) * 5), 12) + "|%s|%s|%s|%s|" + cell((float) countpenta.get(i) / totalcount, 15) + "|" + cell((float) countpentare.get(i) * 5 / seqcount, 15) + "|" + cell((float) countpentare.get(i) * 5 / bpcount, 15) + "|\n", cell((float) (Apenta.get(i) * 100) / (countpentare.get(i) * 5), 7), cell((float) (Tpenta.get(i) * 100) / (countpentare.get(i) * 5), 7), cell((float) (Cpenta.get(i) * 100) / (countpentare.get(i) * 5), 7), cell((float) (Gpenta.get(i) * 100) / (countpentare.get(i) * 5), 7));
                                        stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                        html.println("<tr><td><b>penta</b></td><td>"+countpenta.get(i)+"</td><td>"+(5 *countpentare.get(i))+"</td><td>"+round.format((float) (Apenta.get(i) * 100) / (5*countpentare.get(i)))+"</td><td>"+round.format((float) (Tpenta.get(i) * 100) / (5*countpentare.get(i)))+"</td><td>"+round.format((float) (Cpenta.get(i) * 100) / (5*countpentare.get(i)))+"</td><td>"+round.format((float) (Gpenta.get(i) * 100) / (5*countpentare.get(i)))+"</td><td>"+round.format((float) countpenta.get(i) / totalcount)+"</td><td>"+round.format((float) 5*countpentare.get(i) / seqcount)+"</td><td>"+round.format((float) 5*countpentare.get(i) / bpcount)+"</td></tr>");
                                    }
                                    if (hexa.isSelected()) {
                                        Aperc += Ahexa.get(i);
                                        Tperc += Thexa.get(i);
                                        Gperc += Ghexa.get(i);
                                        Cperc += Chexa.get(i);
                                        //lt.writeInt(counthexa);lt.writeInt(counthexare*6);lt.writeFloat((float)Ahexa*100/counthexare*6);lt.writeFloat((float)Thexa*100/counthexare*6);lt.writeFloat((float)Ghexa*100/counthexare*6);lt.writeFloat((float)Chexa*100/counthexare*6);lt.writeFloat((float) counthexa / totalcount);lt.writeFloat((float) counthexare*6 / seqcount);lt.writeFloat((float) counthexare*6 / bpcount);
                                        stats.printf("|hexa   |" + cell(Integer.toString(counthexa.get(i)), 7) + "|" + cell(Integer.toString(counthexare.get(i) * 6), 12) + "|%s|%s|%s|%s|" + cell((float) counthexa.get(i) / totalcount, 15) + "|" + cell((float) counthexare.get(i) * 6 / seqcount, 15) + "|" + cell((float) counthexare.get(i) * 6 / bpcount, 15) + "|\n", cell((float) (Ahexa.get(i) * 100) / (counthexare.get(i) * 6), 7), cell((float) (Thexa.get(i) * 100) / (counthexare.get(i) * 6), 7), cell((float) (Chexa.get(i) * 100) / (counthexare.get(i) * 6), 7), cell((float) (Ghexa.get(i) * 100) / (counthexare.get(i) * 6), 7));
                                        stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                        html.println("<tr><td><b>hexa</b></td><td>"+counthexa.get(i)+"</td><td>"+(6 *counthexare.get(i))+"</td><td>"+round.format((float) (Ahexa.get(i) * 100) / (6*counthexare.get(i)))+"</td><td>"+round.format((float) (Thexa.get(i) * 100) / (6*counthexare.get(i)))+"</td><td>"+round.format((float) (Chexa.get(i) * 100) / (6*counthexare.get(i)))+"</td><td>"+round.format((float) (Ghexa.get(i) * 100) / (6*counthexare.get(i)))+"</td><td>"+round.format((float) counthexa.get(i) / totalcount)+"</td><td>"+round.format((float) 6*counthexare.get(i) / seqcount)+"</td><td>"+round.format((float) 6*counthexare.get(i) / bpcount)+"</td></tr>");
                                    }




                                    if (mono.isSelected()) {
                                        relfreq += (float) countmono.get(i) / totalcount;
                                        abfreq += (float) countmonore.get(i) / bpcount;
                                    }
                                    if (di.isSelected()) {
                                        relfreq += (float) countdi.get(i) / totalcount;
                                        abfreq += (float) countdire.get(i) * 2 / bpcount;
                                    }
                                    if (tri.isSelected()) {
                                        relfreq += (float) counttri.get(i) / totalcount;
                                        abfreq += (float) counttrire.get(i) * 3 / bpcount;
                                    }
                                    if (tetra.isSelected()) {
                                        relfreq += (float) counttetra.get(i) / totalcount;
                                        abfreq += (float) counttetrare.get(i) * 4 / bpcount;
                                    }
                                    if (penta.isSelected()) {
                                        relfreq += (float) countpenta.get(i) / totalcount;
                                        abfreq += (float) countpentare.get(i) * 5 / bpcount;
                                    }
                                    if (hexa.isSelected()) {
                                        relfreq += (float) counthexa.get(i) / totalcount;
                                        abfreq += (float) counthexare.get(i) * 6 / bpcount;
                                    }
                                    
                                    Globals.A=Aperc;
                                    Globals.T=Tperc;
                                    Globals.G=Gperc;
                                    Globals.C=Cperc;
                                    
                                    lt.writeLong(seqcount);lt.writeInt(totalcount);lt.writeLong(bpcount);lt.writeInt(Aperc);lt.writeInt(Tperc);lt.writeInt(Gperc);lt.writeInt(Cperc);
                                    stats.println("|TOTAL  |" + cell(Integer.toString(totalcount), 7) + "|" + cell(Long.toString(bpcount), 12) + "|" + cell((float)Aperc * 100 / bpcount, 7) + "|" + cell((float)Tperc * 100 / bpcount, 7) + "|" + cell((float)Cperc * 100 / bpcount, 7) + "|" + cell((float)Gperc * 100 / bpcount, 7) + "|" + cell(relfreq, 15) + "|" + cell((float) bpcount / seqcount, 15) + "|" + cell((float) abfreq, 15) + "|");
                                    stats.println("|_______|_______|____________|_______|_______|_______|_______|_______________|_______________|_______________|");
                                    stats.println("Genome length (bp): " + seqcount);
                                    stats.println("Relative Frequency: Count of each motif type / total SSR count");
                                    stats.println("Abundance: bp of each motif type / total sequence bp");
                                    stats.println("Relative Abundance: bp of each motif type / total microsatellites bp");
                                    stats.println();
                                    stats.println();
                                    stats.close();
                                    lt.close();
                                    
                                    html.println("<tr><td><b>TOTAL</b></td><td>"+totalcount+"</td><td>"+bpcount+"</td><td>"+round.format((float)Aperc * 100 / bpcount)+"</td><td>"+round.format((float)Tperc * 100 / bpcount)+"</td><td>"+round.format((float)Cperc * 100 / bpcount)+"</td><td>"+round.format((float)Gperc * 100 / bpcount)+"</td><td>"+round.format((float) relfreq)+"</td><td>"+round.format((float) bpcount / seqcount)+"</td><td>"+round.format((float) abfreq)+"</td></tr></table></html>");
                                    html.close();
                                    
                                    // it = map.keySet().iterator();
                                    
                                    for(String key : map.keySet()){
                                        map.get(key).refresh();
                                    }
                                    
                                    List<String> n1 = new ArrayList<String>();
                                    List<String> n2 = new ArrayList<String>();
                                    List<String> n3 = new ArrayList<String>();
                                    List<String> n4 = new ArrayList<String>();
                                    List<String> n5 = new ArrayList<String>();
                                    List<String> n6 = new ArrayList<String>();
                                    
                                    Iterator<String> it = map.keySet().iterator();
                                    
                                    while(it.hasNext()){
                                        String next = it.next();
                                        int len = next.length();
                                        if(len==1)
                                            n1.add(next);
                                        else if(len==2)
                                            n2.add(next);
                                        else if(len==3)
                                            n3.add(next);
                                        else if(len==4)
                                            n4.add(next);
                                        else if(len==5)
                                            n5.add(next);
                                        else if(len==6)
                                            n6.add(next);
                                        
                                    }
                                    
                                    Collections.sort(n1);
                                    Collections.sort(n2);
                                    Collections.sort(n3);
                                    Collections.sort(n4);
                                    Collections.sort(n5);
                                    Collections.sort(n6);
                                    
                                    boolean[] id1 = new boolean[n1.size()];
                                    boolean[] id2 = new boolean[n2.size()];
                                    boolean[] id3 = new boolean[n3.size()];
                                    boolean[] id4 = new boolean[n4.size()];
                                    boolean[] id5 = new boolean[n5.size()];
                                    boolean[] id6 = new boolean[n6.size()];
                                    
                                    
                                    motifhtml.println("<html><head><title>Motif Statistics</title></head><body>");
                                    int stand = checkStandardize();
                                    // stand=2; debug
                                    if(stand ==0){
                                        motifstats.println("**** Not Standardized ****");
                                        motifhtml.println("<h1>**** Not Standardized ****</h1>");
                                        Arrays.fill(id1, true);
                                        Arrays.fill(id2, true);
                                        Arrays.fill(id3, true);
                                        Arrays.fill(id4, true);
                                        Arrays.fill(id5, true);
                                        Arrays.fill(id6, true);
                                    }else{
                                        if(stand == 1){
                                            motifstats.println("**** Partially Standardized ****");
                                            motifhtml.println("<h1>**** Partially Standardized ****</h1>");
                                            matrix = new parser(1);
                                        }
                                        if(stand == 2){
                                            motifstats.println("**** Fully Standardized ****");
                                            motifhtml.println("<h1>**** Fully Standardized ****</h1>");
                                            matrix = new parser(2);
                                        }
                                        
                                        Arrays.fill(id1, true);
                                        Arrays.fill(id2, true);
                                        Arrays.fill(id3, true);
                                        Arrays.fill(id4, true);
                                        Arrays.fill(id5, true);
                                        Arrays.fill(id6, true);
                                        
                                        
                                        for(int n=0;n<n1.size();n++){
                                            int id = -2;
                                            id = matrix.SearchMap(1, n1.get(n));
                                            if(id>=0){
                                                int found = matrix.checkFound(1, id);
                                                if(found >=0 ){
                                                    map.get(n1.get(found)).merge(map.get(n1.get(n)));
                                                    id1[n] = false; // gia clean twn listwn apo merged TODO *
                                                }
                                                if(found == -4){
                                                    matrix.makeFound(1, id, n);
                                                }
                                            } 
                                        }
                                        
                                        for(int n=0;n<n2.size();n++){
                                            int id = -2;
                                            id = matrix.SearchMap(2, n2.get(n));
                                            if(id>=0){
                                                int found = matrix.checkFound(2, id);
                                                if(found >=0 ){
                                                    map.get(n2.get(found)).merge(map.get(n2.get(n)));
                                                    id2[n] = false; // gia clean twn listwn apo merged TODO *
                                                }
                                                else{
                                                    matrix.makeFound(2, id, n);
                                                    id2[n] = true;
                                                }
                                            } 
                                        }
                                        
                                        for(int n=0;n<n3.size();n++){
                                            int id = -2;
                                            id = matrix.SearchMap(3, n3.get(n));
                                            if(id>=0){
                                                int found = matrix.checkFound(3, id);
                                                if(found >=0 ){
                                                    map.get(n3.get(found)).merge(map.get(n3.get(n)));
                                                    id3[n] = false; // gia clean twn listwn apo merged TODO *
                                                }
                                                else{
                                                    matrix.makeFound(3, id, n);
                                                    id3[n] = true;
                                                }
                                            } 
                                        }
                                        
                                        for(int n=0;n<n4.size();n++){
                                            int id = -2;
                                            id = matrix.SearchMap(4, n4.get(n));
                                            if(id>=0){
                                                int found = matrix.checkFound(4, id);
                                                if(found >=0 ){
                                                    map.get(n4.get(found)).merge(map.get(n4.get(n)));
                                                    id4[n] = false; // gia clean twn listwn apo merged TODO *
                                                }
                                                else{
                                                    matrix.makeFound(4, id, n);
                                                    id4[n] = true;
                                                }
                                            } 
                                        }
                                        
                                        for(int n=0;n<n5.size();n++){
                                            int id = -2;
                                            id = matrix.SearchMap(5, n5.get(n));
                                            if(id>=0){
                                                int found = matrix.checkFound(5, id);
                                                if(found >=0 ){
                                                    map.get(n5.get(found)).merge(map.get(n5.get(n)));
                                                    id5[n] = false; // gia clean twn listwn apo merged TODO *
                                                }
                                                else{
                                                    matrix.makeFound(5, id, n);
                                                    id5[n] = true;
                                                }
                                            } 
                                        }
                                        
                                        for(int n=0;n<n6.size();n++){
                                            int id = -2;
                                            id = matrix.SearchMap(6, n6.get(n));
                                            if(id>=0){
                                                int found = matrix.checkFound(6, id);
                                                if(found >=0 ){
                                                    map.get(n6.get(found)).merge(map.get(n6.get(n)));
                                                    id6[n] = false; // gia clean twn listwn apo merged TODO *
                                                }
                                                else{
                                                    matrix.makeFound(6, id, n);
                                                    id6[n] = true;
                                                }
                                            } 
                                        }
                                    }
                                    
                                    for(String key : map.keySet()){
                                        map.get(key).refresh();
                                    }
                                    motifstats.println(" Motif   Count  Repeats          bp  Avg_Length  SD_Length  Max_Length  Avg_Repeats    A%      T%      C%      G%    ");                                    
                                    motifhtml.println("<table border=\"1\"><tr><td><b>Motif</b></td><td><b>Count</b></td><td><b>Repeats</b></td><td><b>bp</b></td><td><b>Avg_Length</b></td><td><b>SD_Length</b></td><td><b>Max_Length</b></td><td><b>Avg_Repeats</b></td><td><b>A%</b></td><td><b>T%</b></td><td><b>C%</b></td><td><b>G%</b></td></tr>");                                    
                                    if(mono.isSelected()){
                                        for(int z=0;z<n1.size();z++){
                                            if(id1[z] && !map.get(n1.get(z)).getMotif().contains("N")){
                                                motifstats.println(map.get(n1.get(z)).toString());
                                                motifhtml.println(map.get(n1.get(z)).toHTML());
                                            }
                                                
                                        }
                                    }
                                    if(di.isSelected()){
                                        for(int z=0;z<n2.size();z++){
                                            if(id2[z] && !map.get(n2.get(z)).getMotif().contains("N")){
                                                motifstats.println(map.get(n2.get(z)).toString());
                                                motifhtml.println(map.get(n2.get(z)).toHTML());
                                            }
                                        }
                                    }
                                    if(tri.isSelected()){
                                        for(int z=0;z<n3.size();z++){
                                            if(id3[z] && !map.get(n3.get(z)).getMotif().contains("N")){
                                                motifstats.println(map.get(n3.get(z)).toString());
                                                motifhtml.println(map.get(n3.get(z)).toHTML());
                                            }
                                        }
                                    }
                                    if(tetra.isSelected()){
                                        for(int z=0;z<n4.size();z++){
                                            if(id4[z] && !map.get(n4.get(z)).getMotif().contains("N")){
                                                motifstats.println(map.get(n4.get(z)).toString());
                                                motifhtml.println(map.get(n4.get(z)).toHTML());
                                            }
                                        }
                                    }
                                    if(penta.isSelected()){
                                        for(int z=0;z<n5.size();z++){
                                            if(id5[z] && !map.get(n5.get(z)).getMotif().contains("N")){
                                                motifstats.println(map.get(n5.get(z)).toString());
                                                motifhtml.println(map.get(n5.get(z)).toHTML());
                                            }
                                        }
                                    }
                                    if(hexa.isSelected()){
                                        for(int z=0;z<n6.size();z++){
                                            if(id6[z] && !map.get(n6.get(z)).getMotif().contains("N")){
                                                motifstats.println(map.get(n6.get(z)).toString());
                                                motifhtml.println(map.get(n6.get(z)).toHTML());
                                            }
                                        }
                                    }
                                    motifstats.close();
                                    motifhtml.println("</table></body></html>");
                                    motifhtml.close();
                                    
                                    Runtime.getRuntime().exec("notepad " + location2);

                                }


                            } catch (FileNotFoundException ex) {
                                //msg.showMessageDialog(paneldown, "Update your selected species", "Error", JOptionPane.ERROR_MESSAGE);
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            msg.showMessageDialog(paneldown, "Minimum length requires an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        msg.showMessageDialog(paneldown, "Please fill in the minimum length (Integers only)", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (box2.isSelected()){
                    boolean gapisnumber = false;
                    boolean minisnumber = false;
                    int gap = 0;
                    int min = 0;
                    try {
                        gap = Integer.parseInt(max.getText());
                        gapisnumber = true;
                    } catch (NumberFormatException ex) {
                        gapisnumber = false;
                    }

                    try {
                        min = Integer.parseInt(minpregap.getText());
                        minisnumber = true;
                    } catch (NumberFormatException ex) {
                        minisnumber = false;
                    }
                    if (gapisnumber && minisnumber) {
                        try {
                            getImPerfectSSRs(organisms, min, flag, gap);

                            for (int i = 0; i < organisms.length; i++) {


                                PrintWriter stats=null;
                                PrintWriter html=null;

                                String location = ""; // 18/11/2013 added starting here
                                    String filetype = "";
                                    String filepro = "";
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                    //List<String> files = getFiles(organisms[i], minlen, flag);
                                    
                                    
                                    // 18/11/2013 added ending here
                                PrintWriter out = null;
                                DataOutputStream lt=null;
                                if (filetype.contains("organisms")) {
                                        File f = new File("organisms/" + organisms[i] + "/stats/");
                                    if (!f.exists()) {
                                        f.mkdir();
                                    }

                                    stats = new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                                    lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".imperf")));
                                    html =  new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                          
                                    File fi = new File("organisms/" + organisms[i] + "/results/");
                                    if (!fi.exists()) {
                                        fi.mkdir();
                                    }
                                    String toopen = "organisms/" + organisms[i] + "/results/allImPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                                    location = toopen;
                                    out = new PrintWriter(toopen);

                                    out.println("Results for organism: " + organisms[i] + "\t Search Parameters --> Maximum Mismatch length for ImPerfect SSRs : " + gap + " minimum SSR length before given gap: " + min);
                                } else if (filetype.contains("local")) {
                                    
                                    File f = new File("local/" + organisms[i] + "/stats/");
                                    if (!f.exists()) {
                                        f.mkdir();
                                    }

                                    stats = new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                                    lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".imperf")));
                                    html =  new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                    
                                    
                                    
                                    File fi = new File("local/" + organisms[i] + "/results/");
                                    if (!fi.exists()) {
                                        fi.mkdir();
                                    }
                                    String toopen = "local/" + organisms[i] + "/results/allImPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                                    location = toopen;
                                    out = new PrintWriter(toopen);
                                    out.println("Results for project: " + organisms[i] + "\t Search Parameters --> Maximum Mismatch length for ImPerfect SSRs : " + gap + " minimum SSR length before given gap: " + min);
                                }

                                if (mono.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                    
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + min + "_" + gap + "_monoImPerfect.temp")));
                                        boolean eof = false;
                                        while (!eof) {
                                            try {
                                                String ssr = in.readUTF();
                                                int start = in.readInt();
                                                int end = in.readInt();
                                                if(!ssr.contains("N"))
                                                    out.println("SSR: " + ssr + " start-end " + start + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                            } catch (EOFException exc) {
                                                eof = true;
                                            }
                                        }
                                        in.close();
                                    }
                                }
                                if (di.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + min + "_" + gap + "_diImPerfect.temp")));
                                        boolean eof = false;
                                        while (!eof) {
                                            try {
                                                String ssr = in.readUTF();
                                                int start = in.readInt();
                                                int end = in.readInt();
                                                if(!ssr.contains("N"))
                                                    out.println("SSR: " + ssr + " start-end " + start + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                            } catch (EOFException exc) {
                                                eof = true;
                                            }
                                        }
                                        in.close();
                                    }
                                }
                                if (tri.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + min + "_" + gap + "_triImPerfect.temp")));
                                        boolean eof = false;
                                        while (!eof) {
                                            try {
                                                String ssr = in.readUTF();
                                                int start = in.readInt();
                                                int end = in.readInt();
                                                if(!ssr.contains("N"))
                                                    out.println("SSR: " + ssr + " start-end " + start + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                            } catch (EOFException exc) {
                                                eof = true;
                                            }
                                        }
                                        in.close();
                                    }
                                }
                                if (tetra.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + min + "_" + gap + "_tetraImPerfect.temp")));
                                        boolean eof = false;
                                        while (!eof) {
                                            try {
                                                String ssr = in.readUTF();
                                                int start = in.readInt();
                                                int end = in.readInt();
                                                if(!ssr.contains("N"))
                                                    out.println("SSR: " + ssr + " start-end " + start + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                            } catch (EOFException exc) {
                                                eof = true;
                                            }
                                        }
                                        in.close();
                                    }
                                }
                                if (penta.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + min + "_" + gap + "_pentaImPerfect.temp")));
                                        boolean eof = false;
                                        while (!eof) {
                                            try {
                                                String ssr = in.readUTF();
                                                int start = in.readInt();
                                                int end = in.readInt();
                                                if(!ssr.contains("N"))
                                                    out.println("SSR: " + ssr + " start-end " + start + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                            } catch (EOFException exc) {
                                                eof = true;
                                            }
                                        }
                                        in.close();
                                    }
                                }
                                if (hexa.isSelected()) {    
                                    // 18/11/2013 added starting here
                                    
                                    if(flag){
                                        filetype = "organisms";
                                        filepro = "organisms/" + organisms[i] + "/data/";
                                        int ret = getOrganismStatus(organisms[i]);
                                        if(ret == -1)
                                            indexer = new Indexer(chromosomelist);
                                        else
                                            indexer = new Indexer(ret);
                                            
                                    }else{
                                        filetype = "local";
                                        filepro = "local/" + organisms[i] + "/data/";
                                        String indexfile = "local/" + organisms[i] + "/index.txt";
                                        indexer = new Indexer(indexfile);
                                    }
                                        while(indexer.hasNext()){
                                            String files = filepro + indexer.getNextFileName();
                                        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files + "_" + min + "_" + gap + "_hexaImPerfect.temp")));
                                        boolean eof = false;
                                        while (!eof) {
                                            try {
                                                String ssr = in.readUTF();
                                                int start = in.readInt();
                                                int end = in.readInt();
                                                if(!ssr.contains("N"))
                                                    out.println("SSR: " + ssr + "  start-end " + start + "-" + end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                            } catch (EOFException exc) {
                                                eof = true;
                                            }
                                        }
                                        in.close();
                                    }
                                }



                                out.close();

                                Runtime.getRuntime().exec("notepad " + location);
                                
                                DecimalFormat round = new DecimalFormat("#.###");

                                html.println("<html><h1>******* ImPerfect SSRs *******</h1>");
                                html.println("<h4>Results for project: " + organisms[i] + "</h4><h4>Search Parameters --> Maximum Mismatch length for ImPerfect SSRs (bp): " + gap + "</h4><h4>minimum SSR length before given Mismatch length (bp): " + min+"</h4>"); 
                                html.println("<table border=\"1\"><tr><td><b>motif</b></td><td><b>count</b></td><td><b>bp</b></td><td><b>A%</b></td><td><b>T%</b></td><td><b>C%</b></td><td><b>G%</b></td><td><b>Relative Frequency</b></td><td><b>Abundance</b></td><td><b>Relative Abundance</b></td></tr>");

                                
                                stats.println("******* ImPerfect SSRs *******");
                                stats.println("Results for project: " + organisms[i] + "\n Search Parameters --> Maximum Mismatch length for ImPerfect SSRs (bp): " + gap + " \nminimum SSR length before given Mismatch length (bp): " + min);

                                stats.println(" ____________________________________________________________________________________________________________ ");
                                stats.println("|       |       |            |       |       |       |       |   Relative    |               |   Relative    |");
                                stats.println("| motif | count |     bp     |   A%  |   T%  |   C%  |   G%  |   Frequency   |   Abundance   |   Abundance   |");
                                stats.println("|=======|=======|============|=======|=======|=======|=======|===============|===============|===============|");
                                int totalcount = 0;
                                long bpcount = 0;
                                int Aperc = 0;
                                int Tperc = 0;
                                int Gperc = 0;
                                int Cperc = 0;
                                float relfreq = 0;
                                float abfreq = 0;
                                long seqcount = 0;
                                
                                if (mono.isSelected()) {
                                    totalcount += countmono.get(i);
                                    bpcount += countmonore.get(i);
                                }
                                if (di.isSelected()) {
                                    totalcount += countdi.get(i);
                                    bpcount += countdire.get(i);
                                }
                                if (tri.isSelected()) {
                                    totalcount += counttri.get(i);
                                    bpcount += counttrire.get(i);
                                }
                                if (tetra.isSelected()) {
                                    totalcount += counttetra.get(i);
                                    bpcount += counttetrare.get(i);
                                }
                                if (penta.isSelected()) {
                                    totalcount += countpenta.get(i);
                                    bpcount += countpentare.get(i);
                                }
                                if (hexa.isSelected()) {
                                    totalcount += counthexa.get(i);
                                    bpcount += counthexare.get(i);
                                }
                                try {
                                    Class.forName("com.mysql.jdbc.Driver");
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Connection con = null;
                                try {
                                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
                                } catch (SQLException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Statement st = null;
                                try {
                                    st = con.createStatement();
                                } catch (SQLException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                st.executeUpdate("use lobid");
                                seqcount=0;
                                    
                                if (filetype.contains("organisms")) {
                                    ResultSet rs = st.executeQuery("SELECT end FROM slices INNER JOIN organism WHERE slices.org_id=organism.org_id AND organism.name='" + organisms[i] + "'");
                                    while (rs.next()) {
                                        seqcount += Long.parseLong(rs.getString(1));
                                    }
                                } else if (filetype.contains("local")) {
                                    BufferedReader in = new BufferedReader(new FileReader("local/" + organisms[i] + "/index.txt"));
                                    int count = countlines("local/" + organisms[i] + "/index.txt");
                                    for (int c = 0; c < count; c++) {
                                        String temp = in.readLine();
                                        BufferedReader tmp = new BufferedReader(new FileReader("local/" + organisms[i] + "/" + temp + ".txt"));

                                        boolean eof=false;
                                        while (!eof) {
                                            
                                            String s = tmp.readLine();
                                            if(s!=null){
                                                seqcount+=s.length();
                                            }
                                            else{
                                                eof = true;
                                            }
                                        }
                                        tmp.close();
                                    }
                                }
                                int tempmono=countmonore.get(i);
                                int tempdi=countdire.get(i);
                                int temptri=counttrire.get(i);
                                int temptetra=counttetrare.get(i);
                                int temppenta=countpentare.get(i);
                                int temphexa=counthexare.get(i);
                                if(tempmono==0)tempmono=1;
                                if(tempdi==0)tempdi=1;
                                if(temptri==0)temptri=1;
                                if(temptetra==0)temptetra=1;
                                if(temppenta==0)temppenta=1;
                                if(temphexa==0)temphexa=1;

                                if (mono.isSelected()) {
                                    Aperc += Amono.get(i);
                                    Tperc += Tmono.get(i);
                                    Gperc += Gmono.get(i);
                                    Cperc += Cmono.get(i);
                                    

                                    //lt.writeInt(countmono.get(i));lt.writeInt(countmonore.get(i));lt.writeFloat((float)Amono.get(i)*100/tempmono);lt.writeFloat((float)Tmono.get(i)*100/tempmono);lt.writeFloat((float)Gmono.get(i)*100/tempmono);lt.writeFloat((float)Cmono.get(i)*100/tempmono);lt.writeFloat((float) countmono.get(i) / totalcount);lt.writeFloat((float) countmonore.get(i) / seqcount);lt.writeFloat((float) countmonore.get(i) / bpcount);
                                    stats.printf("|mono   |" + cell(Integer.toString(countmono.get(i)), 7) + "|" + cell(Integer.toString(countmonore.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) countmono.get(i) / totalcount, 15) + "|" + cell((float) countmonore.get(i) / seqcount, 15) + "|" + cell((float) countmonore.get(i) / bpcount, 15) + "|\n", cell((float) (Amono.get(i) * 100) / (tempmono), 7), cell((float) (Tmono.get(i) * 100) / (tempmono), 7), cell((float) (Cmono.get(i) * 100) / (tempmono), 7), cell((float) (Gmono.get(i) * 100) / (tempmono), 7));
                                    stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    html.println("<tr><td><b>mono</b></td><td>"+countmono.get(i)+"</td><td>"+(countmonore.get(i))+"</td><td>"+round.format((float) (Amono.get(i) * 100) / (tempmono))+"</td><td>"+round.format((float) (Tmono.get(i) * 100) / (tempmono))+"</td><td>"+round.format((float) (Cmono.get(i) * 100) / (tempmono))+"</td><td>"+round.format((float) (Gmono.get(i) * 100) / (tempmono))+"</td><td>"+round.format((float) countmono.get(i) / totalcount)+"</td><td>"+round.format((float) countmonore.get(i) / seqcount)+"</td><td>"+round.format((float) countmonore.get(i) / bpcount)+"</td></tr>");
                                }
                                if (di.isSelected()) {
                                    Aperc += Adi.get(i);
                                    Tperc += Tdi.get(i);
                                    Gperc += Gdi.get(i);
                                    Cperc += Cdi.get(i);

                                    //lt.writeInt(countdi.get(i));lt.writeInt(countdi.get(i)re.get(i));lt.writeFloat((float)Adi.get(i)*100/tempdi);lt.writeFloat((float)Tdi.get(i)*100/tempdi);lt.writeFloat((float)Gdi.get(i)*100/tempdi);lt.writeFloat((float)Cdi.get(i)*100/tempdi);lt.writeFloat((float) countdi.get(i) / totalcount);lt.writeFloat((float) countdi.get(i)re.get(i) / seqcount);lt.writeFloat((float) countdi.get(i)re.get(i) / bpcount);
                                    stats.printf("|di     |" + cell(Integer.toString(countdi.get(i)), 7) + "|" + cell(Integer.toString(countdire.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) countdi.get(i) / totalcount, 15) + "|" + cell((float) countdi.get(i) / seqcount, 15) + "|" + cell((float) countdi.get(i) / bpcount, 15) + "|\n", cell((float) (Adi.get(i) * 100) / (tempdi), 7), cell((float) (Tdi.get(i) * 100) / (tempdi), 7), cell((float) (Cdi.get(i) * 100) / (tempdi), 7), cell((float) (Gdi.get(i) * 100) / (tempdi), 7));
                                    stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    html.println("<tr><td><b>di</b></td><td>"+countdi.get(i)+"</td><td>"+(countdire.get(i))+"</td><td>"+round.format((float) (Adi.get(i) * 100) / (tempdi))+"</td><td>"+round.format((float) (Tdi.get(i) * 100) / (tempdi))+"</td><td>"+round.format((float) (Cdi.get(i) * 100) / (tempdi))+"</td><td>"+round.format((float) (Gdi.get(i) * 100) / (tempdi))+"</td><td>"+round.format((float) countdi.get(i) / totalcount)+"</td><td>"+round.format((float) countdire.get(i) / seqcount)+"</td><td>"+round.format((float) countdire.get(i) / bpcount)+"</td></tr>");
                                }
                                if (tri.isSelected()) {
                                    Aperc += Atri.get(i);
                                    Tperc += Ttri.get(i);
                                    Gperc += Gtri.get(i);
                                    Cperc += Ctri.get(i);
                                    //lt.writeInt(counttri.get(i));lt.writeInt(counttrire.get(i).get(i));lt.writeFloat((float)Atri.get(i)*100/temptri);lt.writeFloat((float)Ttri.get(i)*100/temptri);lt.writeFloat((float)Gtri.get(i)*100/temptri);lt.writeFloat((float)Ctri.get(i)*100/temptri);lt.writeFloat((float) counttri.get(i) / totalcount);lt.writeFloat((float) counttrire.get(i).get(i) / seqcount);lt.writeFloat((float) counttrire.get(i).get(i) / bpcount);
                                    stats.printf("|tri    |" + cell(Integer.toString(counttri.get(i)), 7) + "|" + cell(Integer.toString(counttrire.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) counttri.get(i) / totalcount, 15) + "|" + cell((float) counttrire.get(i) / seqcount, 15) + "|" + cell((float) counttrire.get(i) / bpcount, 15) + "|\n", cell((float) (Atri.get(i) * 100) / (temptri), 7), cell((float) (Ttri.get(i) * 100) / (temptri), 7), cell((float) (Ctri.get(i) * 100) / (temptri), 7), cell((float) (Gtri.get(i) * 100) / (temptri), 7));
                                    stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    html.println("<tr><td><b>tri</b></td><td>"+counttri.get(i)+"</td><td>"+(counttrire.get(i))+"</td><td>"+round.format((float) (Atri.get(i) * 100) / (temptri))+"</td><td>"+round.format((float) (Ttri.get(i) * 100) / (temptri))+"</td><td>"+round.format((float) (Ctri.get(i) * 100) / (temptri))+"</td><td>"+round.format((float) (Gtri.get(i) * 100) / (temptri))+"</td><td>"+round.format((float) counttri.get(i) / totalcount)+"</td><td>"+round.format((float) counttrire.get(i) / seqcount)+"</td><td>"+round.format((float) counttrire.get(i) / bpcount)+"</td></tr>");
                                }
                                if (tetra.isSelected()) {
                                    Aperc += Atetra.get(i);
                                    Tperc += Ttetra.get(i);
                                    Gperc += Gtetra.get(i);
                                    Cperc += Ctetra.get(i);
                                    //lt.writeInt(counttetra.get(i));lt.writeInt(counttetrare.get(i));lt.writeFloat((float)Atetra.get(i)*100/temptetra);lt.writeFloat((float)Ttetra.get(i)*100/temptetra);lt.writeFloat((float)Gtetra.get(i)*100/temptetra);lt.writeFloat((float)Ctetra.get(i)*100/temptetra);lt.writeFloat((float) counttetra.get(i) / totalcount);lt.writeFloat((float) counttetrare.get(i) / seqcount);lt.writeFloat((float) counttetrare.get(i) / bpcount);
                                    stats.printf("|tetra  |" + cell(Integer.toString(counttetra.get(i)), 7) + "|" + cell(Integer.toString(counttetrare.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) counttetra.get(i) / totalcount, 15) + "|" + cell((float) counttetrare.get(i) / seqcount, 15) + "|" + cell((float) counttetrare.get(i) / bpcount, 15) + "|\n", cell((float) (Atetra.get(i) * 100) / (temptetra), 7), cell((float) (Ttetra.get(i) * 100) / (temptetra), 7), cell((float) (Ctetra.get(i) * 100) / (temptetra), 7), cell((float) (Gtetra.get(i) * 100) / (temptetra), 7));
                                    stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    html.println("<tr><td><b>tetra</b></td><td>"+counttetra.get(i)+"</td><td>"+(counttetrare.get(i))+"</td><td>"+round.format((float) (Atetra.get(i) * 100) / (temptetra))+"</td><td>"+round.format((float) (Ttetra.get(i) * 100) / (temptetra))+"</td><td>"+round.format((float) (Ctetra.get(i) * 100) / (temptetra))+"</td><td>"+round.format((float) (Gtetra.get(i) * 100) / (temptetra))+"</td><td>"+round.format((float) counttetra.get(i) / totalcount)+"</td><td>"+round.format((float) counttetrare.get(i) / seqcount)+"</td><td>"+round.format((float) counttetrare.get(i) / bpcount)+"</td></tr>");
                                }
                                if (penta.isSelected()) {
                                    Aperc += Apenta.get(i);
                                    Tperc += Tpenta.get(i);
                                    Gperc += Gpenta.get(i);
                                    Cperc += Cpenta.get(i);
                                    //lt.writeInt(countpenta.get(i));lt.writeInt(countpentare.get(i));lt.writeFloat((float)Apenta.get(i)*100/temppenta);lt.writeFloat((float)Tpenta.get(i)*100/temppenta);lt.writeFloat((float)Gpenta.get(i)*100/temppenta);lt.writeFloat((float)Cpenta.get(i)*100/temppenta);lt.writeFloat((float) countpenta.get(i) / totalcount);lt.writeFloat((float) countpentare.get(i) / seqcount);lt.writeFloat((float) countpentare.get(i) / bpcount);
                                    stats.printf("|penta  |" + cell(Integer.toString(countpenta.get(i)), 7) + "|" + cell(Integer.toString(countpentare.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) countpenta.get(i) / totalcount, 15) + "|" + cell((float) countpentare.get(i) / seqcount, 15) + "|" + cell((float) countpentare.get(i) / bpcount, 15) + "|\n", cell((float) (Apenta.get(i) * 100) / (temppenta), 7), cell((float) (Tpenta.get(i) * 100) / (temppenta), 7), cell((float) (Cpenta.get(i) * 100) / (temppenta), 7), cell((float) (Gpenta.get(i) * 100) / (temppenta), 7));
                                    stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    html.println("<tr><td><b>penta</b></td><td>"+countpenta.get(i)+"</td><td>"+(countpentare.get(i))+"</td><td>"+round.format((float) (Apenta.get(i) * 100) / (temppenta))+"</td><td>"+round.format((float) (Tpenta.get(i) * 100) / (temppenta))+"</td><td>"+round.format((float) (Cpenta.get(i) * 100) / (temppenta))+"</td><td>"+round.format((float) (Gpenta.get(i) * 100) / (temppenta))+"</td><td>"+round.format((float) countpenta.get(i) / totalcount)+"</td><td>"+round.format((float) countpentare.get(i) / seqcount)+"</td><td>"+round.format((float) countpentare.get(i) / bpcount)+"</td></tr>");
                                }
                                if (hexa.isSelected()) {
                                    Aperc += Ahexa.get(i);
                                    Tperc += Thexa.get(i);
                                    Gperc += Ghexa.get(i);
                                    Cperc += Chexa.get(i);
                                    //lt.writeInt(counthexa.get(i));lt.writeInt(counthexare.get(i));lt.writeFloat((float)Ahexa.get(i)*100/temphexa);lt.writeFloat((float)Thexa.get(i)*100/temphexa);lt.writeFloat((float)Ghexa.get(i)*100/temphexa);lt.writeFloat((float)Chexa.get(i)*100/temphexa);lt.writeFloat((float) counthexa.get(i) / totalcount);lt.writeFloat((float) counthexare.get(i) / seqcount);lt.writeFloat((float) counthexare.get(i) / bpcount);
                                    stats.printf("|hexa   |" + cell(Integer.toString(counthexa.get(i)), 7) + "|" + cell(Integer.toString(counthexare.get(i)), 12) + "|%s|%s|%s|%s|" + cell((float) counthexa.get(i) / totalcount, 15) + "|" + cell((float) counthexare.get(i) / seqcount, 15) + "|" + cell((float) counthexare.get(i) / bpcount, 15) + "|\n", cell((float) (Ahexa.get(i) * 100) / (temphexa), 7), cell((float) (Thexa.get(i) * 100) / (temphexa), 7), cell((float) (Chexa.get(i) * 100) / (temphexa), 7), cell((float) (Ghexa.get(i) * 100) / (temphexa), 7));
                                    stats.println("|-------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    html.println("<tr><td><b>hexa</b></td><td>"+counthexa.get(i)+"</td><td>"+(counthexare.get(i))+"</td><td>"+round.format((float) (Ahexa.get(i) * 100) / (temphexa))+"</td><td>"+round.format((float) (Thexa.get(i) * 100) / (temphexa))+"</td><td>"+round.format((float) (Chexa.get(i) * 100) / (temphexa))+"</td><td>"+round.format((float) (Ghexa.get(i) * 100) / (temphexa))+"</td><td>"+round.format((float) counthexa.get(i) / totalcount)+"</td><td>"+round.format((float) counthexare.get(i) / seqcount)+"</td><td>"+round.format((float) counthexare.get(i) / bpcount)+"</td></tr>");
                                }




                                if (mono.isSelected()) {
                                    relfreq += (float) countmono.get(i) / totalcount;
                                    abfreq += (float) countmonore.get(i) / bpcount;
                                }
                                if (di.isSelected()) {
                                    relfreq += (float) countdi.get(i) / totalcount;
                                    abfreq += (float) countdire.get(i) / bpcount;
                                }
                                if (tri.isSelected()) {
                                    relfreq += (float) counttri.get(i) / totalcount;
                                    abfreq += (float) counttrire.get(i) / bpcount;
                                }
                                if (tetra.isSelected()) {
                                    relfreq += (float) counttetra.get(i) / totalcount;
                                    abfreq += (float) counttetrare.get(i) / bpcount;
                                }
                                if (penta.isSelected()) {
                                    relfreq += (float) countpenta.get(i) / totalcount;
                                    abfreq += (float) countpentare.get(i) / bpcount;
                                }
                                if (hexa.isSelected()) {
                                    relfreq += (float) counthexa.get(i) / totalcount;
                                    abfreq += (float) counthexare.get(i) / bpcount;
                                }


                                lt.writeLong(seqcount);lt.writeInt(totalcount);lt.writeLong(bpcount);lt.writeInt(Aperc);lt.writeInt(Tperc);lt.writeInt(Gperc);lt.writeInt(Cperc);
                                stats.println("|TOTAL  |" + cell(Integer.toString(totalcount), 7) + "|" + cell(Long.toString(bpcount), 12) + "|" + cell((float)Aperc * 100 / bpcount, 7) + "|" + cell((float)Tperc * 100 / bpcount, 7) + "|" + cell((float)Cperc * 100 / bpcount, 7) + "|" + cell((float)Gperc * 100 / bpcount, 7) + "|" + cell(relfreq, 15) + "|" + cell((float) bpcount / seqcount, 15) + "|" + cell((float) abfreq, 15) + "|");
                                stats.println("|_______|_______|____________|_______|_______|_______|_______|_______________|_______________|_______________|");
                                stats.println("Genome length (bp): " + seqcount);
                                stats.println("Relative Frequency: Count of each motif type / total SSR count");
                                stats.println("Abundance: bp of each motif type / total sequence bp");
                                stats.println("Relative Abundance: bp of each motif type / total microsatellites bp");
                                stats.println();
                                stats.println();
                                stats.close();
                                lt.close();
                                html.println("<tr><td><b>TOTAL</b></td><td>"+totalcount+"</td><td>"+bpcount+"</td><td>"+round.format((float)Aperc * 100 / bpcount)+"</td><td>"+round.format((float)Tperc * 100 / bpcount)+"</td><td>"+round.format((float)Cperc * 100 / bpcount)+"</td><td>"+round.format((float)Gperc * 100 / bpcount)+"</td><td>"+round.format((float) relfreq)+"</td><td>"+round.format((float) bpcount / seqcount)+"</td><td>"+round.format((float) abfreq)+"</td></tr></table></html>");
                                html.close();
                                    

                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            //msg.showMessageDialog(paneldown, "Update your selected species", "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        if (!gapisnumber && !minisnumber) {
                            msg.showMessageDialog(paneldown, "Fill in Mismatch length for Imperfect SSRs \n and the minimum sequence length before the Mismatch length\n (Integers only)", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (!gapisnumber) {
                                msg.showMessageDialog(paneldown, "Fill in Mismatch length for Imperfect SSRs (Integer only)", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            if (!minisnumber) {
                                msg.showMessageDialog(paneldown, "Fill in the minimum sequence length before the Mismatch length (Integer only)", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                }

                if (box3.isSelected()) {
                    boolean lenisnumber;
                    boolean gapisnumber;
                    int minlen = 0;
                    int gap = 0;
                    if (!com.isSelected() && !incom.isSelected()) {
                        msg.showMessageDialog(paneldown, "Select a Compound SSR Option", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        

                        try {
                            minlen = Integer.parseInt(score.getText());
                            lenisnumber = true;
                        } catch (NumberFormatException ex) {
                            lenisnumber = false;
                        }


                        try {
                            gap = Integer.parseInt(maxgapcomp.getText());
                            gapisnumber = true;
                        } catch (NumberFormatException ex) {
                            gapisnumber = false;
                        }


                        if (lenisnumber && gapisnumber) {

                            if (com.isSelected()) {
                                try {
                                    getCompoundPerfectSSRs(organisms, minlen, flag, gap);
                                } catch (SQLException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    //msg.showMessageDialog(paneldown, "Update your selected species", "Error", JOptionPane.ERROR_MESSAGE);
                                } catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (incom.isSelected()) {
                                try {
                                    getImPerfectCompoundSSRs(organisms, minlen, flag, gap);
                                } catch (SQLException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    //msg.showMessageDialog(paneldown, "Update your selected species", "Error", JOptionPane.ERROR_MESSAGE);
                                } catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            if (!lenisnumber) {
                                msg.showMessageDialog(paneldown, "Minimum length requires an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            if (!gapisnumber) {
                                msg.showMessageDialog(paneldown, "Inter-repeat Region between compound SSRs requires an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
                if(box1.isSelected()||box2.isSelected()||box3.isSelected()||com.isSelected()||incom.isSelected()){
                    PrintWriter out=null;
                    PrintWriter html=null;
                    boolean orgs=true;
                    DecimalFormat round = new DecimalFormat("#.###");
                    //String file="";
                    for(int i=0;i<organisms.length;i++){

                        String file="organisms/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                        File stats = new File(file);
                        if(!stats.exists()){
                            orgs=false;
                            
                            file ="local/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                        }
                        try {
                            
                            html =  new PrintWriter(new FileWriter(file.substring(0, file.indexOf("."))+".html",true));
                            html.println("<html><h1>******* SUMMARY TABLE *******</h1>");
                            html.println("<table border=\"1\"><tr><td>type</td><td><b>count</b></td><td><b>bp</b></td><td><b>A%</b></td><td><b>T%</b></td><td><b>C%</b></td><td><b>G%</b></td><td><b>Relative Frequency</b></td><td><b>Abundance</b></td><td><b>Relative Abundance</b></td></tr>");
                            
                            out = new PrintWriter(new FileWriter(file,true));
                            out.println("******* SUMMARY TABLE *******");

                            out.println(" _________________________________________________________________________________________________________________ ");
                            out.println("|            |       |            |       |       |       |       |   Relative    |               |   Relative    |");
                            out.println("|    type    | count |     bp     |   A%  |   T%  |   C%  |   G%  |   Frequency   |   Abundance   |   Abundance   |");
                            out.println("|============|=======|============|=======|=======|=======|=======|===============|===============|===============|");


                        } catch (IOException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //TOTAL
                        long seqcount=0;
                        int SSRcount=0;
                        int SSRbpcount=0;
                        int A=0,T=0,G=0,C=0;
                        float relfreq=0;
                        float abfreq=0;

                        //perfect
                        int pcount=0;
                        long pbpcount=0;
                        int Ap=0,Tp=0,Gp=0,Cp=0;
                        //imperfect
                        int imcount=0;
                        long imbpcount=0;
                        int Aim=0,Tim=0,Gim=0,Cim=0;
                        
                        //compound perfect
                        int ccount=0;
                        long cbpcount=0;
                        int Ac=0,Tc=0,Gc=0,Cc=0;
                        //compound imperfect
                        int cicount=0;
                        long cibpcount=0;
                        int Aci=0,Tci=0,Gci=0,Cci=0;

                        if(box1.isSelected()){
                            DataInputStream in=null;
                            if(orgs){
                                try {
                                    in = new DataInputStream(new BufferedInputStream(new FileInputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".perf")));
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }else{
                                try {
                                    in = new DataInputStream(new BufferedInputStream(new FileInputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".perf")));
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            try {
                                seqcount=in.readLong();
                                pcount=in.readInt();
                                pbpcount=in.readLong();
                                Ap=in.readInt();Tp=in.readInt();Gp=in.readInt();Cp=in.readInt();
                                A+=Ap;T+=Tp;G+=Gp;C+=Cp;

                                SSRcount+=pcount;
                                SSRbpcount+=pbpcount;
                                in.close();
                            }catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }if(box2.isSelected()){
                            DataInputStream in=null;
                            if(orgs){
                                try {
                                    in = new DataInputStream(new BufferedInputStream(new FileInputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".imperf")));
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }else{
                                try {
                                    in = new DataInputStream(new BufferedInputStream(new FileInputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".imperf")));
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            try {
                                seqcount=in.readLong();
                                imcount=in.readInt();
                                imbpcount=in.readLong();
                                Aim=in.readInt();Tim=in.readInt();Gim=in.readInt();Cim=in.readInt();
                                A+=Aim;T+=Tim;G+=Gim;C+=Cim;

                                SSRcount+=imcount;
                                SSRbpcount+=imbpcount;
                                in.close();
                            }catch (IOException ex) {
                                    Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }if(box3.isSelected()){
                            if(com.isSelected()){
                                DataInputStream in=null;
                                if(orgs){
                                    try {
                                        in = new DataInputStream(new BufferedInputStream(new FileInputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compp")));
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }else{
                                    try {
                                        in = new DataInputStream(new BufferedInputStream(new FileInputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compp")));
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                try {
                                    seqcount=in.readLong();
                                    ccount=in.readInt();
                                    cbpcount=in.readInt();
                                    Ac=in.readInt();Tc=in.readInt();Gc=in.readInt();Cc=in.readInt();
                                    A+=Ac;T+=Tc;G+=Gc;C+=Cc;
                                    
                                    SSRcount+=ccount;
                                    SSRbpcount+=cbpcount;
                                    in.close();
                                }catch (IOException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if(incom.isSelected()){
                                DataInputStream in=null;
                                if(orgs){
                                    try {
                                        in = new DataInputStream(new BufferedInputStream(new FileInputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compim")));
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }else{
                                    try {
                                        in = new DataInputStream(new BufferedInputStream(new FileInputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compim")));
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                try {
                                    seqcount=in.readLong();
                                    cicount=in.readInt();
                                    cibpcount=in.readInt();
                                    Aci=in.readInt();Tci=in.readInt();Gci=in.readInt();Cci=in.readInt();
                                    A+=Aci;T+=Tci;G+=Gci;C+=Cci;

                                    SSRcount+=cicount;
                                    SSRbpcount+=cibpcount;
                                    in.close();
                                }catch (IOException ex) {
                                        Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        
                        if(box1.isSelected()){
                            relfreq+=(float)pcount/SSRcount;
                            abfreq+=(float)pbpcount/SSRbpcount;
                            out.printf("|Perfect     |" + cell(Integer.toString(pcount), 7) + "|" + cell(Long.toString(pbpcount), 12) + "|%s|%s|%s|%s|" + cell((float) pcount / SSRcount, 15) + "|" + cell((float) pbpcount / seqcount, 15) + "|" + cell((float) pbpcount / SSRbpcount, 15) + "|\n", cell((float) (Ap * 100) / (SSRbpcount), 7), cell((float) (Tp * 100) / (SSRbpcount), 7), cell((float) (Cp * 100) / (SSRbpcount), 7), cell((float) (Gp * 100) / (SSRbpcount), 7));
                            out.println("|------------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");   
                            html.println("<tr><td><b>Perfect</b></td><td>"+pcount+"</td><td>"+pbpcount+"</td><td>"+round.format((float)Ap * 100 / SSRbpcount)+"</td><td>"+round.format((float)Tp * 100 / SSRbpcount)+"</td><td>"+round.format((float)Cp * 100 / SSRbpcount)+"</td><td>"+round.format((float)Gp * 100 / SSRbpcount)+"</td><td>"+round.format((float) pcount / SSRcount)+"</td><td>"+round.format((float) pbpcount / seqcount)+"</td><td>"+round.format((float) pbpcount / SSRbpcount)+"</td></tr>");
                        }
                        if(box2.isSelected()){
                            relfreq+=(float)imcount/SSRcount;
                            abfreq+=(float)imbpcount/SSRbpcount;
                            out.printf("|Imperfect   |" + cell(Integer.toString(imcount), 7) + "|" + cell(Long.toString(imbpcount), 12) + "|%s|%s|%s|%s|" + cell((float) imcount / SSRcount, 15) + "|" + cell((float) imbpcount / seqcount, 15) + "|" + cell((float) imbpcount / SSRbpcount, 15) + "|\n", cell((float) (Aim * 100) / (SSRbpcount), 7), cell((float) (Tim * 100) / (SSRbpcount), 7), cell((float) (Cim * 100) / (SSRbpcount), 7), cell((float) (Gim * 100) / (SSRbpcount), 7));
                            out.println("|------------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");   
                            html.println("<tr><td><b>Imperfect</b></td><td>"+imcount+"</td><td>"+imbpcount+"</td><td>"+round.format((float)Aim * 100 / SSRbpcount)+"</td><td>"+round.format((float)Tim * 100 / SSRbpcount)+"</td><td>"+round.format((float)Cim * 100 / SSRbpcount)+"</td><td>"+round.format((float)Gim * 100 / SSRbpcount)+"</td><td>"+round.format((float) imcount / SSRcount)+"</td><td>"+round.format((float) imbpcount / seqcount)+"</td><td>"+round.format((float) imbpcount / SSRbpcount)+"</td></tr>");
                        }
                        if(box3.isSelected()){
                            if(com.isSelected()){
                                abfreq+=(float)cbpcount/SSRbpcount;
                                relfreq+=(float)ccount/SSRcount;
                                out.printf("|Compound Per|" + cell(Integer.toString(ccount), 7) + "|" + cell(Long.toString(cbpcount), 12) + "|%s|%s|%s|%s|" + cell((float) ccount / SSRcount, 15) + "|" + cell((float) cbpcount / seqcount, 15) + "|" + cell((float) cbpcount / SSRbpcount, 15) + "|\n", cell((float) (Ac * 100) / (SSRbpcount), 7), cell((float) (Tc * 100) / (SSRbpcount), 7), cell((float) (Cc * 100) / (SSRbpcount), 7), cell((float) (Gc * 100) / (SSRbpcount), 7));
                                out.println("|------------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");   
                                html.println("<tr><td><b>Compound Perf.</b></td><td>"+ccount+"</td><td>"+cbpcount+"</td><td>"+round.format((float)Ac * 100 / SSRbpcount)+"</td><td>"+round.format((float)Tc * 100 / SSRbpcount)+"</td><td>"+round.format((float)Cc * 100 / SSRbpcount)+"</td><td>"+round.format((float)Gc * 100 / SSRbpcount)+"</td><td>"+round.format((float) ccount / SSRcount)+"</td><td>"+round.format((float) cbpcount / seqcount)+"</td><td>"+round.format((float) cbpcount / SSRbpcount)+"</td></tr>");
                            }
                            if(incom.isSelected()){
                                abfreq+=(float)cibpcount/SSRbpcount;
                                relfreq+=(float)cicount/SSRcount;
                                out.printf("|Compound Imp|" + cell(Integer.toString(cicount), 7) + "|" + cell(Long.toString(cibpcount), 12) + "|%s|%s|%s|%s|" + cell((float) cicount / SSRcount, 15) + "|" + cell((float) cibpcount / seqcount, 15) + "|" + cell((float) cibpcount / SSRbpcount, 15) + "|\n", cell((float) (Aci * 100) / (SSRbpcount), 7), cell((float) (Tci * 100) / (SSRbpcount), 7), cell((float) (Cci * 100) / (SSRbpcount), 7), cell((float) (Gci * 100) / (SSRbpcount), 7));
                                out.println("|------------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");   
                                html.println("<tr><td><b>Compound Imperf.</b></td><td>"+cicount+"</td><td>"+cibpcount+"</td><td>"+round.format((float)Aci * 100 / SSRbpcount)+"</td><td>"+round.format((float)Tci * 100 / SSRbpcount)+"</td><td>"+round.format((float)Cci * 100 / SSRbpcount)+"</td><td>"+round.format((float)Gci * 100 / SSRbpcount)+"</td><td>"+round.format((float) cicount / SSRcount)+"</td><td>"+round.format((float) cibpcount / seqcount)+"</td><td>"+round.format((float) cibpcount / SSRbpcount)+"</td></tr>");
                            }
                        }
                        
                        out.println("|TOTAL       |" + cell(Integer.toString(SSRcount), 7) + "|" + cell(Long.toString(SSRbpcount), 12) + "|" + cell((float)A * 100 / SSRbpcount, 7) + "|" + cell((float)T * 100 / SSRbpcount, 7) + "|" + cell((float)C * 100 / SSRbpcount, 7) + "|" + cell((float)G * 100 / SSRbpcount, 7) + "|" + cell(relfreq, 15) + "|" + cell((float) SSRbpcount / seqcount, 15) + "|" + cell((float) abfreq, 15) + "|");
                        out.println("|____________|_______|____________|_______|_______|_______|_______|_______________|_______________|_______________|");
                        out.println("Genome length (bp): " + seqcount);
                        out.println("Relative Frequency: Count of each motif type / total SSR count");
                        out.println("Abundance: bp of each motif type / total sequence bp");
                        out.println("Relative Abundance: bp of each motif type / total microsatellites bp");
                        out.println();
                        out.println();
                        out.close();
                        html.println("<tr><td><b>TOTAL</b></td><td>"+SSRcount+"</td><td>"+SSRbpcount+"</td><td>"+round.format((float)A * 100 / SSRbpcount)+"</td><td>"+round.format((float)T * 100 / SSRbpcount)+"</td><td>"+round.format((float)C * 100 / SSRbpcount)+"</td><td>"+round.format((float)G * 100 / SSRbpcount)+"</td><td>"+round.format((float) relfreq)+"</td><td>"+round.format((float) SSRbpcount / seqcount)+"</td><td>"+round.format((float) abfreq)+"</td></tr></table></html>");
                        html.close();
                            
                        
                        try {
                            Runtime.getRuntime().exec("notepad " + file);
                        } catch (IOException ex) {
                            Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                    
                }
                

                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                
                
            }
        });


        selectsp = new JButton("Select new Species");
        selectsp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    input_frame frame = new input_frame();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(updateframe.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
            }
        });


        quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        tab = new JTabbedPane();
        tab.setSize(790, 590);

        // about us panel
        ImageIcon image = new ImageIcon("miga.png");
        JLabel label = new JLabel("", image, JLabel.CENTER);
        contact = new JLabel("<html><b><p>Machine Learning and Knowledge Discovery Group</p><p>Computer Science Department</p><p>Aristotle University of Thessaloniki</p><p> </p><p>In collaboration with the laboratory of</p><p> </p><p>Population Genetics of Animal Organisms</p><p>Department of Genetics, Development and Molecular Biology</p><p>School of Biology</p><p>Aristotle University of Thessaloniki</p><p> </p><p> </p><p>For any comments or information please contact with:</p><p><a href=\"mailto:ikavak@csd.auth.gr\">ikavak@csd.auth.gr</a></p></b></html>");
        about = new JPanel();
        about.setLayout(new GridBagLayout());
        GridBagConstraints ab = new GridBagConstraints();
        
        ab.fill = GridBagConstraints.WEST;
        ab.weightx = 0.5;
        ab.weighty = 0.5;
        ab.gridx = 0;
        ab.gridy = 0;
        about.add(label, ab);
        
        ab.fill = GridBagConstraints.WEST;
        ab.weightx = 0.5;
        ab.weighty = 0.5;
        ab.gridx = 0;
        ab.gridy = 1;
        about.add(contact, ab);
        
        
        //end of about us 
        
        //below are the rest of th panels
        
        paneltop = new JPanel();
        TitledBorder t = BorderFactory.createTitledBorder("Select Period");
        paneltop.setBorder(t);

        paneltop.setLayout(new GridBagLayout());
        GridBagConstraints pt = new GridBagConstraints();

        pt.fill = GridBagConstraints.HORIZONTAL;
        pt.weightx = 0.5;
        pt.weighty = 0.5;
        pt.gridx = 0;
        pt.gridy = 0;
        paneltop.add(mono, pt);

        pt.fill = GridBagConstraints.HORIZONTAL;
        pt.weightx = 0.5;
        pt.weighty = 0.5;
        pt.gridx = 0;
        pt.gridy = -1;
        paneltop.add(di, pt);

        pt.fill = GridBagConstraints.HORIZONTAL;
        pt.weightx = 0.5;
        pt.weighty = 0.5;
        pt.gridx = 0;
        pt.gridy = -2;
        paneltop.add(tri, pt);

        pt.fill = GridBagConstraints.HORIZONTAL;
        pt.weightx = 0.5;
        pt.weighty = 0.5;
        pt.gridx = 0;
        pt.gridy = -3;
        paneltop.add(tetra, pt);

        pt.fill = GridBagConstraints.HORIZONTAL;
        pt.weightx = 0.5;
        pt.weighty = 0.5;
        pt.gridx = 0;
        pt.gridy = -4;
        paneltop.add(penta, pt);

        pt.fill = GridBagConstraints.HORIZONTAL;
        pt.weightx = 0.5;
        pt.weighty = 0.5;
        pt.gridx = 0;
        pt.gridy = -5;
        paneltop.add(hexa, pt);


        panel1 = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Type");
        panel1.setBorder(title);

        panel1.setLayout(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();

        a.fill = GridBagConstraints.HORIZONTAL;
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = 0;
        panel1.add(select, a);

        a.fill = GridBagConstraints.HORIZONTAL;
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = -1;
        panel1.add(box1, a);

        a.fill = GridBagConstraints.HORIZONTAL;
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = -2;
        panel1.add(box2, a);

        a.fill = GridBagConstraints.HORIZONTAL;
        a.weightx = 0.5;
        a.weighty = 0.5;
        a.gridx = 0;
        a.gridy = -3;
        panel1.add(box3, a);

        panel2 = new JPanel();
        panel2.setVisible(false);
        TitledBorder title2 = BorderFactory.createTitledBorder("More Options");
        panel2.setBorder(title2);

        panel2.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();

        b.fill = GridBagConstraints.HORIZONTAL;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 0;
        b.gridy = 0;
        panel2.add(minimumssrlen, b);

        b.fill = GridBagConstraints.CENTER;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 1;
        b.gridy = 0;
        panel2.add(score, b);

        b.fill = GridBagConstraints.HORIZONTAL;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 0;
        b.gridy = -1;
        panel2.add(gapmax, b);

        b.fill = GridBagConstraints.CENTER;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 1;
        b.gridy = -1;
        panel2.add(max, b);


        b.fill = GridBagConstraints.HORIZONTAL;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 0;
        b.gridy = -2;
        panel2.add(minlenpregap, b);

        b.fill = GridBagConstraints.CENTER;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 1;
        b.gridy = -2;
        panel2.add(minpregap, b);

        b.fill = GridBagConstraints.HORIZONTAL;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 0;
        b.gridy = -3;
        panel2.add(gapcomp, b);

        b.fill = GridBagConstraints.CENTER;
        b.weightx = 0.5;
        b.weighty = 0.5;
        b.gridx = 1;
        b.gridy = -3;
        panel2.add(maxgapcomp, b);






        paneldownleft = new JPanel();
        paneldownleft.setVisible(false);
        TitledBorder titledl = BorderFactory.createTitledBorder("Compound SSR options");
        paneldownleft.setBorder(titledl);
        paneldownleft.setLayout(new GridBagLayout());

        GridBagConstraints dl = new GridBagConstraints();

        dl.fill = GridBagConstraints.CENTER;
        dl.weightx = 0.5;
        dl.weighty = 0.5;
        dl.gridx = 0;
        dl.gridy = 0;
        paneldownleft.add(com, dl);

        dl.fill = GridBagConstraints.CENTER;
        dl.weightx = 0.5;
        dl.weighty = 0.5;
        dl.gridx = 0;
        dl.gridy = -1;
        paneldownleft.add(incom, dl);




        paneldownright = new JPanel();
        paneldownright.setLayout(new GridBagLayout());
        GridBagConstraints dr = new GridBagConstraints();


        dr.fill = GridBagConstraints.CENTER;
        dr.weightx = 0.5;
        dr.weighty = 0.5;
        dr.gridx = 0;
        dr.gridy = 0;
        paneldownright.add(show, dr);

        dr.fill = GridBagConstraints.CENTER;
        dr.weightx = 0.5;
        dr.weighty = 0.5;
        dr.gridx = 0;
        dr.gridy = -1;
        paneldownright.add(selectsp, dr);

        dr.fill = GridBagConstraints.CENTER;
        dr.weightx = 0.5;
        dr.weighty = 0.5;
        dr.gridx = 0;
        dr.gridy = -2;
        paneldownright.add(quit, dr);




        panelup = new JPanel();
        TitledBorder titleup = BorderFactory.createTitledBorder("Statistics");
        panelup.setBorder(titleup);



        panelup.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        panelup.add(paneltop, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        panelup.add(panel1, c);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = -1;
        panelup.add(paneldownleft, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 1;
        c.gridy = -1;
        panelup.add(panel2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = -2;
        panelup.add(paneldownright, c);
        
        TitledBorder s = BorderFactory.createTitledBorder("Standardization");
        std.setBorder(s);
        std.add(no_st);
        std.add(part_st);
        std.add(full_st);
        std.setEnabled(false);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = -3;
        panelup.add(std, c);


        tab.add(panelup, "Statistics");


        panelshow = new JPanel();
        TitledBorder ps = BorderFactory.createTitledBorder("Sequence Retrieval");
        panelshow.setBorder(ps);

        panelshow.setLayout(new GridBagLayout());
        GridBagConstraints psg = new GridBagConstraints();


        panelshowup = new JPanel();

        panelshowup.setLayout(new GridBagLayout());
        GridBagConstraints p = new GridBagConstraints();

        p.fill = GridBagConstraints.WEST;
        p.weightx = 0.5;
        p.weighty = 0.5;
        p.gridx = 0;
        p.gridy = 0;
        panelshowup.add(lab, p);


        panelshowd = new JPanel();

        panelshowd.setLayout(new GridBagLayout());
        GridBagConstraints pd = new GridBagConstraints();

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 0;
        pd.gridy = -1;
        panelshowd.add(startlab, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -1;
        panelshowd.add(startnum, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 0;
        pd.gridy = -2;
        panelshowd.add(endlab, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -2;
        panelshowd.add(endnum, pd);


        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 0;
        pd.gridy = -3;
        panelshowd.add(titlelab, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -3;
        panelshowd.add(titlef, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 0;
        pd.gridy = -4;
        panelshowd.add(flk, pd);



        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -4;
        panelshowd.add(new JLabel(" "), pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 0;
        pd.gridy = -5;
        panelshowd.add(flankst, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -5;
        panelshowd.add(flankstn, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 0;
        pd.gridy = -6;
        panelshowd.add(flankend, pd);

        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -6;
        panelshowd.add(flankendn, pd);


        pd.fill = GridBagConstraints.CENTER;
        pd.weightx = 0.5;
        pd.weighty = 0.5;
        pd.gridx = 1;
        pd.gridy = -7;
        panelshowd.add(retrieve, pd);

        psg.fill = GridBagConstraints.CENTER;
        psg.weightx = 0.5;
        psg.weighty = 0.5;
        psg.gridx = 0;
        psg.gridy = 0;
        panelshow.add(panelshowup, psg);

        psg.fill = GridBagConstraints.CENTER;
        psg.weightx = 0.5;
        psg.weighty = 0.5;
        psg.gridx = 0;
        psg.gridy = -1;
        panelshow.add(panelshowd, psg);

        psg.fill = GridBagConstraints.CENTER;
        psg.weightx = 0.5;
        psg.weighty = 0.5;
        psg.gridx = 0;
        psg.gridy = -2;
        panelshow.add(sbrText, psg);



        tab.add(panelshow, "Sequence Retrieval");
        tab.add(about, "About us");
        add(tab);
        setVisible(true);

    }

    public void getPerfectSSRs(String[] organisms, int length, boolean flag) throws FileNotFoundException, SQLException, ClassNotFoundException, IOException {

        for (int i = 0; i < organisms.length; i++) {
             // 18/11/2013 added starting here
            String filetype = "";
            String filepro = "";

            if(flag){
                filetype = "organisms";
                filepro = "organisms/" + organisms[i] + "/data/";
                int ret = getOrganismStatus(organisms[i]);
                if(ret == -1)
                    indexer = new Indexer(chromosomelist);
                else
                    indexer = new Indexer(ret);

            }else{
                filetype = "local";
                filepro = "local/" + organisms[i] + "/data/";
                String indexfile = "local/" + organisms[i] + "/index.txt";
                indexer = new Indexer(indexfile);
            }
            //List<String> files = getFiles(organisms[i], minlen, flag);


            // 18/11/2013 added ending here
            countmono.set(i, 0);
            countdi.set(i, 0);
            counttri.set(i, 0);
            counttetra.set(i, 0);
            countpenta.set(i, 0);
            counthexa.set(i, 0);
            countmonore.set(i, 0);
            countdire.set(i, 0);
            counttrire.set(i, 0);
            counttetrare.set(i, 0);
            countpentare.set(i, 0);
            counthexare.set(i, 0);
            Amono.set(i, 0);
            Adi.set(i, 0);
            Atri.set(i, 0);
            Atetra.set(i, 0);
            Apenta.set(i, 0);
            Ahexa.set(i, 0);
            Tmono.set(i, 0);
            Tdi.set(i, 0);
            Ttri.set(i, 0);
            Ttetra.set(i, 0);
            Tpenta.set(i, 0);
            Thexa.set(i, 0);
            Gmono.set(i, 0);
            Gdi.set(i, 0);
            Gtri.set(i, 0);
            Gtetra.set(i, 0);
            Gpenta.set(i, 0);
            Ghexa.set(i, 0);
            Cmono.set(i, 0);
            Cdi.set(i, 0);
            Ctri.set(i, 0);
            Ctetra.set(i, 0);
            Cpenta.set(i, 0);
            Chexa.set(i, 0);

             while(indexer.hasNext()){
                String files = filepro + indexer.getNextFileName();
            //for (int j = 0; j < files.size(); j++) {

                List<File> exis = new ArrayList<File>();
                exis.add(new File(files + "_" + length + "_monoPerfect.temp"));
                exis.add(new File(files + "_" + length + "_diPerfect.temp"));
                exis.add(new File(files + "_" + length + "_triPerfect.temp"));
                exis.add(new File(files + "_" + length + "_tetraPerfect.temp"));
                exis.add(new File(files + "_" + length + "_pentaPerfect.temp"));
                exis.add(new File(files + "_" + length + "_hexaPerfect.temp"));
                int num = 0;
                for (int temp = 0; temp < exis.size(); temp++) {
                    if (exis.get(temp).exists()) {
                        num++;
                    }
                }

                if (num != exis.size()) {

                    DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files)));

                    //
                    DataOutputStream outmono = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_monoPerfect.temp")));
                    DataOutputStream outdi = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_diPerfect.temp")));
                    DataOutputStream outtri = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_triPerfect.temp")));
                    DataOutputStream outtetra = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_tetraPerfect.temp")));
                    DataOutputStream outpenta = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_pentaPerfect.temp")));
                    DataOutputStream outhexa = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_hexaPerfect.temp")));
                    boolean eof = false;
                    while (!eof) {
                        try {
                            int len = in.readInt();
                            int line = in.readInt();
                            for (int k = 0; k < len; k++) {
                                SSR.add(in.readUTF());
                                int end = in.readInt();
                                repeats.add(in.readInt());
                                EndOfSsr.add(end + (line - 1) * 20000);
                            }

                            for (int c = 0; c < SSR.size(); c++) {
                                
                                if(!SSR.get(c).contains("N")){
                                    if (repeats.get(c) * SSR.get(c).length() >= length) {
                                        if (SSR.get(c).length() == 1) {
                                            countmono.set(i, countmono.get(i)+1); 
                                            countmonore.set(i, countmonore.get(i)+repeats.get(c));
                                            if (SSR.get(c).contains("A")) {
                                                Amono.set(i, Amono.get(i) +repeats.get(c));
                                            }
                                            if (SSR.get(c).contains("T")) {
                                                Tmono.set(i, Tmono.get(i) +repeats.get(c));
                                            }
                                            if (SSR.get(c).contains("G")) {
                                                Gmono.set(i, Gmono.get(i) +repeats.get(c));
                                            }
                                            if (SSR.get(c).contains("C")) {
                                                Cmono.set(i, Cmono.get(i) +repeats.get(c));
                                            }
                                            outmono.writeUTF(SSR.get(c));
                                            outmono.writeInt(repeats.get(c));
                                            outmono.writeInt(EndOfSsr.get(c));
                                        } else if (SSR.get(c).length() == 2) {
                                            countdi.set(i, countdi.get(i) +1);
                                            countdire.set(i, countdire.get(i) +repeats.get(c));
                                            if (SSR.get(c).contains("A")) {
                                                Adi.set(i, Adi.get(i) +repeats.get(c));
                                            }
                                            if (SSR.get(c).contains("T")) {
                                                Tdi.set(i, Tdi.get(i) +repeats.get(c));
                                            }
                                            if (SSR.get(c).contains("G")) {
                                                Gdi.set(i, Gdi.get(i) +repeats.get(c));
                                            }
                                            if (SSR.get(c).contains("C")) {
                                                Cdi.set(i, Cdi.get(i) +repeats.get(c));
                                            }
                                            outdi.writeUTF(SSR.get(c));
                                            outdi.writeInt(repeats.get(c));
                                            outdi.writeInt(EndOfSsr.get(c));
                                        } else if (SSR.get(c).length() == 3) {
                                            counttri.set(i, counttri.get(i) +1);
                                            counttrire.set(i, counttrire.get(i) +repeats.get(c));
                                            if (SSR.get(c).contains("A")) {
                                                Atri.set(i, Atri.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "A"));
                                            }
                                            if (SSR.get(c).contains("T")) {
                                                Ttri.set(i, Ttri.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "T"));
                                            }
                                            if (SSR.get(c).contains("G")) {
                                                Gtri.set(i, Gtri.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "G"));
                                            }
                                            if (SSR.get(c).contains("C")) {
                                                Ctri.set(i, Ctri.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "C"));
                                            }
                                            outtri.writeUTF(SSR.get(c));
                                            outtri.writeInt(repeats.get(c));
                                            outtri.writeInt(EndOfSsr.get(c));
                                        } else if (SSR.get(c).length() == 4) {
                                             counttetra.set(i, counttetra.get(i) +1);
                                            counttetrare.set(i, counttetrare.get(i) +repeats.get(c));
                                            if (SSR.get(c).contains("A")) {
                                                Atetra.set(i, Atetra.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "A"));
                                            }
                                            if (SSR.get(c).contains("T")) {
                                                Ttetra.set(i, Ttetra.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "T"));
                                            }
                                            if (SSR.get(c).contains("G")) {
                                                Gtetra.set(i, Gtetra.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "G"));
                                            }
                                            if (SSR.get(c).contains("C")) {
                                                Ctetra.set(i, Ctetra.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "C"));
                                            }
                                            outtetra.writeUTF(SSR.get(c));
                                            outtetra.writeInt(repeats.get(c));
                                            outtetra.writeInt(EndOfSsr.get(c));
                                        } else if (SSR.get(c).length() == 5) {


                                            countpenta.set(i, countpenta.get(i) +1);
                                            countpentare.set(i, countpentare.get(i) +repeats.get(c));
                                            if (SSR.get(c).contains("A")) {
                                                Apenta.set(i, Apenta.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "A"));
                                            }
                                            if (SSR.get(c).contains("T")) {
                                                Tpenta.set(i, Tpenta.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "T"));
                                            }
                                            if (SSR.get(c).contains("G")) {
                                                Gpenta.set(i, Gpenta.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "G"));
                                            }
                                            if (SSR.get(c).contains("C")) {
                                                Cpenta.set(i, Cpenta.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "C"));
                                            }


                                            outpenta.writeUTF(SSR.get(c));
                                            outpenta.writeInt(repeats.get(c));
                                            outpenta.writeInt(EndOfSsr.get(c));
                                        } else if (SSR.get(c).length() == 6) {


                                            counthexa.set(i, counthexa.get(i) +1);
                                            counthexare.set(i, counthexare.get(i) +repeats.get(c));
                                            if (SSR.get(c).contains("A")) {
                                                Ahexa.set(i, Ahexa.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "A"));
                                            }
                                            if (SSR.get(c).contains("T")) {
                                                Thexa.set(i, Thexa.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "T"));
                                            }
                                            if (SSR.get(c).contains("G")) {
                                                Ghexa.set(i, Ghexa.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "G"));
                                            }
                                            if (SSR.get(c).contains("C")) {
                                                Chexa.set(i, Chexa.get(i) +repeats.get(c)* StringUtils.countMatches(SSR.get(c), "C"));
                                            }
                                            outhexa.writeUTF(SSR.get(c));
                                            outhexa.writeInt(repeats.get(c));
                                            outhexa.writeInt(EndOfSsr.get(c));
                                        }
                                    }
                                }
                            }

                            SSR = new ArrayList<String>();
                            repeats = new ArrayList<Integer>();
                            EndOfSsr = new ArrayList<Integer>();

                        } catch (EOFException e) {
                            eof = true;
                        }
                    }

                    outmono.close();
                    outdi.close();
                    outtri.close();
                    outtetra.close();
                    outpenta.close();
                    outhexa.close();

                    //
                    in.close();
                    DataOutputStream save = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files.substring(0, files.lastIndexOf('/')) + "/perf_stats")));
                    save.writeInt(countmono.get(i));
                    save.writeInt(countdi.get(i));
                    save.writeInt(counttri.get(i));
                    save.writeInt(counttetra.get(i));
                    save.writeInt(countpenta.get(i));
                    save.writeInt(counthexa.get(i));
                    save.writeInt(countmonore.get(i));
                    save.writeInt(countdire.get(i));
                    save.writeInt(counttrire.get(i));
                    save.writeInt(counttetrare.get(i));
                    save.writeInt(countpentare.get(i));
                    save.writeInt(counthexare.get(i));
                    save.writeInt(Amono.get(i));
                    save.writeInt(Tmono.get(i));
                    save.writeInt(Gmono.get(i));
                    save.writeInt(Cmono.get(i));
                    save.writeInt(Adi.get(i));
                    save.writeInt(Tdi.get(i));
                    save.writeInt(Gdi.get(i));
                    save.writeInt(Cdi.get(i));
                    save.writeInt(Atri.get(i));
                    save.writeInt(Ttri.get(i));
                    save.writeInt(Gtri.get(i));
                    save.writeInt(Ctri.get(i));
                    save.writeInt(Atetra.get(i));
                    save.writeInt(Ttetra.get(i));
                    save.writeInt(Gtetra.get(i));
                    save.writeInt(Ctetra.get(i));
                    save.writeInt(Apenta.get(i));
                    save.writeInt(Tpenta.get(i));
                    save.writeInt(Gpenta.get(i));
                    save.writeInt(Cpenta.get(i));
                    save.writeInt(Ahexa.get(i));
                    save.writeInt(Thexa.get(i));
                    save.writeInt(Ghexa.get(i));
                    save.writeInt(Chexa.get(i));
                    save.close();

                } else {
                    DataInputStream save = new DataInputStream(new BufferedInputStream(new FileInputStream(files.substring(0, files.lastIndexOf('/')) + "/perf_stats")));


                    countmono.set(i, save.readInt());
                    countdi.set(i, save.readInt());
                    counttri.set(i, save.readInt());
                    counttetra.set(i, save.readInt());
                    countpenta.set(i, save.readInt());
                    counthexa.set(i, save.readInt());
                    countmonore.set(i, save.readInt());
                    countdire.set(i, save.readInt());
                    counttrire.set(i, save.readInt());
                    counttetrare.set(i, save.readInt());
                    countpentare.set(i, save.readInt());
                    counthexare.set(i, save.readInt());
                    Amono.set(i, save.readInt());
                    Tmono.set(i, save.readInt());
                    Gmono.set(i, save.readInt());
                    Cmono.set(i, save.readInt());
                    Adi.set(i, save.readInt());
                    Tdi.set(i, save.readInt());
                    Gdi.set(i, save.readInt());
                    Cdi.set(i, save.readInt());
                    Atri.set(i, save.readInt());
                    Ttri.set(i, save.readInt());
                    Gtri.set(i, save.readInt());
                    Ctri.set(i, save.readInt());
                    Atetra.set(i, save.readInt());
                    Ttetra.set(i, save.readInt());
                    Gtetra.set(i, save.readInt());
                    Ctetra.set(i, save.readInt());
                    Apenta.set(i, save.readInt());
                    Tpenta.set(i, save.readInt());
                    Gpenta.set(i, save.readInt());
                    Cpenta.set(i, save.readInt());
                    Ahexa.set(i, save.readInt());
                    Thexa.set(i, save.readInt());
                    Ghexa.set(i, save.readInt());
                    Chexa.set(i, save.readInt());
                    save.close();
                }
            }
        }
    }

    public void getImPerfectSSRs(String[] organisms, int length, boolean flag, int gap) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {

        for (int i = 0; i < organisms.length; i++) {

            countmono.set(i, 0);
            countdi.set(i, 0);
            counttri.set(i, 0);
            counttetra.set(i, 0);
            countpenta.set(i, 0);
            counthexa.set(i, 0);
            countmonore.set(i, 0);
            countdire.set(i, 0);
            counttrire.set(i, 0);
            counttetrare.set(i, 0);
            countpentare.set(i, 0);
            counthexare.set(i, 0);
            Amono.set(i, 0);
            Adi.set(i, 0);
            Atri.set(i, 0);
            Atetra.set(i, 0);
            Apenta.set(i, 0);
            Ahexa.set(i, 0);
            Tmono.set(i, 0);
            Tdi.set(i, 0);
            Ttri.set(i, 0);
            Ttetra.set(i, 0);
            Tpenta.set(i, 0);
            Thexa.set(i, 0);
            Gmono.set(i, 0);
            Gdi.set(i, 0);
            Gtri.set(i, 0);
            Gtetra.set(i, 0);
            Gpenta.set(i, 0);
            Ghexa.set(i, 0);
            Cmono.set(i, 0);
            Cdi.set(i, 0);
            Ctri.set(i, 0);
            Ctetra.set(i, 0);
            Cpenta.set(i, 0);
            Chexa.set(i, 0);



            boolean found = false;
            String buffer = new String();
            int seekstart = 0;
            int seekend = 0;
            List<String> ssrs = new ArrayList<String>();
            // 18/11/2013 added starting here
            String filetype = "";
            String filepro = "";

            if(flag){
                filetype = "organisms";
                filepro = "organisms/" + organisms[i] + "/data/";
                int ret = getOrganismStatus(organisms[i]);
                if(ret == -1)
                    indexer = new Indexer(chromosomelist);
                else
                    indexer = new Indexer(ret);

            }else{
                filetype = "local";
                filepro = "local/" + organisms[i] + "/data/";
                String indexfile = "local/" + organisms[i] + "/index.txt";
                indexer = new Indexer(indexfile);
            }
            //List<String> files = getFiles(organisms[i], minlen, flag);


            // 18/11/2013 added ending here
             while(indexer.hasNext()){
                String files = filepro + indexer.getNextFileName();

                List<File> exis = new ArrayList<File>();
                exis.add(new File(files + "_" + length + "_" + gap + "_monoImPerfect.temp"));
                exis.add(new File(files + "_" + length + "_" + gap + "_diImPerfect.temp"));
                exis.add(new File(files + "_" + length + "_" + gap + "_triImPerfect.temp"));
                exis.add(new File(files + "_" + length + "_" + gap + "_tetraImPerfect.temp"));
                exis.add(new File(files + "_" + length + "_" + gap + "_pentaImPerfect.temp"));
                exis.add(new File(files + "_" + length + "_" + gap + "_hexaImPerfect.temp"));
                int num = 0;
                for (int temp = 0; temp < exis.size(); temp++) {
                    if (exis.get(temp).exists()) {
                        num++;
                    }
                }

                if (num != exis.size()) {

                    DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files)));
                    DataOutputStream outmono = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_" + gap + "_monoImPerfect.temp")));
                    DataOutputStream outdi = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_" + gap + "_diImPerfect.temp")));
                    DataOutputStream outtri = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_" + gap + "_triImPerfect.temp")));
                    DataOutputStream outtetra = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_" + gap + "_tetraImPerfect.temp")));
                    DataOutputStream outpenta = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_" + gap + "_pentaImPerfect.temp")));
                    DataOutputStream outhexa = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files + "_" + length + "_" + gap + "_hexaImPerfect.temp")));
                    boolean eof = false;
                    while (!eof) {
                        try {
                            SSR = new ArrayList<String>();
                            repeats = new ArrayList<Integer>();
                            EndOfSsr = new ArrayList<Integer>();
                            start = new ArrayList<Integer>();

                            int len = in.readInt();
                            int line = in.readInt();
                            //try{
                                for (int k = 0; k < len; k++) {
                                    String temp = in.readUTF();
                                    //THIS
                                    //if(!temp.contains("N")){
                                    SSR.add(temp);
                                    EndOfSsr.add(in.readInt());
                                    repeats.add(in.readInt());
                                    start.add(EndOfSsr.get(k) - (SSR.get(k).length() * repeats.get(k)));
                                    /*}else{
                                        int junk = in.readInt();
                                        junk = in.readInt();
                                        k--;
                                        len--;
                                    }*/
                                }
                            /*}
                            catch(IndexOutOfBoundsException e){
                                System.out.println(SSR.size());
                                System.out.println(EndOfSsr.size());
                                System.out.println(repeats.size());
                                System.out.println(start.size());
                            }*/
                            

                            List<String> SSRlen = new ArrayList<String>();
                            List<Integer> Endlen = new ArrayList<Integer>();
                            List<Integer> repslen = new ArrayList<Integer>();
                            List<Integer> startlen = new ArrayList<Integer>();

                            for (int k = 0; k < SSR.size(); k++) {
                                if (SSR.get(k).length() * repeats.get(k) >= length) {
                                    SSRlen.add(SSR.get(k));
                                    Endlen.add(EndOfSsr.get(k));
                                    repslen.add(repeats.get(k));
                                    startlen.add(start.get(k));
                                }
                            }


                            List<Integer> sortedstart = new ArrayList<Integer>();

                            List<Integer> sortedend = new ArrayList<Integer>();
                            for (int t = 0; t < startlen.size(); t++) {
                                sortedstart.add(startlen.get(t));
                                sortedend.add(Endlen.get(t));
                            }

                            Collections.sort(sortedstart);
                            Collections.sort(sortedend);


                            //List<String> tofile = new ArrayList<String>();
                            for (int k = 0; k < sortedstart.size() - 1; k++) {
                                found = false;
                                ssrs.clear();
                                ssrs = new ArrayList<String>();
                                if (sortedstart.get(k + 1) - sortedend.get(k) <= gap && sortedstart.get(k + 1) - sortedend.get(k) >= 0) {
                                    seekstart = sortedstart.get(k);
                                    while (k < sortedstart.size() - 1 && sortedstart.get(k + 1) - sortedend.get(k) <= gap && sortedstart.get(k + 1) - sortedend.get(k) >= 0) {
                                        for (int c = 0; c < startlen.size(); c++) {
                                            if (sortedstart.get(k) == startlen.get(c)) {
                                                ssrs.add(SSRlen.get(c));
                                            }
                                            if (sortedstart.get(k + 1) == startlen.get(c)) {
                                                ssrs.add(SSRlen.get(c));
                                                seekend = Endlen.get(c);
                                                found = true;
                                            }
                                        }
                                        k++;
                                    }
                                    k--;
                                }
                                boolean check = checkallsame(ssrs);
                                if (found && check) {
                                    BufferedReader stdin = null;
                                    if (flag) {
                                        String[] temp = files.split("/");
                                        boolean type = CheckForKaryotype(organisms[i]);
                                        String newdir = "";
                                        if (type) {
                                            newdir = temp[0] + "/" + temp[1] + "/chrom-" + temp[3].substring(0, temp[3].lastIndexOf('.')) + "-slices.txt";
                                        } else {
                                            newdir = temp[0] + "/" + temp[1] + "/slice-" + temp[3].substring(0, temp[3].lastIndexOf('.')) + ".txt";
                                        }

                                        stdin = new BufferedReader(new FileReader(newdir));
                                    } else {
                                        //files.add("local/" + organism + "/data/" + temp + ".ssr");
                                        String[] temp = files.split("data/");
                                        String newdir = temp[0] + "/" + temp[1].substring(0, temp[1].lastIndexOf('.')) + ".txt";
                                        stdin = new BufferedReader(new FileReader(newdir));
                                    }
                                    buffer = "";
                                    for (int c = 0; c < line; c++) {
                                        buffer = stdin.readLine();
                                    }
                                    //System.out.println(buffer.length() + "\t" + seekstart + "\t" + seekend);
                                    int real_end = ((Integer) (line - 1) * 20000) + seekend;
                                    int real_start = ((Integer) (line - 1) * 20000) + seekstart;
                                    //tofile.add("SSR: "+buffer.substring(seekstart, seekend) + "start-end: "+ real_start + "-" +real_end );

                                    if (ssrs.get(1).length() == 1) {
                                        countmono.set(i, countmono.get(i)+1); 
                                        countmonore.set(i, countmonore.get(i)+real_end - real_start);
                                        outmono.writeUTF(buffer.substring(seekstart+1, seekend+1));
                                        if (buffer.substring(seekstart+1, seekend+1).contains("A") || buffer.substring(seekstart+1, seekend+1).contains("a")) {
                                            Amono.set(i, Amono.get(i) +StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "A") + StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "a"));
                                        }
                                        if (buffer.substring(seekstart+1, seekend+1).contains("T") || buffer.substring(seekstart+1, seekend+1).contains("t")) {
                                            Tmono.set(i, Tmono.get(i) +StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "T") + StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "t"));
                                        }
                                        if (buffer.substring(seekstart+1, seekend+1).contains("G") || buffer.substring(seekstart+1, seekend+1).contains("g")) {
                                            Gmono.set(i, Gmono.get(i) +StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "G") + StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "g"));
                                        }
                                        if (buffer.substring(seekstart+1, seekend+1).contains("C") || buffer.substring(seekstart+1, seekend+1).contains("c")) {
                                            Cmono.set(i, Cmono.get(i) +StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "C") + StringUtils.countMatches(buffer.substring(seekstart+1, seekend+1), "c"));
                                        }
                                        outmono.writeInt(real_start+1);
                                        outmono.writeInt(real_end+1);
                                    } else if (ssrs.get(1).length() == 2) {
                                        countdi.set(i, countdi.get(i)+1); 
                                        countdire.set(i, countdire.get(i)+real_end - real_start);
                                        
                                        
                                        if (buffer.substring(seekstart, seekend).contains("A") || buffer.substring(seekstart, seekend).contains("a")) {
                                            Adi.set(i, Adi.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "A") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "a"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("T") || buffer.substring(seekstart, seekend).contains("t")) {
                                            Tdi.set(i, Tdi.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "T") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "t"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("G") || buffer.substring(seekstart, seekend).contains("g")) {
                                            Gdi.set(i, Gdi.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "G") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "g"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("C") || buffer.substring(seekstart, seekend).contains("c")) {
                                            Cdi.set(i, Cdi.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "C") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "c"));
                                        }
                                        
                                        outdi.writeUTF(buffer.substring(seekstart, seekend));
                                        outdi.writeInt(real_start);
                                        outdi.writeInt(real_end);

                                    } else if (ssrs.get(1).length() == 3) {
                                        
                                        counttri.set(i, counttri.get(i)+1); 
                                        counttrire.set(i, counttrire.get(i)+real_end - real_start);
                                        
                                        
                                        if (buffer.substring(seekstart, seekend).contains("A") || buffer.substring(seekstart, seekend).contains("a")) {
                                            Atri.set(i, Atri.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "A") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "a"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("T") || buffer.substring(seekstart, seekend).contains("t")) {
                                            Ttri.set(i, Ttri.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "T") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "t"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("G") || buffer.substring(seekstart, seekend).contains("g")) {
                                            Gtri.set(i, Gtri.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "G") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "g"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("C") || buffer.substring(seekstart, seekend).contains("c")) {
                                            Ctri.set(i, Ctri.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "C") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "c"));
                                        }
                                        
                                        outtri.writeUTF(buffer.substring(seekstart, seekend));
                                        outtri.writeInt(real_start);
                                        outtri.writeInt(real_end);

                                    } else if (ssrs.get(1).length() == 4) {
                                        
                                        counttetra.set(i, counttetra.get(i)+1); 
                                        counttetrare.set(i, counttetrare.get(i)+real_end - real_start);
                                        
                                        
                                        if (buffer.substring(seekstart, seekend).contains("A") || buffer.substring(seekstart, seekend).contains("a")) {
                                            Atetra.set(i, Atetra.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "A") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "a"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("T") || buffer.substring(seekstart, seekend).contains("t")) {
                                            Ttetra.set(i, Ttetra.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "T") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "t"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("G") || buffer.substring(seekstart, seekend).contains("g")) {
                                            Gtetra.set(i, Gtetra.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "G") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "g"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("C") || buffer.substring(seekstart, seekend).contains("c")) {
                                            Ctetra.set(i, Ctetra.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "C") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "c"));
                                        }
                                        outtetra.writeUTF(buffer.substring(seekstart, seekend));
                                        outtetra.writeInt(real_start);
                                        outtetra.writeInt(real_end);

                                    } else if (ssrs.get(1).length() == 5) {
                                        countpenta.set(i, countpenta.get(i)+1); 
                                        countpentare.set(i, countpentare.get(i)+real_end - real_start);
                                        
                                        
                                        if (buffer.substring(seekstart, seekend).contains("A") || buffer.substring(seekstart, seekend).contains("a")) {
                                            Apenta.set(i, Apenta.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "A") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "a"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("T") || buffer.substring(seekstart, seekend).contains("t")) {
                                            Tpenta.set(i, Tpenta.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "T") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "t"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("G") || buffer.substring(seekstart, seekend).contains("g")) {
                                            Gpenta.set(i, Gpenta.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "G") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "g"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("C") || buffer.substring(seekstart, seekend).contains("c")) {
                                            Cpenta.set(i, Cpenta.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "C") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "c"));
                                        }
                                        
                                        outpenta.writeUTF(buffer.substring(seekstart, seekend));
                                        outpenta.writeInt(real_start);
                                        outpenta.writeInt(real_end);

                                    } else if (ssrs.get(1).length() == 6) {
                                        counthexa.set(i, counthexa.get(i)+1); 
                                        counthexare.set(i, counthexare.get(i)+real_end - real_start);
                                        
                                        
                                        if (buffer.substring(seekstart, seekend).contains("A") || buffer.substring(seekstart, seekend).contains("a")) {
                                            Ahexa.set(i, Ahexa.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "A") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "a"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("T") || buffer.substring(seekstart, seekend).contains("t")) {
                                            Thexa.set(i, Thexa.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "T") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "t"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("G") || buffer.substring(seekstart, seekend).contains("g")) {
                                            Ghexa.set(i, Ghexa.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "G") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "g"));
                                        }
                                        if (buffer.substring(seekstart, seekend).contains("C") || buffer.substring(seekstart, seekend).contains("c")) {
                                            Chexa.set(i, Chexa.get(i) +StringUtils.countMatches(buffer.substring(seekstart, seekend), "C") + StringUtils.countMatches(buffer.substring(seekstart, seekend), "c"));
                                        }
                                        
                                        outhexa.writeUTF(buffer.substring(seekstart, seekend));
                                        outhexa.writeInt(real_start);
                                        outhexa.writeInt(real_end);

                                    }
                                    //out.println("SSR: " + buffer.substring(seekstart, seekend) + " start-end: " + real_start + "-" + real_end);
                                    stdin.close();


                                }
                            }



                        } catch (EOFException e) {
                            eof = true;
                        }
                    }
                    in.close();
                    outmono.close();
                    outdi.close();
                    outtri.close();
                    outtetra.close();
                    outpenta.close();
                    outhexa.close();

                    DataOutputStream save = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files.substring(0, files.lastIndexOf('/')) + "/imperf_stats")));
                    save.writeInt(countmono.get(i));
                    save.writeInt(countdi.get(i));
                    save.writeInt(counttri.get(i));
                    save.writeInt(counttetra.get(i));
                    save.writeInt(countpenta.get(i));
                    save.writeInt(counthexa.get(i));
                    save.writeInt(countmonore.get(i));
                    save.writeInt(countdire.get(i));
                    save.writeInt(counttrire.get(i));
                    save.writeInt(counttetrare.get(i));
                    save.writeInt(countpentare.get(i));
                    save.writeInt(counthexare.get(i));
                    save.writeInt(Amono.get(i));
                    save.writeInt(Tmono.get(i));
                    save.writeInt(Gmono.get(i));
                    save.writeInt(Cmono.get(i));
                    save.writeInt(Adi.get(i));
                    save.writeInt(Tdi.get(i));
                    save.writeInt(Gdi.get(i));
                    save.writeInt(Cdi.get(i));
                    save.writeInt(Atri.get(i));
                    save.writeInt(Ttri.get(i));
                    save.writeInt(Gtri.get(i));
                    save.writeInt(Ctri.get(i));
                    save.writeInt(Atetra.get(i));
                    save.writeInt(Ttetra.get(i));
                    save.writeInt(Gtetra.get(i));
                    save.writeInt(Ctetra.get(i));
                    save.writeInt(Apenta.get(i));
                    save.writeInt(Tpenta.get(i));
                    save.writeInt(Gpenta.get(i));
                    save.writeInt(Cpenta.get(i));
                    save.writeInt(Ahexa.get(i));
                    save.writeInt(Thexa.get(i));
                    save.writeInt(Ghexa.get(i));
                    save.writeInt(Chexa.get(i));
                    save.close();

                } else {
                    DataInputStream save = new DataInputStream(new BufferedInputStream(new FileInputStream(files.substring(0, files.lastIndexOf('/')) + "/imperf_stats")));


                    countmono.set(i, save.readInt());
                    countdi.set(i, save.readInt());
                    counttri.set(i, save.readInt());
                    counttetra.set(i, save.readInt());
                    countpenta.set(i, save.readInt());
                    counthexa.set(i, save.readInt());
                    countmonore.set(i, save.readInt());
                    countdire.set(i, save.readInt());
                    counttrire.set(i, save.readInt());
                    counttetrare.set(i, save.readInt());
                    countpentare.set(i, save.readInt());
                    counthexare.set(i, save.readInt());
                    Amono.set(i, save.readInt());
                    Tmono.set(i, save.readInt());
                    Gmono.set(i, save.readInt());
                    Cmono.set(i, save.readInt());
                    Adi.set(i, save.readInt());
                    Tdi.set(i, save.readInt());
                    Gdi.set(i, save.readInt());
                    Cdi.set(i, save.readInt());
                    Atri.set(i, save.readInt());
                    Ttri.set(i, save.readInt());
                    Gtri.set(i, save.readInt());
                    Ctri.set(i, save.readInt());
                    Atetra.set(i, save.readInt());
                    Ttetra.set(i, save.readInt());
                    Gtetra.set(i, save.readInt());
                    Ctetra.set(i, save.readInt());
                    Apenta.set(i, save.readInt());
                    Tpenta.set(i, save.readInt());
                    Gpenta.set(i, save.readInt());
                    Cpenta.set(i, save.readInt());
                    Ahexa.set(i, save.readInt());
                    Thexa.set(i, save.readInt());
                    Ghexa.set(i, save.readInt());
                    Chexa.set(i, save.readInt());
                    save.close();
                }
            }


        }

    }

    public void getCompoundPerfectSSRs(String[] organisms, int length, boolean flag, int gap) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
        String statsfile = "";
        for (int i = 0; i < organisms.length; i++) {            
            boolean found = false;
            String buffer = new String();
            int seekstart = 0;
            int seekend = 0;
            List<String> ssrs = new ArrayList<String>();
            // 18/11/2013 added starting here
            String filetype = "";
            String filepro = "";

            if(flag){
                filetype = "organisms";
                filepro = "organisms/" + organisms[i] + "/data/";
                int ret = getOrganismStatus(organisms[i]);
                if(ret == -1)
                    indexer = new Indexer(chromosomelist);
                else
                    indexer = new Indexer(ret);

            }else{
                filetype = "local";
                filepro = "local/" + organisms[i] + "/data/";
                String indexfile = "local/" + organisms[i] + "/index.txt";
                indexer = new Indexer(indexfile);
            }
            //List<String> files = getFiles(organisms[i], minlen, flag);


            // 18/11/2013 added ending here

            PrintWriter stats = null;
            PrintWriter html = null;
            
            PrintWriter out;
            DataOutputStream lt=null;
            if (filetype.contains("organism")) {
                
                File f = new File("organisms/" + organisms[i] + "/stats/");
                if (!f.exists()) {
                    f.mkdir();
                }

                stats = new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compp")));
                html =  new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                                                             
                
                File fi = new File("organisms/" + organisms[i] + "/results/");
                if (!fi.exists()) {
                    fi.mkdir();
                }
                String toopen = "organisms/" + organisms[i] + "/results/allCompPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                statsfile = toopen;
                out = new PrintWriter(toopen);
                out.println("Results for organism: " + organisms[i] + "\t Search Parameters --> Maximum Inter-repeat Region for Perfect Compound SSRs (bp) : " + gap + " - minimum SSR length (bp): " + length);
            } else{
                File f = new File("local/" + organisms[i] + "/stats/");
                if (!f.exists()) {
                    f.mkdir();
                }

                stats = new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compp")));
                html =  new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                
                
                File fi = new File("local/" + organisms[i] + "/results/");
                if (!fi.exists()) {
                    fi.mkdir();
                }
                String toopen = "local/" + organisms[i] + "/results/allCompPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                statsfile = toopen;
                out = new PrintWriter(toopen);
                out.println("Results for project: " + organisms[i] + "\t Search Parameters --> Maximum Inter-repeat Region for Perfect Compound SSRs (bp) : " + gap + " - minimum SSR length (bp): " + length);
            }
            
            
            int countpc=0;
            int bpcount=0,Aperc=0,Tperc=0,Gperc=0,Cperc=0;
            
             while(indexer.hasNext()){
                String files = filepro + indexer.getNextFileName();
                DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files)));

                boolean eof = false;
                while (!eof) {
                    try {
                        SSR = new ArrayList<String>();
                        repeats = new ArrayList<Integer>();
                        EndOfSsr = new ArrayList<Integer>();
                        start = new ArrayList<Integer>();

                        int len = in.readInt();
                        int line = in.readInt();
                        for (int k = 0; k < len; k++) {
                            String temp = in.readUTF();
                            if(!temp.contains("N")){
                                SSR.add(temp);
                                EndOfSsr.add(in.readInt());
                                repeats.add(in.readInt());
                                int st=EndOfSsr.get(k) - (SSR.get(k).length() * repeats.get(k));
                                if(st>=0)
                                    start.add(st);
                                else
                                    start.add(0);
                            }else{
                                int junk =in.readInt();
                                junk =in.readInt();
                            }

                            /*
                            int real_end = end+(line-1)*20000;
                            
                            int start = real_end - (ssr.length()*reps);
                            
                            //SSR.add(ssr);
                            repeats.add(in.readInt());
                            EndOfSsr.add(real_end);
                            this.start.add(start);
                             * 
                             */
                        }

                        List<String> SSRlen = new ArrayList<String>();
                        List<Integer> Endlen = new ArrayList<Integer>();
                        List<Integer> repslen = new ArrayList<Integer>();
                        List<Integer> startlen = new ArrayList<Integer>();

                        for (int k = 0; k < SSR.size(); k++) {
                            if (SSR.get(k).length() * repeats.get(k) >= length) {
                                SSRlen.add(SSR.get(k));
                                Endlen.add(EndOfSsr.get(k));
                                repslen.add(repeats.get(k));
                                startlen.add(start.get(k));
                            }
                        }


                        List<Integer> sortedstart = new ArrayList<Integer>();

                        List<Integer> sortedend = new ArrayList<Integer>();
                        for (int t = 0; t < startlen.size(); t++) {
                            sortedstart.add(startlen.get(t));
                            sortedend.add(Endlen.get(t));
                        }

                        Collections.sort(sortedstart);
                        Collections.sort(sortedend);


                        for (int k = 0; k < sortedstart.size() - 2; k++) {
                            found = false;

                            ssrs = new ArrayList<String>();
                            if (sortedstart.get(k + 1) - sortedend.get(k) <= gap && sortedstart.get(k + 1) - sortedend.get(k) >= 0) {
                                seekstart = sortedstart.get(k);
                                while (k < sortedstart.size() - 1 && sortedstart.get(k + 1) - sortedend.get(k) <= gap && sortedstart.get(k + 1) - sortedend.get(k) >= 0) {
                                    for (int c = 0; c < startlen.size(); c++) {
                                        if (sortedstart.get(k) == startlen.get(c)) {
                                            ssrs.add(SSRlen.get(c));
                                        }
                                        if (sortedstart.get(k + 1) == startlen.get(c)) {
                                            ssrs.add(SSRlen.get(c));
                                            seekend = Endlen.get(c);
                                            found = true;
                                        }
                                    }
                                    k++;
                                }
                                k--;
                            }
                            boolean check = checkalldiff(ssrs);
                            if (found && check) {
                                BufferedReader stdin = null;
                                String newdir="";
                                if (flag) {
                                    String[] temp = files.split("/");

                                    boolean type = CheckForKaryotype(organisms[i]);
                                    newdir = "";
                                    if (type) {
                                        newdir = temp[0] + "/" + temp[1] + "/chrom-" + temp[3].substring(0, temp[3].lastIndexOf('.')) + "-slices.txt";
                                    } else {
                                        newdir = temp[0] + "/" + temp[1] + "/slice-" + temp[3].substring(0, temp[3].lastIndexOf('.')) + ".txt";
                                    }
                                    stdin = new BufferedReader(new FileReader(newdir));

                                } else {
                                    String[] temp = files.split("data/");
                                    newdir = temp[0] + "/" + temp[1].substring(0, temp[1].lastIndexOf('.')) + ".txt";
                                    stdin = new BufferedReader(new FileReader(newdir));
                                }



                                buffer = "";
                                String prebuf="";
                                for (int c = 0; c < line; c++) {
                                    buffer = stdin.readLine();
                                }
                                stdin.close();
                                //System.out.println(buffer.length() + "\t" + seekstart + "\t" + seekend);
                                int real_end = (line - 1) * 20000 + seekend;
                                int real_start = (line - 1) * 20000 + seekstart;
                                //tofile.add("SSR: "+buffer.substring(seekstart, seekend) + "start-end: "+ real_start + "-" +real_end );
                                countpc++;
                                String tmp="";
                                if(seekstart<0){
                                    stdin = new BufferedReader(new FileReader(newdir));
                                    for (int c = 0; c < line-1; c++) {
                                        prebuf = stdin.readLine();
                                    }
                                    stdin.close();
                                    tmp+=prebuf.substring(prebuf.length()+seekstart);
                                    tmp+=buffer.substring(0, seekend);
                                }else
                                    tmp=buffer.substring(seekstart, seekend);
                                
                                bpcount+=tmp.length();
                                if (tmp.contains("A")) {
                                    Aperc += StringUtils.countMatches(tmp, "A");
                                }
                                if (tmp.contains("T")) {
                                    Tperc += StringUtils.countMatches(tmp, "T");
                                }
                                if (tmp.contains("G")) {
                                    Gperc += StringUtils.countMatches(tmp, "G");
                                }
                                if (tmp.contains("C")) {
                                    Cperc += StringUtils.countMatches(tmp, "C");
                                }
                                
                                out.println("SSR: " + tmp + " start-end: " + real_start + "-" + real_end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                
                            }
                        }
                    } catch (EOFException e) {
                        eof = true;
                    }
                }
                in.close();
            }
            out.close();
            Runtime.getRuntime().exec("notepad " + statsfile);
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection con = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
            } catch (SQLException ex) {
                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            Statement st = null;
            try {
                st = con.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            st.executeUpdate("use lobid");
            
            int seqcount=0;
                                    
            if (filetype.contains("organisms")) {
                ResultSet rs = st.executeQuery("SELECT end FROM slices INNER JOIN organism WHERE slices.org_id=organism.org_id AND organism.name='" + organisms[i] + "'");
                while (rs.next()) {
                    seqcount += Long.parseLong(rs.getString(1));
                }
            } else if (filetype.contains("local")) {
                BufferedReader in = new BufferedReader(new FileReader("local/" + organisms[i] + "/index.txt"));
                int count = countlines("local/" + organisms[i] + "/index.txt");
                for (int c = 0; c < count; c++) {
                    String temp = in.readLine();
                    BufferedReader tmp = new BufferedReader(new FileReader("local/" + organisms[i] + "/" + temp + ".txt"));

                    boolean eof=false;
                    while (!eof) {
                        String s = tmp.readLine();
                        if(s!=null){
                            seqcount+=s.length();
                        }
                        else{
                            eof = true;
                        }
                    }
                    tmp.close();
                }
            }
            
                                            
            DecimalFormat round = new DecimalFormat("#.###");

            html.println("<html><h1>******* Compound Perfect SSRs *******</h1>");
            html.println("<h4>Results for project: " + organisms[i] + "</h4><h4>Search Parameters --> Maximum Inter-repeat Region for Perfect Compound SSRs (bp) : " + gap + "</h4><h4>minimum SSR length (bp): " + length+"</h4>"); 
            html.println("<table border=\"1\"><tr><td> </td><td><b>count</b></td><td><b>bp</b></td><td><b>A%</b></td><td><b>T%</b></td><td><b>C%</b></td><td><b>G%</b></td><td><b>Relative Frequency</b></td><td><b>Abundance</b></td><td><b>Relative Abundance</b></td></tr>");
            html.println("<tr><td><b>Compound Perf.</b></td><td>"+countpc+"</td><td>"+bpcount+"</td><td>"+round.format((float)Aperc * 100 / bpcount)+"</td><td>"+round.format((float)Tperc * 100 / bpcount)+"</td><td>"+round.format((float)Cperc * 100 / bpcount)+"</td><td>"+round.format((float)Gperc * 100 / bpcount)+"</td><td>"+round.format((float)countpc / countpc)+"</td><td>"+round.format((float) bpcount / seqcount)+"</td><td>"+round.format((float) bpcount / bpcount)+"</td></tr>");
            html.println("<tr><td><b>TOTAL</b></td><td>"+countpc+"</td><td>"+bpcount+"</td><td>"+round.format((float)Aperc * 100 / bpcount)+"</td><td>"+round.format((float)Tperc * 100 / bpcount)+"</td><td>"+round.format((float)Cperc * 100 / bpcount)+"</td><td>"+round.format((float)Gperc * 100 / bpcount)+"</td><td>"+round.format((float)countpc / countpc)+"</td><td>"+round.format((float) bpcount / seqcount)+"</td><td>"+round.format((float) bpcount / bpcount)+"</td></tr></table></html>");
            html.close();
            
            
            stats.println("******* Compound Perfect SSRs *******");
            stats.println("Results for project: " + organisms[i] + "\nSearch Parameters --> Maximum Inter-repeat Region for Perfect Compound SSRs (bp) : " + gap + "\nminimum SSR length (bp): " + length);

            stats.println(" ___________________________________________________________________________________________________________________ ");
            stats.println("|              |       |            |       |       |       |       |   Relative    |               |   Relative    |");
            stats.println("|              | count |     bp     |   A%  |   T%  |   C%  |   G%  |   Frequency   |   Abundance   |   Abundance   |");
            stats.println("|==============|=======|============|=======|=======|=======|=======|===============|===============|===============|");
            stats.printf("|Compound Perf.|" + cell(Integer.toString(countpc), 7) + "|" + cell(Integer.toString(bpcount), 12) + "|%s|%s|%s|%s|" + cell((float) countpc / countpc, 15) + "|" + cell((float) bpcount / seqcount, 15) + "|" + cell((float) bpcount / bpcount, 15) + "|\n", cell((float) Aperc * 100 / bpcount, 7), cell((float) Tperc * 100 / bpcount, 7), cell((float) Cperc * 100 / bpcount, 7), cell((float) Gperc * 100 / bpcount, 7));
            stats.println("|--------------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    
            lt.writeLong(seqcount);lt.writeInt(countpc);lt.writeInt(bpcount);lt.writeInt(Aperc);lt.writeInt(Tperc);lt.writeInt(Gperc);lt.writeInt(Cperc);
                                    
            
            stats.println("|TOTAL         |" + cell(Integer.toString(countpc), 7) + "|" + cell(Long.toString(bpcount), 12) + "|" + cell((float)Aperc * 100 / bpcount, 7) + "|" + cell((float)Tperc * 100 / bpcount, 7) + "|" + cell((float)Cperc * 100 / bpcount, 7) + "|" + cell((float)Gperc * 100 / bpcount, 7) + "|" + cell((float) countpc / countpc, 15) + "|" + cell((float) bpcount / seqcount, 15) + "|" + cell((float) bpcount/bpcount, 15) + "|");
            stats.println("|______________|_______|____________|_______|_______|_______|_______|_______________|_______________|_______________|");
            stats.println("Genome length (bp): " + seqcount);
            stats.println("Relative Frequency: Count of each motif type / total SSR count");
            stats.println("Abundance: bp of each motif type / total sequence bp");
            stats.println("Relative Abundance: bp of each motif type / total microsatellites bp");
            stats.println();
            stats.println();
            stats.close();
            lt.close();
                                


        }

    }

    public void getImPerfectCompoundSSRs(String[] organisms, int length, boolean flag, int gap) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {

        String statsfile = "";
        for (int i = 0; i < organisms.length; i++) {
            boolean found = false;
            String buffer = new String();
            int seekstart = 0;
            int seekend = 0;
            List<String> ssrs = new ArrayList<String>();
            // 18/11/2013 added starting here
            String filetype = "";
            String filepro = "";

            if(flag){
                filetype = "organisms";
                filepro = "organisms/" + organisms[i] + "/data/";
                int ret = getOrganismStatus(organisms[i]);
                if(ret == -1)
                    indexer = new Indexer(chromosomelist);
                else
                    indexer = new Indexer(ret);

            }else{
                filetype = "local";
                filepro = "local/" + organisms[i] + "/data/";
                String indexfile = "local/" + organisms[i] + "/index.txt";
                indexer = new Indexer(indexfile);
            }
            //List<String> files = getFiles(organisms[i], minlen, flag);


            // 18/11/2013 added ending here
            PrintWriter out;
            PrintWriter stats;
            PrintWriter html;
            DataOutputStream lt=null;
            if (filetype.contains("organism")) {

                File f = new File("organisms/" + organisms[i] + "/stats/");
                if (!f.exists()) {
                    f.mkdir();
                }

                stats = new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("organisms/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compim")));
                html =  new PrintWriter(new FileWriter("organisms/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                
                
                File fi = new File("organisms/" + organisms[i] + "/results/");
                if (!fi.exists()) {
                    fi.mkdir();
                }
                
                String toopen = "organisms/" + organisms[i] + "/results/allCompImPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                statsfile = toopen;
                out = new PrintWriter(toopen);
                out.println("Results for organism: " + organisms[i] + "\t Search Parameters --> Maximum Inter-repeat Region for Imperfect Compound SSRs(bp) : " + gap + " - minimum SSR length(bp): " + length);

            } else {

                File f = new File("local/" + organisms[i] + "/stats/");
                if (!f.exists()) {
                    f.mkdir();
                }

                stats = new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" + now.toString().replace(':', '_').replace(' ', '_') + ".txt", true));
                lt = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("local/" + organisms[i] + "/data/"+now.toString().replace(':', '_').replace(' ', '_')+".compim")));
                html =  new PrintWriter(new FileWriter("local/" + organisms[i] + "/stats/" + "summary_statistics" +now.toString().replace(':', '_').replace(' ', '_') + ".html", true));
                
                File fi = new File("local/" + organisms[i] + "/results/");
                if (!fi.exists()) {
                    fi.mkdir();
                }
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                String toopen = "local/" + organisms[i] + "/results/allCompImPerfect_" + now.toString().replace(':', '_').replace(' ', '_') + ".txt";
                statsfile = toopen;
                out = new PrintWriter(toopen);
                out.println("Results for project: " + organisms[i] + "\t Search Parameters --> Maximum Inter-repeat Region for Imperfect Compound SSRs(bp) : " + gap + " - minimum SSR length(bp): " + length);

            }
            
            
            int countpc=0;
            int bpcount=0,Aperc=0,Tperc=0,Gperc=0,Cperc=0;
            
             while(indexer.hasNext()){
                String files = filepro + indexer.getNextFileName();
                DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(files)));
                //PrintWriter out = new PrintWriter(files + "-minlentgh_" + length + "_ImPerfect.stats");
                boolean eof = false;
                while (!eof) {
                    try {
                        SSR = new ArrayList<String>();
                        repeats = new ArrayList<Integer>();
                        EndOfSsr = new ArrayList<Integer>();
                        start = new ArrayList<Integer>();

                        int len = in.readInt();
                        int line = in.readInt();
                        for (int k = 0; k < len; k++) {
                            //THIS
                            String temp = in.readUTF();
                            if(!temp.contains("N")){
                                SSR.add(temp);
                                EndOfSsr.add(in.readInt());
                                repeats.add(in.readInt());

                                start.add(EndOfSsr.get(k) - (SSR.get(k).length() * repeats.get(k)));
                            }else{
                                int junk = in.readInt();
                                junk = in.readInt();
                            }
                        }

                        List<String> SSRlen = new ArrayList<String>();
                        List<Integer> Endlen = new ArrayList<Integer>();
                        List<Integer> repslen = new ArrayList<Integer>();
                        List<Integer> startlen = new ArrayList<Integer>();

                        for (int k = 0; k < SSR.size(); k++) {
                            if (SSR.get(k).length() * repeats.get(k) >= length) {
                                SSRlen.add(SSR.get(k));
                                Endlen.add(EndOfSsr.get(k));
                                repslen.add(repeats.get(k));
                                startlen.add(start.get(k));
                            }
                        }


                        List<Integer> sortedstart = new ArrayList<Integer>();

                        List<Integer> sortedend = new ArrayList<Integer>();
                        for (int t = 0; t < startlen.size(); t++) {
                            sortedstart.add(startlen.get(t));
                            sortedend.add(Endlen.get(t));
                        }

                        Collections.sort(sortedstart);
                        Collections.sort(sortedend);


                        //List<String> tofile = new ArrayList<String>();
                        for (int k = 0; k < sortedstart.size() - 2; k++) {
                            found = false;
                            ssrs.clear();
                            ssrs = new ArrayList<String>();
                            if (sortedstart.get(k + 1) - sortedend.get(k) <= gap && sortedstart.get(k + 1) - sortedend.get(k) >= 0) {
                                seekstart = sortedstart.get(k);
                                while (k < sortedstart.size() - 1 && sortedstart.get(k + 1) - sortedend.get(k) <= gap && sortedstart.get(k + 1) - sortedend.get(k) >= 0) {
                                    for (int c = 0; c < startlen.size(); c++) {
                                        if (sortedstart.get(k) == startlen.get(c)) {
                                            ssrs.add(SSRlen.get(c));
                                        }
                                        if (sortedstart.get(k + 1) == startlen.get(c)) {
                                            ssrs.add(SSRlen.get(c));
                                            seekend = Endlen.get(c);
                                            found = true;
                                        }
                                    }
                                    k++;
                                }
                                k--;
                            }
                            boolean check = checkallsame(ssrs);
                            boolean check2 = checkalldiff(ssrs);
                            if (found && !check && !check2) {
                                BufferedReader stdin = null;
                                if (flag) {
                                    String[] temp = files.split("/");
                                    boolean type = CheckForKaryotype(organisms[i]);
                                    String newdir = "";
                                    if (type) {
                                        newdir = temp[0] + "/" + temp[1] + "/chrom-" + temp[3].substring(0, temp[3].lastIndexOf('.')) + "-slices.txt";
                                    } else {
                                        newdir = temp[0] + "/" + temp[1] + "/slice-" + temp[3].substring(0, temp[3].lastIndexOf('.')) + ".txt";
                                    }

                                    stdin = new BufferedReader(new FileReader(newdir));


                                } else {
                                    String[] temp = files.split("data/");
                                    String newdir = temp[0] + "/" + temp[1].substring(0, temp[1].lastIndexOf('.')) + ".txt";
                                    stdin = new BufferedReader(new FileReader(newdir));
                                }
                                buffer = "";
                                for (int c = 0; c < line; c++) {
                                    buffer = stdin.readLine();
                                }
                                //System.out.println(buffer.length() + "\t" + seekstart + "\t" + seekend);
                                int real_end = (line - 1) * 20000 + seekend;
                                int real_start = (line - 1) * 20000 + seekstart;
                                //tofile.add("SSR: "+buffer.substring(seekstart, seekend) + "start-end: "+ real_start + "-" +real_end );
                                countpc++;
                                if(seekstart<0)
                                    seekstart++;
                                String tmp=buffer.substring(seekstart, seekend);
                                bpcount+=tmp.length();
                                if (tmp.contains("A")) {
                                    Aperc += StringUtils.countMatches(tmp, "A");
                                }
                                if (tmp.contains("T")) {
                                    Tperc += StringUtils.countMatches(tmp, "T");
                                }
                                if (tmp.contains("G")) {
                                    Gperc += StringUtils.countMatches(tmp, "G");
                                }
                                if (tmp.contains("C")) {
                                    Cperc += StringUtils.countMatches(tmp, "C");
                                }
                                out.println("SSR: " + tmp + " start-end: " + real_start + "-" + real_end + " Path(../data/chromosome): " + files.substring(0, files.lastIndexOf('.')));
                                stdin.close();

                            }

                        }



                    } catch (EOFException e) {
                        eof = true;
                    }
                }
                in.close();
            }
            out.close();
            Runtime.getRuntime().exec("notepad " + statsfile);
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection con = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
            } catch (SQLException ex) {
                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            Statement st = null;
            try {
                st = con.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(StatsSelection.class.getName()).log(Level.SEVERE, null, ex);
            }
            st.executeUpdate("use lobid");
            
            int seqcount=0;
            if (filetype.contains("organisms")) {
                ResultSet rs = st.executeQuery("SELECT end FROM slices INNER JOIN organism WHERE slices.org_id=organism.org_id AND organism.name='" + organisms[i] + "'");
                while (rs.next()) {
                    seqcount += Long.parseLong(rs.getString(1));
                }
            } else if (filetype.contains("local")) {
                BufferedReader in = new BufferedReader(new FileReader("local/" + organisms[i] + "/index.txt"));
                int count = countlines("local/" + organisms[i] + "/index.txt");
                for (int c = 0; c < count; c++) {
                    String temp = in.readLine();
                    BufferedReader tmp = new BufferedReader(new FileReader("local/" + organisms[i] + "/" + temp + ".txt"));

                    boolean eof=false;
                    while (!eof) {
                        String s = tmp.readLine();
                        if(s!=null){
                            seqcount+=s.length();
                        }
                        else{
                            eof = true;
                        }
                    }
                    tmp.close();
                }
            }
            
                                            
            DecimalFormat round = new DecimalFormat("#.###");

            html.println("<html><h1>******* Compound Imperfect SSRs *******</h1>");
            html.println("<h4>Results for project: " + organisms[i] + "</h4><h4>Search Parameters --> Maximum Inter-repeat Region for Imperfect Compound SSRs (bp) : " + gap + "</h4><h4>minimum SSR length (bp): " + length+"</h4>"); 
            html.println("<table border=\"1\"><tr><td> </td><td><b>count</b></td><td><b>bp</b></td><td><b>A%</b></td><td><b>T%</b></td><td><b>C%</b></td><td><b>G%</b></td><td><b>Relative Frequency</b></td><td><b>Abundance</b></td><td><b>Relative Abundance</b></td></tr>");
            html.println("<tr><td><b>Compound Imperf.</b></td><td>"+countpc+"</td><td>"+bpcount+"</td><td>"+round.format((float)Aperc * 100 / bpcount)+"</td><td>"+round.format((float)Tperc * 100 / bpcount)+"</td><td>"+round.format((float)Cperc * 100 / bpcount)+"</td><td>"+round.format((float)Gperc * 100 / bpcount)+"</td><td>"+round.format((float)countpc / countpc)+"</td><td>"+round.format((float) bpcount / seqcount)+"</td><td>"+round.format((float) bpcount / bpcount)+"</td></tr>");
            html.println("<tr><td><b>TOTAL</b></td><td>"+countpc+"</td><td>"+bpcount+"</td><td>"+round.format((float)Aperc * 100 / bpcount)+"</td><td>"+round.format((float)Tperc * 100 / bpcount)+"</td><td>"+round.format((float)Cperc * 100 / bpcount)+"</td><td>"+round.format((float)Gperc * 100 / bpcount)+"</td><td>"+round.format((float)countpc / countpc)+"</td><td>"+round.format((float) bpcount / seqcount)+"</td><td>"+round.format((float) bpcount / bpcount)+"</td></tr></table></html>");
            html.close();
            
            
            
            stats.println("******* Compound Imperfect SSRs *******");
            stats.println("Results for project: " + organisms[i] + "\nSearch Parameters --> Maximum Inter-repeat Region for Imperfect Compound SSRs(bp) : " + gap + "\nminimum SSR length(bp): " + length);

            stats.println(" ____________________________________________________________________________________________________________________ ");
            stats.println("|               |       |            |       |       |       |       |   Relative    |               |   Relative    |");
            stats.println("|               | count |     bp     |   A%  |   T%  |   C%  |   G%  |   Frequency   |   Abundance   |   Abundance   |");
            stats.println("|===============|=======|============|=======|=======|=======|=======|===============|===============|===============|");
            stats.printf("|Compound Imper.|" + cell(Integer.toString(countpc), 7) + "|" + cell(Integer.toString(bpcount), 12) + "|%s|%s|%s|%s|" + cell((float) countpc / countpc, 15) + "|" + cell((float) bpcount / seqcount, 15) + "|" + cell((float) bpcount / bpcount, 15) + "|\n", cell((float) Aperc * 100 / bpcount, 7), cell((float) Tperc * 100 / bpcount, 7), cell((float) Cperc * 100 / bpcount, 7), cell((float) Gperc * 100 / bpcount, 7));
            stats.println("|---------------|-------|------------|-------|-------|-------|-------|---------------|---------------|---------------|");
                                    
            lt.writeLong(seqcount);lt.writeInt(countpc);lt.writeInt(bpcount);lt.writeInt(Aperc);lt.writeInt(Tperc);lt.writeInt(Gperc);lt.writeInt(Cperc);
            
            
            stats.println("|TOTAL          |" + cell(Integer.toString(countpc), 7) + "|" + cell(Long.toString(bpcount), 12) + "|" + cell((float)Aperc * 100 / bpcount, 7) + "|" + cell((float)Tperc * 100 / bpcount, 7) + "|" + cell((float)Cperc * 100 / bpcount, 7) + "|" + cell((float)Gperc * 100 / bpcount, 7) + "|" + cell((float) countpc / countpc, 15) + "|" + cell((float) bpcount / seqcount, 15) + "|" + cell((float) bpcount/bpcount, 15) + "|");
            stats.println("|_______________|_______|____________|_______|_______|_______|_______|_______________|_______________|_______________|");
            stats.println("Genome length (bp): " + seqcount);
            stats.println("Relative Frequency: Count of each motif type / total SSR count");
            stats.println("Abundance: bp of each motif type / total sequence bp");
            stats.println("Relative Abundance: bp of each motif type / total microsatellites bp");
            stats.println();
            stats.println();
            stats.close();
            lt.close();



        }

    }
    
    

    public int getOrganismStatus(String organism) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
        
        if (CheckForKaryotype(organism)) {

                ChromsParser parse = new ChromsParser(organism);
                chromosomes = parse.getChroms();
                chromosomelist = new ArrayList<String>();

                try {
                    int j = Integer.parseInt(chromosomes[0]);
                    for (int i = 0; i < chromosomes.length; i++) {
                        if (i == 0 && j > 1) {
                            int c = 1;
                            while (c <= j) {
                                chromosomelist.add(c+"");
                                c++;
                            }
                        } else {
                            chromosomelist.add(chromosomes[i]);
                        }

                        //System.out.println(chromosomes[i]);
                    }
                } catch (NumberFormatException ex) {
                    for (int i = 0; i < chromosomes.length; i++) {
                        chromosomelist.add(chromosomes[i]);
                    }
                }
                return -1;
        } else {
                ChromsParser parse = new ChromsParser(organism);
                chromosomes = parse.getChroms();
                int j = Integer.parseInt(chromosomes[0]);
                return j;
        }
    }

    public int countlines(String filename) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] b = new byte[1024];
            int count = 0;
            int readChars = 0;
            while ((readChars = in.read(b)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (b[i] == '\n') {
                        ++count;
                    }
                }
            }
            return count;
        } finally {
            in.close();
        }
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

    public boolean checkallsame(List<String> ssr) {
        int count = 1;
        if (ssr.size() >= 2) {
            for (int i = 1; i < ssr.size(); i++) {
                if (circle(ssr.get(0)).contains(ssr.get(i))) {
                    count++;
                }
            }
            if (count == ssr.size()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<String> circle(String s) {
        List<String> circ = new ArrayList<String>();

        /*for(int i=0;i<s.length();i++){
        String seq="";
        
        for(int j=0;j<s.length();j++){
        
        seq+=s.charAt(j);
        }
        }*/
        /*
        if (s.length() == 1) {
        circ.add(s);
        } else if (s.length() == 2) {
        circ.add("" + s.charAt(0) + s.charAt(1));
        circ.add("" + s.charAt(1) + s.charAt(0));
        } else if (s.length() == 3) {
        circ.add("" + s.charAt(0) + s.charAt(1) + s.charAt(2));
        circ.add("" + s.charAt(1) + s.charAt(2) + s.charAt(0));
        circ.add("" + s.charAt(2) + s.charAt(0) + s.charAt(1));
        } else if (s.length() == 4) {
        circ.add("" + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3));
        circ.add("" + s.charAt(1) + s.charAt(2) + s.charAt(3) + s.charAt(0));
        circ.add("" + s.charAt(2) + s.charAt(3) + s.charAt(0) + s.charAt(1));
        circ.add("" + s.charAt(3) + s.charAt(0) + s.charAt(1) + s.charAt(2));
        } else if (s.length() == 5) {
        circ.add("" + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3) + s.charAt(4));
        circ.add("" + s.charAt(1) + s.charAt(2) + s.charAt(3) + s.charAt(4) + s.charAt(0));
        circ.add("" + s.charAt(2) + s.charAt(3) + s.charAt(4) + s.charAt(0) + s.charAt(1));
        circ.add("" + s.charAt(3) + s.charAt(4) + s.charAt(0) + s.charAt(1) + s.charAt(2));
        circ.add("" + s.charAt(4) + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3));
        } else if (s.length() == 6) {
        circ.add("" + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3) + s.charAt(4) + s.charAt(5));
        circ.add("" + s.charAt(1) + s.charAt(2) + s.charAt(3) + s.charAt(4) + s.charAt(5) + s.charAt(0));
        circ.add("" + s.charAt(2) + s.charAt(3) + s.charAt(4) + s.charAt(5) + s.charAt(0) + s.charAt(1));
        circ.add("" + s.charAt(3) + s.charAt(4) + s.charAt(5) + s.charAt(0) + s.charAt(1) + s.charAt(2));
        circ.add("" + s.charAt(4) + s.charAt(5) + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3));
        circ.add("" + s.charAt(5) + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3) + s.charAt(4));
        }*/
        for (int i = 0; i < s.length(); i++) {
            String seq = "";
            boolean fl = false;
            for (int j = i; j < s.length(); j++) {
                if (fl && j == i) {
                    break;
                }
                seq += s.charAt(j);
                if (j == s.length() - 1 && !fl) {
                    j = -1;
                    fl = true;
                }
            }
            circ.add(seq);
        }

        return circ;
    }

    public int countchecked() {
        int count = 0;
        if (mono.isSelected()) {
            count++;
        }
        if (di.isSelected()) {
            count++;
        }
        if (tri.isSelected()) {
            count++;
        }
        if (tetra.isSelected()) {
            count++;
        }
        if (penta.isSelected()) {
            count++;
        }
        if (hexa.isSelected()) {
            count++;
        }

        return count;
    }

    public String cell(String ele, int len) {
        String str = "";
        if (ele.length() == len) {
            return ele;
        } else if (ele.length() < len) {
            int rest = len - ele.length();
            for (int i = 0; i < rest; i++) {
                str += " ";
            }
            str += ele;
            return str;
        } else {
            for (int i = 0; i < len; i++) {
                str += "#";
            }

            return str;

        }
    }

    public String cell(float fl, int len) {

        String str = "";
        if (fl != 0) {
            DecimalFormat round = new DecimalFormat("#.###");

            String ele = round.format(fl);
            if (ele.length() == len) {
                return ele;
            } else if (ele.length() < len) {
                int rest = len - ele.length();
                for (int i = 0; i < rest; i++) {
                    str += " ";
                }
                str += ele;
                return str;
            } else {
                for (int i = 0; i < len; i++) {
                    str += "#";
                }

                return str;

            }
        }else{
            for(int i=0;i<len-1;i++){
                str+=" ";
            }
            str+="0";
            return str;
        }
    }

    public boolean checkalldiff(List<String> ssr) {
        for (int i = 0; i < ssr.size() - 1; i++) {
            List<String> t = circle(ssr.get(i));
            if (t.contains(ssr.get(i + 1))) {
                return false;
            }
        }
        return true;
    }
    
    public int checkStandardize(){
        if(no_st.isSelected())
            return 0;
        else if (part_st.isSelected())
            return 1;
        else if (full_st.isSelected())
            return 2;
        return 0;
    }
}
