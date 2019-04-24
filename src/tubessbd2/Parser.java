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
        nama_tabel=data[3].replace(";","");
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
    public Tabel cekTabel(Tabel t1, Tabel t2, Tabel t3){
       if(this.nama_tabel.equals(t1.nama_tabel)){
           return t1;
       }else if (this.nama_tabel.equals(t2.nama_tabel)){
            return t2;
       }else if (this.nama_tabel.equals(t3.nama_tabel)){
           return t3;
       }else{
           System.out.println("     Tabel tidak ada ");
           return null;
       }
    }
    public String[] cekKolom(Tabel t1){
        int j=0;
        if(t1.equals("*")){
            return t1.kolom;
        }else{
            for (int i = 0; i < this.kolom.length; i++) {
                if(t1.cekKolom(this.kolom[i])){
                    j++;
                    System.out.println(this.kolom[i]);
                }
            }
            if(j==this.kolom.length) return this.kolom;
            else{
                System.out.println(this.kolom.length);
                System.out.println(j);
                
                System.out.println("    Kolom tidak sesuai");
                return null;
            }
        }
    }
    public int[] A1PK(Tabel t1,int B){
        int[] bil = new int[2];
        bil[0] = 1;
        bil[1] = t1.br(B)/2;
        return bil;
        
    }
    
   public int[] A1NPK(Tabel t1,int B){
        int[] bil = new int[2];
        bil[0] = 1;
        bil[1] = t1.br(B);
        return bil;
        
    }
    
   public int[] A2(Tabel t1,int B,int P){
        int[] bil = new int[2];
       int jum = t1.hi(B, P);
        bil[0] = jum;
        bil[1] = jum;
        return bil;
        
    }
   
   public String Cost1(String[] kondisi,Tabel t, int B){
       int[] cek1 = new int[2];
       String hasil="";
       if(kondisi[0].contains(t.kolom[0])){
           cek1 = A1PK(t,B);
       }else{
           cek1=A1NPK(t,B);
       }
      
       
       hasil = cek1[0]+" ts + "+cek1[1]+" tr";
       return hasil;
       
   }
   
    public String Cost2(String[] kondisi,Tabel t, int B,int P){
       int[] cek = new int[2];
       String hasil="";
       cek = A2(t,B,P);
      
       
       hasil = cek[0]+" ts + "+cek[1]+" tr";
       return hasil;
       
   }
    
     public String Optimal(String[] kondisi,Tabel t, int B,int P){
       int[] cek1 = new int[2];
       int[] cek2 = new int[2];
       String hasil="";
       if(kondisi[0].contains(t.kolom[0])){
           cek1 = A1PK(t,B);
       }else{
           cek1=A1NPK(t,B);
       }
      
       cek2 =A2(t,B,P);
       
       if((cek2[0]+cek2[1])>(cek1[0]+cek1[1])){
           hasil = "QEP Optimal : QEP#1";
       }else if ((cek2[0]+cek2[1])<(cek1[0]+cek1[1])){
            hasil = "QEP Optimal : QEP#2";
       }else{
           hasil = "QEP Optimal : QEP#1 dan QEP#2";
       }
       return hasil;
       
   }
   
   public String[] QEP1(Tabel tab, String[] kondisi,int B,int P){
       String[] kata = new String[4];
       String kol="";
       for (int i = 0; i < tab.kolom.length; i++) {
           kol+=tab.kolom[i]+",";
       }
       kata[0] = "PROJECTION "+kol + " --on the fly-- ";
       String bantu;
       if(kondisi[0].contains(tab.kolom[0])){
           bantu=" --A1 key";
       }else{
           bantu =" --A1 No-Key";
       }
       kata[1] = "SELECTION "+kondisi[0]+bantu;
       kata[2]= tab.nama_tabel;
       kata[3] = ""+Cost1(kondisi,tab,B);
       
       return kata;
   }
    
    
    
    
    
    
}
