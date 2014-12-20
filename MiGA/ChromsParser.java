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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class ChromsParser {
    
    private String[] chromosomes;
    
    public ChromsParser(String org) throws ClassNotFoundException, SQLException{
        chromosomes = Cparser(org);
        
    }
    
    private String[] Cparser(String organism) throws ClassNotFoundException, SQLException{
        
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "biouser", "thesis2012");
        Statement st = con.createStatement();
        st.executeUpdate("use LoBiD");
        
        ResultSet res = st.executeQuery("SELECT * FROM organism WHERE name = '"+organism+"'");
        
        res.next();
        //System.out.println(res.getString(4));
        
        
        /*
         * Chromosome line parser for DB
         */
        String[] chroms;
        if(Integer.parseInt(res.getString(3))==1){
            chroms = res.getString(4).split("\\?"); // X?Y# returns ["X","Y#"]
            int k = chroms.length-1;
            chroms[k] = chroms[k].substring(0, chroms[k].length()-1); // last string "Y#" -> "Y"

            con.close();
        }else{
            chroms = new String[2];
            chroms[0]=res.getString(4);
            chroms[1]="nil";
        }
        return chroms;
    }
    
    public String[] getChroms(){
        return chromosomes;
    }
    
    
}
