/** 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 *
 * Copyright ownership: Ioannis Kavakiotis, Patroklos Samaras, Antonios Voulgaridis
 */
package matrixparser;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Ioannis Kavakiotis, Patroklos Samaras, Antonios Voulgaridis
 */
public class parser {
    private BufferedReader in;
    private int termcount;
    private int linecount;
    private String[] terms;
    private List<String> matrix;
    private HashMap<Integer,HashMap<Integer,List<String>>> map;
    private HashMap<Integer, int[]> ids;
    
    public parser(int a) throws FileNotFoundException{
        if(a==1)
            in = new BufferedReader(new FileReader("std/Partially_Standardization.txt"));
        else
            in = new BufferedReader(new FileReader("std/Fully_Standardization.txt"));
        
        map = new HashMap<Integer,HashMap<Integer,List<String>>>();
        termcount =0;
        linecount =0;
        String buffer;
        boolean eof = false;
        
        for(int i=1;i<=6;i++){
            map.put(i, new HashMap<Integer,List<String>>());
        }
        int c=0;
        int found_len = 0;
        int cur_len = 0;
        while (!eof){
            try {
                buffer = in.readLine();
                if(buffer==null)
                    eof=true;
                else{
                    linecount++;
                    StringTokenizer str = new StringTokenizer(buffer," \t");
                    String first="null";
                    //for(int i=0;i<str.countTokens();i++){
                    int i=0;
                    while(str.hasMoreTokens()){
                        String temp = str.nextToken(" \t");
                        if(i==0){
                            first = temp;
                            
                            found_len = first.length();
                            if(found_len>cur_len){
                                c=0;
                                cur_len = found_len;
                            }
                            map.get(first.length()).put(c, new ArrayList<String>());
                        }
                        map.get(first.length()).get(c).add(temp);
                        //System.out.println(temp);
                        termcount++;
                        i++;
                    }
                    c++;
                }
            } catch (IOException ex) {
                eof = true;
            }
        }
        
        initIds();
        //System.out.println(termcount);
        //System.out.println(linecount);
        //System.out.println(map.size());
        //System.out.println(map.get(1).size()+map.get(2).size()+map.get(3).size()+map.get(4).size()+map.get(5).size()+map.get(6).size());
    }
    
    private void initIds(){
        
        ids = new HashMap<Integer, int[]>();
        
        for(int i=1;i<7;i++){
            int size = map.get(i).size();
            ids.put(i, new int[size]);
            
            for(int c=0;c<size;c++)
                ids.get(i)[c] = -4;
        }
    }
    
    public HashMap<Integer,HashMap<Integer,List<String>>> getMap(){
        return map;
    }
    
    public String getFirst(int len, int id){
        return map.get(len).get(id).get(0);
    }
    
    public int checkFound(int len, int a){
        return ids.get(len)[a];
    }
    
    public void makeFound(int len, int a, int idlist){
        ids.get(len)[a] = idlist;
    }
    
    public int SearchMap(int len, String str){
        int size = map.get(len).size();
        for(int i=0;i<size;i++){
            
            if(map.get(len).get(i).contains(str)){
                return i;
            }
                
        }
        return -1; // not found
    }
    
    public void PrintMap(){
        for(int len=1;len<7;len++){
            for(int i=0;i<map.get(len).size();i++)
                System.out.println(map.get(len).get(i));
        }
    }
    
}
