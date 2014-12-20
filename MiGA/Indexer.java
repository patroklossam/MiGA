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

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Indexer {
    private long seqfiles;
    private long current;
    private BufferedReader index;
    private String path;
    private boolean karyotype;
    private List<String> chromosomes;
    
    public Indexer(String in) throws FileNotFoundException, IOException{
        // gets the index.txt
        index = new BufferedReader(new FileReader((in)));
        current=0;
        path = in;
        seqfiles = (long) countlines(path);
        karyotype = false;
        chromosomes = null;
    }
    
    public Indexer(long num){
        // gets the slice count from LoBiD
        seqfiles = num;
        current = 0;
        index=null;
        karyotype = false;
        chromosomes = null;
    }
    
    public Indexer(List<String> chroms){
        // gets the chromosomes
        
        chromosomes = chroms;
        seqfiles = chromosomes.size();
        current = 0;
        index=null;
        karyotype = true;
    }
    
    public String getNextFileName(){
        if(index == null){
            if(karyotype){
                if(current < seqfiles){
                    String num = current+"";
                    String filenum = chromosomes.get(Integer.parseInt(num));
                    current++;
                    return filenum+".ssr";
                }
                else
                    return null;
            }else{
                if(current < seqfiles){
                    long filenum = current +1;
                    current++;
                    return filenum+".ssr";
                }
                else
                    return null;
            }
        }else{
            String temp;
            if(current < seqfiles){
                try {
                    temp = index.readLine();
                    current++;
                    return temp+".ssr";
                } catch (IOException ex) {
                    Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                return null;
            }            
        }
        
        return null;
    }
    
    public boolean hasNext(){
        if(current<seqfiles)
            return true;
        else
            return false;
    }
    
    private long countlines(String filename) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] b = new byte[1024];
            long count = 0;
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
    
    
    public void Close() throws IOException{
        if(index!=null)
            index.close();
        }
    
}
