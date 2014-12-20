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
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

public class input_frame extends JFrame {

    private MyFileChooser choice;
    private JTabbedPane tab;
    private JLabel label; 
    private JButton next;
    private JButton select;
    private JButton unselect;
    private Stack organism_stack;
    private Stack temp_stack;
    private Stack timestamp_stack;
    private JCheckBox species1;
    private JOptionPane msg;
    private BufferedReader stdin = null;
    private List<JCheckBox> species;
    private List<String> exts;
    private String organism;
    
    private DataOutputStream stdout = null;
    private Sequence[] ssrs = null;
    private JLabel space;
    private JPanel panel2;
    private JPanel panel;
    private JPanel panel3;
    private JPanel panel_local;
    
    private File localfile;
    private JButton choosebutton;
    private JButton scan;
    private JButton next2;
    private JButton open;
    private JLabel filename;
    private JTextField userfilename;
    
    
    public input_frame() throws ClassNotFoundException, SQLException {
        setTitle("MiGA");
        
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        Image im = Toolkit.getDefaultToolkit().getImage("ssr.png");
        this.setIconImage(im);
        
         try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
	        catch (Exception e) {}

        
        msg = new JOptionPane();
        
        organism_stack = new Stack();
        temp_stack = new Stack();
        timestamp_stack = new Stack();
        species = new ArrayList<JCheckBox>();
        /* stack opou th kratame ts organismous p 
         * epilexthikan sta checkboxes
         */
                

        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
        Statement st = conn.createStatement();
        st.executeUpdate("use LoBiD");//xrisimopoioume tin vasi mas gia anaktisi twn onomatwn twn organismwn
        ResultSet rs = st.executeQuery("SELECT name FROM organism");

        while(rs.next()){
            species1 = new JCheckBox(rs.getString(1));
            species.add(species1);
        } //end while
        conn.close();
        

        select = new JButton("Check all");
        select.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SelectAll(true);
            }
        });
        
        unselect = new JButton("Uncheck all");
        unselect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SelectAll(false);
            }
        });
        
        
        
        next = new JButton("Check last updated");
        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(checkSelected()){
                    next.setEnabled(false);
                    for(int i=0;i<species.size();i++){
                        if(species.get(i).isSelected()){
                            organism_stack.push(species.get(i).getText());
                        }
                    }
                    temp_stack=(Stack) organism_stack.clone();
                    if(temp_stack.isEmpty() == false){
                        while (temp_stack.isEmpty() == false) {
                            organism = temp_stack.pop().toString();

                            try {
                                timestamp_stack.push(CheckforTimestamp(organism));
                            } catch (SQLException ex) {
                                Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        Stack reverse_species = new Stack();
                        while(organism_stack.isEmpty()==false){
                            reverse_species.push(organism_stack.pop());
                        }
                        CheckedOrganisms frame = new CheckedOrganisms(reverse_species, timestamp_stack);
                        dispose();
                    }
                }
            }
        });
        
        /*
         * neo panel se allo tab gia topika arxeia
         */
        exts = new ArrayList<String>();
        exts.add("fasta");
        exts.add("fa");
        
        
        choice = new MyFileChooser();
        
        FileFilter filter = new FileFilter(){
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;

                } else if (f.isFile()) {
                    Iterator it = exts.iterator();
                    while (it.hasNext()) {
                        if (f.getName().endsWith((String) it.next()))
                            return true;
                    }
                }

                // A file that didn't match
                return false;
            }

            @Override
            public String getDescription() {
                return "FASTA FILES: *.fasta, *.fa";
            }
            
        };
        
        choice.setDialogType(JFileChooser.OPEN_DIALOG);
        final JDialog dialog = choice.createDialog(null);
        
        choice.setFileFilter(filter);
        choice.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                localfile = choice.getSelectedFile();
                dialog.dispose();
                
            }
            
        });
        
        open = new JButton("Open Previous Project");
        File idx = new File("local/projects.idx");
        if(!idx.exists()){
            open.setEnabled(false);
        }
        
        open.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                List<Object> possibilities = new ArrayList<Object>();
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader("local/projects.idx"));
                    boolean eof=false;
                    String proj = "";
                    while(!eof){
                        try {
                            proj = in.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if(proj!=null){
                            possibilities.add(proj);
                        }else{
                            eof=true;
                        }
                    }
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String s = (String)JOptionPane.showInputDialog(panel,"Choose a project","Open Project",
                        JOptionPane.PLAIN_MESSAGE,null,
                        possibilities.toArray(),
                        "Select");
                    if(s!=null){
                        String[] org = new String[1];
                        org[0]=s;

                        StatsSelection frame = new StatsSelection(org,false);
                        /*
                         * se auto to frame o xrhsths tha dinei ts parametrous 
                         * sxetika me ts SSRs kai ta statistika pou thelei na tou emfanistoun.
                         */
                        dispose();
                    }
                
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        
        
        
        scan = new JButton("Start scanning");
        scan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if(localfile!=null && !userfilename.getText().isEmpty()){
                    
                     
                    String[] org= new String[1];
                    org[0]=userfilename.getText();
                    //indexing for open
                    PrintWriter idx=null;
                    
                    try {
                        idx = new PrintWriter(new FileWriter("local/projects.idx",true));
                    } catch (IOException ex) {
                        Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    idx.println(org[0]);
                    idx.close();
                    
                    msg.showMessageDialog(panel_local,"File scanning starts!\nClick OK and wait to finish","Information",JOptionPane.INFORMATION_MESSAGE);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        FastaToSlicedConverter fts = new FastaToSlicedConverter(localfile,org[0]);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        filesearch(org[0]);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    StatsSelection frame = new StatsSelection(org,false);
                    /*
                     * se auto to frame o xrhsths tha dinei ts parametrous 
                     * sxetika me ts SSRs kai ta statistika pou thelei na tou emfanistoun.
                     */
                    dispose();
                }else{
                    if(localfile==null)
                        msg.showMessageDialog(panel_local,"Choose a file","Error",JOptionPane.ERROR_MESSAGE);
                    if(userfilename.getText().isEmpty())
                        msg.showMessageDialog(panel_local,"Give a name \n(e.g. organism and/or chromosome)","Error",JOptionPane.ERROR_MESSAGE);       
                }
            }
        });
        scan.setVisible(true);
        
        
        label = new JLabel("Choose your FASTA file");
        userfilename = new JTextField(); 
        userfilename.setColumns(20);
        filename = new JLabel("Give project name");
        
        final JLabel text = new JLabel("<html><b>Scanning done</b></html>");
        text.setVisible(false);
        choosebutton = new JButton("Browse");
        choosebutton.addActionListener(new ActionListener(){
            
             public void actionPerformed(ActionEvent e) {
                 dialog.setVisible(true);
                 if(localfile!=null){
                     Globals.openframe=true;
                     //text.setVisible(true);
                 }
            }
        
        });
        
        
        
        
                        
        
        //δημιουργια πλεγματος και στοιχιση
        tab = new JTabbedPane();
        tab.setSize(790, 590);
        
        panel = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Select Species");
        panel.setBorder(title);
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();
        
        int i=0,j=0;
        int element = 0;
        
        int columns = species.size()/16;
        int rest = species.size()%16;
        
        for(j=0;j<columns;j++){
            for(i=0;i<16;i++){

                a.fill = GridBagConstraints.HORIZONTAL;
                a.weightx = 0.5;
                a.weighty = 0.5;
                a.gridx = j;
                a.gridy = -2-i;
                panel.add(species.get(element), a);
                element++;
            }
        }
        for(int temp=0;temp<rest;temp++){
            a.fill = GridBagConstraints.HORIZONTAL;
            a.weightx = 0.5;
            a.weighty = 0.5;
            a.gridx = j;
            a.gridy = -2-temp;
            panel.add(species.get(element), a);
            element++;
        }
        
        
        panel2 = new JPanel();
      
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();

        panel2.add(next, b);
        
        space = new JLabel("                   ");
        
        panel2.add(space, b);
        panel2.add(select, b);
        panel2.add(unselect, b);
        
        panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        panel3.add(panel, c);
        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = -1;
        panel3.add(panel2, c);
        
        tab.add("Ensembl or local",panel3);
        
        panel_local = new JPanel();
        panel_local.setLayout(new GridBagLayout());
        GridBagConstraints l = new GridBagConstraints();
        
        l.fill = GridBagConstraints.CENTER;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 0;
        l.gridy = 0;
        panel_local.add(label,l);
        
        
        l.fill = GridBagConstraints.CENTER;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 1;
        l.gridy = 0;
        panel_local.add(choosebutton,l);
        /*
        l.fill = GridBagConstraints.HORIZONTAL;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 1;
        l.gridy = 1;
        panel_local.add(new JLabel("  "),l);*/
        
        l.fill = GridBagConstraints.EAST;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 0;
        l.gridy = -1;
        panel_local.add(filename,l);
        
        l.fill = GridBagConstraints.WEST;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 1;
        l.gridy = -1;
        panel_local.add(userfilename,l);
        
        
        l.fill = GridBagConstraints.CENTER;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 0;
        l.gridy = -3;
        panel_local.add(scan,l);
        
        l.fill = GridBagConstraints.CENTER;
        l.weightx = 0.5;
        l.weighty = 0.5;
        l.gridx = 1;
        l.gridy = -3;
        panel_local.add(open,l);
        
        
        
        tab.add("File from User", panel_local);
        
        add(tab);
        setVisible(true);
    }
    
    class MyFileChooser extends JFileChooser {
        
        public JDialog createDialog(Component parent) throws HeadlessException {
            return super.createDialog(parent);
        }
    }
   
    
    private boolean checkSelected(){
        boolean found=false;
        for (int i=0;i<species.size();i++){
            if(species.get(i).isSelected()){
                found=true;
                break;
            }
        }
        return found;
    }
    
   /* public static class processThread implements Runnable{
        public void run(){
            for (int i=0; i<=100; i++){ //Progressively increment variable i
                bar.setValue(i); //Set value
                bar.repaint(); //Refresh graphics
                
                try {
                    p = Runtime.getRuntime().exec("cmd /c cd organisms && perl chromosome_downloader "+organism+" 1");
                        try {
                            p.waitFor();
                            
                            done.setEnabled(true);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                } catch (IOException ex) {
                    Logger.getLogger(input_frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }*/
    private void SelectAll(boolean set){
        for(int i=0;i<species.size();i++){
            species.get(i).setSelected(set);
        }
    }

    public String CheckforTimestamp(String org) throws SQLException, ClassNotFoundException{
        
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
        Statement st = con.createStatement();
        st.executeUpdate("use LoBiD");
        
        
        /* 
         * Elegxos an yparxei o organismos kataxwrhmenos
         * 
         */
        ResultSet check = st.executeQuery("SELECT timestamp FROM organism WHERE name = '"+org+"'");
        if(check.next())
            return check.getString(1);
        else
            return "not found";
        
         
    }
    
    public void SavetoDB(String org) throws FileNotFoundException, ClassNotFoundException, SQLException, IOException {
        
        String accession_number = null;
        String length = null;
        String sequence = null;
        int pos=0;
        
        ssrs = new Sequence[10];
        
        stdin = new BufferedReader(new FileReader(org.concat(".txt")));
        stdout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("ssr.txt")));
        boolean eof=false;
        
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
        Statement st = con.createStatement();
        st.executeUpdate("use LoBiD");
        
        
        /* 
         * Elegxos an yparxei o organismos kataxwrhmenos
         * 
         */
        ResultSet check = st.executeQuery("SELECT * FROM organism WHERE name = '"+org+"'");
        
        if(!check.next()){
            st.executeUpdate("INSERT INTO organism (name) VALUES('" + org + "')");
        }        
        stdin.readLine(); // to arxeio ksekinaei me mia kenh grammh
        while(!eof){
            
            try {
                accession_number = stdin.readLine().substring(18);
                length = stdin.readLine().substring(8);
                sequence = stdin.readLine().substring(10); //se sxolio gia na mhn trwei xrono sthn apothikeush
                //stdin.readLine();
                
                if(accession_number == null){
                    eof=true;
                }
                else{
                    

                    ResultSet res_org = st.executeQuery("SELECT org_id FROM organism WHERE name = '"+org+"'");
                    res_org.absolute(1);
                    String org_id = res_org.getString("org_id");
                    //System.out.println(org_id);
                    st.executeUpdate("INSERT INTO sequence_info (acc_num, length) VALUES('" + accession_number + "','" + length + "')");
                    ResultSet res_s = st.executeQuery("SELECT s_id FROM sequence_info WHERE acc_num = '"+ accession_number +"'");
                    res_s.absolute(1);
                    String field_id = res_s.getString("s_id");


                    st.executeUpdate("INSERT INTO linker (org_id,field_id) VALUES('"+ org_id +"','"+ field_id +"')");
                    
                    if (Integer.parseInt(length)<5000){
                    
                        Sequence seq = new Sequence(sequence);
                        /*
                         * o arxikos pinakas exeis megethos 10
                         * an vrethei to pos%9==0 eimaste st teleutaio stoixeio
                         * ara kanoume realloc ton pinaka mas kata 10
                         */
                        if(pos%9==1){
                            ssrs[pos] = seq;
                            Sequence[] temp = new Sequence[pos+9];
                            System.arraycopy(ssrs, 0, temp, 0, ssrs.length);
                            ssrs = new Sequence[temp.length];
                            System.arraycopy(temp, 0, ssrs, 0, temp.length);
                        }
                        else{
                            ssrs[pos] = seq;

                        }


                        stdout.writeUTF(org);
                        stdout.writeUTF(ssrs[pos].toString());
                        //List temp_ssr = ssrs[pos].getSSR();
                        for(int i=0;i<ssrs[pos].getSSR().size();i++){
                            //out = out + "SSR: "+ SSR.get(i) + "\trepeats: " + repeats.get(i) + "\tending position: " + EndOfSsr.get(i) + "\n";
                            st.executeUpdate("INSERT INTO ssr (sequence, repeats, ending_position, sequence_id) VALUES('" 
                                    + ssrs[pos].getSSR().get(i) + "','" 
                                    + Integer.toString(ssrs[pos].CountRepeats().get(i)) + "','" 
                                    + Integer.toString(ssrs[pos].getEndOfSsr().get(i)) + "','" 
                                    + field_id
                                    + "')");
                        }
                    }
                    
                    

                    //System.out.println(accession_number);
                    //System.out.println(length);
                    //System.out.println(sequence);

               }
            }catch (NullPointerException ex) {
                eof = true;
            }
        }
        
        stdout.close();
        stdin.close(); //kleisimo arxeiou
    }
    
    public void filesearch(String org) throws FileNotFoundException, IOException{
        boolean eof=false;
        BufferedReader in = new BufferedReader(new FileReader("local/" +org+"/index.txt"));
        
        int lines = countlines("local/" +org+"/index.txt");
        String temp = in.readLine();
        for(int i=0;i<lines;i++){
            BufferedReader stdin = new BufferedReader(new FileReader("local/" + org + "/"+temp+".txt"));
            int line =0;
            eof=false;
            while(!eof){
                String s=stdin.readLine();
                line++;
                if(s==null){
                    eof=true;
                    break;
                }else{
                    Sequence seq = new Sequence(s);
                    seq.toFile(org, null, line,false,temp); // 12 gia n vgalei auta me mhkos megalutero tou 12
                }
            } 
            stdin.close();
            temp=in.readLine();
        }
        in.close();

        
        
    }
    
    public int countlines(String filename) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] b = new byte[1024];
            int count = 0;
            int readChars = 0;
            while ((readChars = in.read(b)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (b[i] == '\n')
                        ++count;
                }
            }
            return count;
        } finally {
            in.close();
        }
    }



    
    

}