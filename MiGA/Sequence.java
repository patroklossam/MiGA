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

/**
 * 
 * h class auth ftiaxnei ena antikeimeno afou ths dwsoume mia akolouthia
 * kai krataei olous ts SSR se list
 * tn arithmo epanalhpsewn se list
 * kai thn thesh termatismou tous se list
 * 
 * given a sequence, this class creates an object
 * and keeps alla the SSRs in a list
 * the number of repeats  in a list
 * the ending position in a list
 * 
 */
package MiGA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Sequence {
    private String sequence;
    private List<String> SSR;
    private int counter, current;
    private List<Integer> repeats;
    private List<Integer> EndOfSsr;//gia na ksana psaksoume sto epomeno kommati mhpws yparxei k allos
    private boolean eos; // end of sequence
    
    public Sequence(){
        sequence = new String();
        current = 0;
        eos = false;
        SSR = new ArrayList<String>();
        repeats = new ArrayList<Integer>();
        EndOfSsr = new ArrayList<Integer>(); 
    }
    
    public Sequence(String seq){
        sequence = seq +"ENDOF55kSEQ";
        current = 0;
        eos = false;
        SSR = new ArrayList<String>();
        repeats = new ArrayList<Integer>();
        EndOfSsr = new ArrayList<Integer>(); 
        SearchSSRs(sequence);
        normalizeSSREndingPositions();
    }
    
    public void setSequence(String seq){
        sequence = seq;
        SSR = new ArrayList<String>();
        repeats = new ArrayList<Integer>();
        EndOfSsr = new ArrayList<Integer>();
        SearchSSRs(sequence);
        normalizeSSREndingPositions();
    }
    public String getSequence(){
        return sequence;
    }
    public List<String> getSSR(){
        return SSR;
    }
    
    public List<Integer> getEndOfSsr(){
        return EndOfSsr;
    }
    
    public List<Integer> CountRepeats(){
        return repeats;
    }
    
    private void SearchSSRs(String seq){
        CheckForMono(seq);
        for(int i=0;i<5;i++){
            eos=false;
            CheckFor2to6CharSeq(seq,i);
        }
    }
    
    private void CheckForMono(String seq){
        
        int reps;
        int i=0;
        int inner_count=0;
        
        for(i=0;i<seq.length()-2;i++){
            
            //allagh stn tropo apaloifhs tou N
            if(eos==false && seq.charAt(i)!= 'N'){
                current++;
                if(seq.substring(i, i+1).equalsIgnoreCase(seq.substring(i+1, i+2))){
                    reps=2;
                    inner_count++;
                    counter++;
                    SSR.add(counter-1,seq.substring(i, i+1)) ;
                    
                    boolean out=false;
                    while(i<seq.length()-2 && !out){
                        i++;
                        if(seq.substring(i, i+1).equalsIgnoreCase(seq.substring(i+1, i+2))){
                            reps++;
                        }
                        else{
                            repeats.add(counter-1, reps);
                            EndOfSsr.add(counter-1, i);
                            out = true;
                        }
                    }
                }
            }
        }
    }
    
    private void CheckFor2to6CharSeq(String seq,int extra){
        int reps;
        int i=0;
        int inner_count=0;
        
        /*
         * gia kathe stoixeio ths akolouthias checkaroume an h duada p ksekinaei
         * apo auto einai idia me to epomeno
         * 
         * an einai idio elegxei kai thn epomenh duada mexri n vrei diaforetikh
         * 
         * otan vrei diaforetikh  trexei anadromika thn methodo me substring apo
         * shmeio pou stamathse kai meta. otan ftasei sto telos ths arxikhs 
         * sequence thetei to flag eos = true
         * kai vgainei apo thn sunarthsh
         * 
         */
        
        for(i=0;i<seq.length()-4-(2*extra);i++){
            if(eos == false && seq.charAt(i)!='N'){
                current++;
                if(seq.substring(i, i+2+extra).equalsIgnoreCase(seq.substring(i+2+extra, i+4+2*extra))){
                    inner_count++;
                    counter += inner_count;
                    SSR.add(counter-1,seq.substring(i, i+2+extra)) ;

                    reps=2;

                    int k=2+extra;// kathe fora vlepoume ana 2 
                    int multi=2;
                    while((k<seq.length()-2-i-extra)&& eos == false){
                        if(SSR.get(counter-1).equalsIgnoreCase(seq.substring(i+k*multi, i+k*(multi+1)))){
                            reps ++;
                            current++;
                            multi++;
                            
                            //k+=(2+extra);
                        }
                        else{
                            repeats.add(counter-1, reps);
                            
                            EndOfSsr.add(counter-1, i+repeats.get(counter-1)*(2+extra)); 
                            
                            if(EndOfSsr.get(counter-1)<seq.length()-5-(extra*2)){   // to teleutaio kommati
                                if(counter==1){
                                    CheckFor2to6CharSeq(seq.substring(EndOfSsr.get(counter-1)),extra);
                                }
                                else{
                                    CheckFor2to6CharSeq(seq.substring(EndOfSsr.get(counter-1)),extra);
                                }
                                
                            }
                            else{
                                /*
                                 * edw checkarei thn telikh 5ada an exei kati se SSR
                                 */
                                String finalSeq = seq.substring(EndOfSsr.get(counter-1));
                                if(EndOfSsr.get(counter-1)==seq.length()-4-2*extra){
                                    for (int j=0;j<finalSeq.length();j++){
                                        if (eos == false){
                                            if(finalSeq.substring(0, 2+extra).equalsIgnoreCase(finalSeq.substring(2+extra,4+extra))){
                                                counter++;
                                                SSR.add(counter-1,finalSeq.substring(0, 2+extra)) ;
                                                repeats.add(counter-1, 2);

                                                EndOfSsr.add(counter-1, 4); // to teleutaio kommati
                                                eos = true;
                                                break;

                                            }
                                            else 
                                                if(finalSeq.substring(1, 3).equalsIgnoreCase(finalSeq.substring(3))){
                                                    counter++;
                                                    SSR.add(counter-1,finalSeq.substring(1, 3)) ;
                                                    repeats.add(counter-1, 2);

                                                    EndOfSsr.add(counter-1, 5); // to teleutaio kommati
                                                    eos = true;
                                                    break;
                                                }
                                        }
                                    }
                                }
                                eos = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        eos = true;   
    }
    
    public void normalizeSSREndingPositions(){
        
        /*
         * after the ssr searching, the ending positions are only
         * distances between the SSRendings in the sequence
         * so we add the distances according to the SSRs length
         * to find the exact position in the given sequence
         */
        
        boolean found2 = false, found3 = false, found4 = false, found5 = false, found6 = false;
        int i=0;
        for(i=0;i<SSR.size();i++){
            if(SSR.get(i).length()==2){
                if(found2==true){
                    EndOfSsr.set(i, EndOfSsr.get(i)+EndOfSsr.get(i-1));
                }
                if(found2==false){
                    found2=true;
                }
            }
            if(SSR.get(i).length()==3){
                if(found3==true){
                    EndOfSsr.set(i, EndOfSsr.get(i)+EndOfSsr.get(i-1));
                }
                if(found3==false){
                    found3=true;
                }
            }
            if(SSR.get(i).length()==4){
                if(found4==true){
                    EndOfSsr.set(i, EndOfSsr.get(i)+EndOfSsr.get(i-1));
                    
                }
                if(found4==false){
                    found4=true;
                }
            }
            if(SSR.get(i).length()==5){
                if(found5==true){
                    EndOfSsr.set(i, EndOfSsr.get(i)+EndOfSsr.get(i-1));
                }
                if(found5==false){
                    found5=true;
                }
            }
            if(SSR.get(i).length()==6){
                if(found6==true){
                    EndOfSsr.set(i, EndOfSsr.get(i)+EndOfSsr.get(i-1));
                }
                if(found6==false){
                    found6=true;
                }
            }
        }
        
        
        
        //to vgazoume se sxolio mias kai dn to upologizoume kan stn arxh
        /* 
        for(i=0;i<SSR.size();i++){
            if(SSR.get(i).contains("N")){
                SSR.remove(i);
                repeats.remove(i);
                EndOfSsr.remove(i);
                i--;
            }
        }
        */
        
        // a found hexa may contain smt like this (ATANAT)x2
        for(i=0;i<SSR.size();i++){
            if(SSR.get(i).contains("N")){
                SSR.remove(i);
                repeats.remove(i);
                EndOfSsr.remove(i);
                i--;
            } 
        }
        
        for(i=0;i<SSR.size();i++){
            if(SSR.get(i).length()==2 && SSR.get(i).substring(0, 1).equalsIgnoreCase(SSR.get(i).substring(1))){
                    SSR.remove(i);
                    repeats.remove(i);
                    EndOfSsr.remove(i);
                    i--;
                }
            if(SSR.get(i).length()==3 && SSR.get(i).substring(0, 2).equalsIgnoreCase(SSR.get(i).substring(1))){
                SSR.remove(i);
                repeats.remove(i);
                EndOfSsr.remove(i);
                    i--;
            }
            if(SSR.get(i).length()==4 && SSR.get(i).substring(0, 2).equalsIgnoreCase(SSR.get(i).substring(2))){
                SSR.remove(i);
                repeats.remove(i);
                EndOfSsr.remove(i);
                    i--;
            }
            if(SSR.get(i).length()==5 && SSR.get(i).substring(0, 3).equalsIgnoreCase(SSR.get(i).substring(2,5))){
                SSR.remove(i);
                repeats.remove(i);
                EndOfSsr.remove(i);
                    i--;
            }
            if(SSR.get(i).length()==6 && 
                    (SSR.get(i).substring(0, 3).equalsIgnoreCase(SSR.get(i).substring(3)) 
                    || (SSR.get(i).substring(0, 2).equalsIgnoreCase(SSR.get(i).substring(2,4))
                        && SSR.get(i).substring(0, 2).equalsIgnoreCase(SSR.get(i).substring(4))))){
                SSR.remove(i);
                repeats.remove(i);
                EndOfSsr.remove(i);
                    i--;
            }
        }
    }
    /*
    public void toDB(String org,String chrom,int line) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
        Statement st = con.createStatement();
        st.executeUpdate("use LoBiD");
        
        for(int i=0;i<SSR.size();i++){
            if(repeats.get(i)*SSR.get(i).length() >=10){
                int start = EndOfSsr.get(i)-SSR.get(i).length()*repeats.get(i);
                st.executeUpdate("INSERT INTO sequence_info (chromosome,start,end,ssr,line_in_file,repeats) "
                        + "VALUES('"+chrom+"',"+start+","+EndOfSsr.get(i)+",'"+SSR.get(i)+"',"+line+","+repeats.get(i)+")");
                ResultSet rs = st.executeQuery("SELECT s_id FROM sequence_info "
                        + "WHERE chromosome='"+chrom+"' AND end="+EndOfSsr.get(i)+" AND line_in_file="+line+"");
                String sid = null;
                while(rs.next()){
                    sid=rs.getString(1);
                }
                rs = st.executeQuery("SELECT org_id FROM organism WHERE name='"+org+"'");
                String orgid = null;
                while(rs.next()){
                    orgid=rs.getString(1);
                }

                st.executeUpdate("INSERT INTO orgtossr (org_id,field_id) " 
                        + "VALUES("+orgid+","+sid+")");
            }
        }
        con.close();
    }
    */
    public void toFile(String org,String chrom,int line,boolean flag,String file) throws FileNotFoundException, IOException{
        if(flag){
            File f = new File("organisms/"+org+"/data/");
            if(!f.exists()){
                f.mkdir();
            }
            
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("organisms/"+org+"/data/"+chrom+".ssr",true)));
            /*
             * ara diavazoume  size - SSR - EndOfSsr - repeats - line
             */
            out.writeInt(SSR.size());
            out.writeInt(line);
            for(int i=0;i<SSR.size();i++){ 
                out.writeUTF(SSR.get(i));
                out.writeInt(EndOfSsr.get(i));
                out.writeInt(repeats.get(i));
                
                //System.out.println(SSR.get(i)+"\t"+EndOfSsr.get(i)+"\t"+repeats.get(i));
            }
            out.close();
        }
        if(!flag){
            File f = new File("local/"+org+"/data/");
            if(!f.exists()){
                f.mkdir();
            }
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("local/"+org+"/data/"+file+".ssr",true)));
            /*
             * ara diavazoume  size - SSR - EndOfSsr - repeats - line
             */
            out.writeInt(SSR.size());
            out.writeInt(line);
            for(int i=0;i<SSR.size();i++){ 
                out.writeUTF(SSR.get(i));
                out.writeInt(EndOfSsr.get(i));
                out.writeInt(repeats.get(i));
            }
            out.close();
        }
    }
    
    public String tString(int length){
        String out = new String();
        
        for(int i=0;i<SSR.size();i++){
            if(repeats.get(i)*SSR.get(i).length() >=length)
                //out++;
                out = out + "SSR: "+ SSR.get(i) + "\trepeats: " + repeats.get(i) + "\tending position: " + EndOfSsr.get(i) + "\n";
        }
        return out;
    }
    
    
}
