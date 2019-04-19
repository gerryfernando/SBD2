/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessbd2;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.util.Arrays;

/**
 *
 * @author liuslangobelen
 */
public class Parser {
    String nama_tabel;
    String[] kolom;
    String[] data;
    
    public Parser(String s){
        data=s.split(" ");
        nama_tabel=data[3];
        kolom=getkolom(data[1]);
    }
    public String[] getkolom(String s){
        String koma1 = s.replace(", ", ",");
        String koma2 = koma1.replace(" ,", ",");
        String[] input = koma2.split(",");
        return input;
    }
    public String[] isKondisi(String[] p){
        String[] kondisi=new String[p.length-5];
        String[] konfix;
        int j=0,l=0;
        for (int i = 1; i < p.length; i++) {
            if(p[i].equals("where")||p[i].equals("Where")){
                kondisi[j]=p[i+1];j++;
                for (int k = i+1; k < p.length; k++) {
                    if(p[k].equals("and")||p[k].equals("And")){
                        kondisi[j]=p[k+1];l++;
                    } 
                    
                }
                
                konfix=Arrays.copyOf(kondisi, kondisi.length-l);

                return konfix;
            }
            
        }
        return null;
    
    }
    
}
