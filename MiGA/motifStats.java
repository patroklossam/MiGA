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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class motifStats {

    private String motif;
    private int count;
    private int repeats;
    private long mbp_len;
    private double avg_len;
    private int max_len;
    private double avg_repeats;
    private double perA;
    private double perT;
    private double perG;
    private double perC;
    private List<Integer> lens;
    private float st_dev;
    private boolean sd_clc; 
    
    public  motifStats(String m, int r) {
        sd_clc = false;
        motif = m;
        count = 1;
        repeats = r;
        mbp_len = motif.length() * r;
        avg_len = (double) mbp_len / count;
        max_len = (int) mbp_len;
        avg_repeats = (double) repeats / count;

        if (motif.contains("A")) {
            perA = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "A") / mbp_len * 100;
        }
        if (motif.contains("T")) {
            perT = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "T") / mbp_len * 100;
        }
        if (motif.contains("G")) {
            perG = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "G") / mbp_len * 100;
        }
        if (motif.contains("C")) {
            perC = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "C") / mbp_len * 100;
        }
        
        lens = new ArrayList<Integer>();
        lens.add(r*m.length());
    }

    public void update(int re) {
        count++;
        repeats += re;
        mbp_len += re * motif.length();
        avg_len = (double) mbp_len / count;
        if (re * motif.length() > max_len) {
            max_len = re * motif.length();
        }
        avg_repeats = (double) repeats / count;


        if (motif.contains("A")) {
            perA = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "A") / mbp_len * 100;
        }
        if (motif.contains("T")) {
            perT = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "T") / mbp_len * 100;
        }
        if (motif.contains("G")) {
            perG = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "G") / mbp_len * 100;
        }
        if (motif.contains("C")) {
            perC = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "C") / mbp_len * 100;
        }
        
        lens.add(re*motif.length());
    }

    public void refresh() {
        if (motif.contains("A")) {
            perA = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "A") / Globals.A * 100;
        }
        if (motif.contains("T")) {
            perT = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "T") / Globals.T * 100;
        }
        if (motif.contains("G")) {
            perG = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "G") / Globals.G * 100;
        }
        if (motif.contains("C")) {
            perC = (double) (mbp_len / motif.length()) * StringUtils.countMatches(motif, "C") / Globals.C * 100;
        }
    }
    
    public void calcSD(){
        float sd=0;
        int up;
        
        for(int i=0;i<lens.size();i++){
            sd += Math.pow(lens.get(i) - this.getAvg_len(),2);
        }
        sd = (float) sd / lens.size();
        
        sd = (float) Math.sqrt(sd);
        
        st_dev = sd;
    }

    public void mergeNrefresh(motifStats m) {
        //teleutaia allagh gia 200% bug

        if (motif.contains("A")) {
            perA = (double) (mbp_len / motif.length()) * (StringUtils.countMatches(motif, "A") + StringUtils.countMatches(m.getMotif(), "A")) / (Globals.A) * 100;
        }
        if (motif.contains("T")) {
            perT = (double) (mbp_len / motif.length()) * (StringUtils.countMatches(motif, "T") + StringUtils.countMatches(m.getMotif(), "T")) / (Globals.T) * 100;
        
        }
        if (motif.contains("G")) {
            perG = (double) (mbp_len / motif.length()) * (StringUtils.countMatches(motif, "G") + StringUtils.countMatches(m.getMotif(), "G")) / (Globals.G) * 100;
        }
        if (motif.contains("C")) {
            perC = (double) (mbp_len / motif.length()) * (StringUtils.countMatches(motif, "C") + StringUtils.countMatches(m.getMotif(), "C")) / (Globals.C) * 100;
        }
    }

    public double getAvg_len() {
        return avg_len;
    }

    public double getAvg_repeats() {
        return avg_repeats;
    }

    public int getCount() {
        return count;
    }

    public int getMax_len() {
        return max_len;
    }

    public long getMbp_len() {
        return mbp_len;
    }

    public String getMotif() {
        return motif;
    }

    public double getPercentA() {
        return perA;
    }

    public double getPercentC() {
        return perC;
    }

    public double getPercentG() {
        return perG;
    }

    public double getPercentT() {
        return perT;
    }

    public int getNumOfRepeats() {
        return repeats;
    }
    
    public List<Integer> getLenList(){
        return lens;
    }

    final public String toString() {
        if(!sd_clc){
            calcSD();
            sd_clc = true;
        }
        
        String str = cell(motif, 6) + "  " + cell(count, 6) + "  " + cell(repeats, 7) + "  " + cell(mbp_len, 10)
                + "  " + cell(avg_len, 10) + "  " + cell(st_dev, 9) + "  " + cell(max_len, 10) + "  " + cell(avg_repeats, 11)
                + "  " + cell(perA, 6) + "  " + cell(perT, 6) + "  " + cell(perC, 6) + "  " + cell(perG, 6) + "  ";
        //System.out.println(str);
        return str;
    }
    
    final public String toHTML() {
        
        if(!sd_clc){
            calcSD();
            sd_clc = true;
        }
        
        DecimalFormat round = new DecimalFormat("#.###");
        
        String html = "<tr><td>"+motif+"</td><td>"+count+"</td><td>"+repeats+"</td><td>"+mbp_len+"</td>"
                + "<td>"+round.format(avg_len)+"</td><td>"+round.format(st_dev)+"</td><td>"+max_len+"</td><td>"+round.format(avg_repeats)+"</td>"
                + "<td>"+round.format(perA)+"</td><td>"+round.format(perT)+"</td><td>"+round.format(perC)+"</td><td>"+round.format(perG)+"</td></tr>";
        
        return html;
    }

    public void merge(motifStats m) {

        count += m.getCount();
        repeats += m.getNumOfRepeats();
        mbp_len += m.getMbp_len();
        if (max_len < m.getMax_len()) {
            max_len = m.getMax_len();
        }

        avg_len = (double) mbp_len / count;
        avg_repeats = (double) repeats / count;
        
        lens.addAll(m.getLenList());
        
        Globals.C -= StringUtils.countMatches(m.motif, "C") * m.getNumOfRepeats();
        Globals.T -= StringUtils.countMatches(m.motif, "T") * m.getNumOfRepeats();
        Globals.G -= StringUtils.countMatches(m.motif, "G") * m.getNumOfRepeats();
        Globals.A -= StringUtils.countMatches(m.motif, "A") * m.getNumOfRepeats();
        
        Globals.C += StringUtils.countMatches(motif, "C") * m.getNumOfRepeats();
        Globals.T += StringUtils.countMatches(motif, "T") * m.getNumOfRepeats();
        Globals.G += StringUtils.countMatches(motif, "G") * m.getNumOfRepeats();
        Globals.A += StringUtils.countMatches(motif, "A") * m.getNumOfRepeats();
        
        refresh();
        //mergeNrefresh(m);

        /*String str =  cell(motif,6)+"  "+cell(count,6)+"  "+cell(repeats,7)+"  "+cell(mbp_len,10)
        +"  "+cell(avg_len,10)+"  "+cell(max_len,10)+"  "+cell(avg_repeats,11)
        +"  "+cell(perA,6)+"  "+cell(perT,6)+"  "+cell(perC,6)+"  "+cell(perG,6)+"  ";
        System.out.println(str);*/
    }

    private String cell(String ele, int len) {
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

    private String cell(double fl, int len) {

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
        } else {
            for (int i = 0; i < len - 1; i++) {
                str += " ";
            }
            str += "0";
            return str;
        }
    }
}
