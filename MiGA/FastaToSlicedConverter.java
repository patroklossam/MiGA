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
import javax.swing.JProgressBar;

public class FastaToSlicedConverter {
    private String buffer;
    private BufferedReader fromfasta;
    private PrintWriter out;
    private PrintWriter index;
    
    public FastaToSlicedConverter(File a,String name) throws FileNotFoundException, IOException {
        convert(a,name);
        
    }
    
    public final void convert(File a,String name) throws FileNotFoundException, IOException{
        boolean eof=false;
        File f = new File("local/");
        if(!f.exists()){
            f.mkdir();
        }
        f = new File("local/"+name);
        if(!f.exists()){
            f.mkdir();
        } 
        out = new PrintWriter("local/"+name+"/temp.txt");
        index = new PrintWriter("local/"+name+"/index.txt");
        fromfasta = new BufferedReader(new FileReader(a));
        buffer="";
        int count =0;
        while(!eof){
            try{
                String temp = fromfasta.readLine();
                if(temp.contains(">")){
                    if(!buffer.equals(""))
                        out.println(buffer);
                    buffer="";
                    out.close();
                    index.println(temp.substring(1).replace(':', '_'));
                    out = new PrintWriter("local/"+name+"/"+temp.substring(1).replace(':', '_') +".txt");
                    count++;
                }
                else{
                    if(buffer.length()+temp.length()<=20000)
                        buffer+=temp;
                    else{
                        out.println(buffer);
                        buffer="";
                        buffer+=temp;
                        count++;
                    }

                }
            }catch(NullPointerException e){
                eof = true;
                if(!buffer.equals(""))
                    out.println(buffer);
                buffer="";
                out.close();   
            }
        }
        
        //out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("test.txt")));
        //Sequence seq = new Sequence(buffer);
        //out.writeUTF(buffer);
        
        
        index.close();
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
