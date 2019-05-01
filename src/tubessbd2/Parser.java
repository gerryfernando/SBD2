/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessbd2;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liuslangobelen
 */
public class Parser {
    String nama_tabel1;
    String nama_tabel2;
    String[] kolom1;
    String[] kolom2;
    String[] data;
    String using;
    
    public Parser(String s){
        data=s.split(" ");
        nama_tabel1=data[3].replace(";","");
        kolom1=getkolom(data[1]);
        kolom2= new String[kolom1.length];
        if(s.contains("join")){
            nama_tabel2=data[5];
            using=data[7].replace("(","");
            using=using.replace(");","");
        }else {
            nama_tabel2="";
            using="";
        }
        
    }
    public String[] getkolom(String s){
        String koma1 = s.replace(", ", ",");
        String koma2 = koma1.replace(" ,", ",");
        String[] input = koma2.split(",");
        return input;
    }
    public void splitKolom(Tabel t1,Tabel t2,String[] kol){
        int idx1=0,idx2=0,i=0;
        while(i<kol.length) {
            if(t1.cekKolom(kol[i])){
                this.kolom1[idx1]=kol[i];
                idx1++;
                        }
            if(t2.cekKolom(kol[i])){
                this.kolom2[idx2]=kol[i];
                idx2++;
            }
            i++;
        }

    }
    public String[] isKondisi(String[] p){
        boolean containtwhere=false;
        for (int i = 0; i < p.length; i++) {
         if(p[i].equals("where"))containtwhere=true;
        }
        if(containtwhere){
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
        }
        return null;
       
    }
    public Tabel cekTabel1(Tabel t1, Tabel t2, Tabel t3){
       if(this.nama_tabel1.equals(t1.nama_tabel)) return t1;
       else if (this.nama_tabel1.equals(t2.nama_tabel)) return t2;
       else if (this.nama_tabel1.equals(t3.nama_tabel)) return t3;
       else{
           System.out.println("     Tabel tidak ada ");
           return null;
       }
    }
    public Tabel cekTabel2(Tabel t1, Tabel t2, Tabel t3){
       if(this.nama_tabel2.equals(t1.nama_tabel)) return t1;
       else if (this.nama_tabel2.equals(t2.nama_tabel)) return t2;
       else if (this.nama_tabel2.equals(t3.nama_tabel)) return t3;
       else{
           System.out.println("     Tabel tidak ada ");
           return null;
       }
    }
    
    public String[] cekKolom(Tabel t1){
        int j=0;
        if(this.kolom1[0].equals("*")){
            return t1.kolom;
        }else{
            for (int i = 0; i < this.kolom1.length; i++) {
                if(t1.cekKolom(this.kolom1[i])){
                    j++;
                }
            }
            if(j==this.kolom1.length) return this.kolom1;
            else{
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
       hasil = cek1[1]+" Block Transfer + "+cek1[0]+" Seek";
       return hasil;
   }
   
    public String Cost2(String[] kondisi,Tabel t, int B,int P){
       int[] cek = new int[2];
       String hasil="";
       cek = A2(t,B,P);
       hasil = cek[1]+" Block Transfer + "+cek[0]+" Seeks";
       return hasil;
   }
    public int[] BNLJ(Tabel t1,Tabel t2,int B){
        int[] bill = new int[2];
        bill[0] = (t1.br(B)*t2.bs(B))+t1.br(B);
        bill[1] = 2*t1.br(B);
        return bill;
    }
    
     public String Optimal1(String[] kondisi,Tabel t, int B,int P,String s){
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
     
      
    
    public String Optimal2(int[] cek1,int[] cek2){
    
       String hasil="";
       int jum1= cek1[0]+cek1[1];
       int jum2= cek2[0]+cek2[1];
       if(jum1<jum2){
           hasil = "QEP Optimal : QEP#1";
       }else if (jum2<jum1){
            hasil = "QEP Optimal : QEP#2";
       }else{
           hasil = "QEP Optimal : QEP#1 dan QEP#2";
       }
       return hasil;   
   } 
     
   public String[] QEP(Tabel tab, String[] kondisi,int B,int P,String qep){
       String[] kata = new String[5];
       String kol="";
       for (int i = 0; i < tab.kolom.length; i++) {
           if(tab.kolom[i]!=null) kol+=tab.kolom[i]+",";
       }if(qep.equals("QEP1")){
           kata[0] = "  >>QEP #1";
           kata[4] = "      Cost : "+Cost1(kondisi,tab,B);
       }
       else {
           kata[0]="   >>QEP #2";
           kata[4] = "      Cost : "+Cost2(kondisi,tab,B,P);
       }
       kata[1] = "      PROJECTION "+kol + " --on the fly-- ";
       String bantu;
       if (qep.equals("QEP1")) {
           if(kondisi[0].contains(tab.kolom[0])){
           bantu=" --A1 key";
           }else{
           bantu =" --A1 No-Key";
           }
       }else{
           bantu ="--A2";
       }
       kata[2] = "      SELECTION "+kondisi[0].replace(";", "")+" "+bantu;
       kata[3] = "      "+tab.nama_tabel;
       
       return kata;
   }
   
   
   public String[] qepJoin(Tabel t1,Tabel t2,int[] cost,String qep,String[] kol){
        String[] kata = new String[5];
        String cek="";
        if(qep.equals("QEP1")){
            kata[0] = "  >>QEP #1";
        }
        else {
            kata[0]="   >>QEP #2";
        }
        if(!kol[0].equals("*")){
            for (int i = 0; i < kol.length; i++) {
                cek+=kol[i]+", ";
             }
        }else{
            int j=0;
            for (int i = 0; i < t1.kolom.length; i++) {
                if(t1.kolom[i]!=null){if(!cek.contains(t1.kolom[i])){cek+=t1.kolom[i]+", ";}}
            }
            while(j<t2.kolom.length && t2.kolom[j]!=null) {
                if(t1.kolom[j]!=null){if(!cek.contains(t2.kolom[j])){cek+=t2.kolom[j]+", ";}}j++;
            }
        }
        kata[1] = "      PROJECTION "+cek+ " --on the fly-- ";
        String bantu;
            bantu=" -BNLJ";
        kata[2] = "         JOIN "+t1.nama_tabel+"."+this.using+" = "+t2.nama_tabel+"."+this.using+bantu;
        kata[3] = "      "+t1.nama_tabel+"  "+t2.nama_tabel;
        kata[4] = "      Cost(worst case) : "+cost[0]+" Block Transfer + "+cost[1]+" Seeks";
        return kata;   
   }
   
   public void SaveData(String s){
       try{
            FileWriter writer = new FileWriter("src/tubessbd2/sharedpool.csv");
            writer.write(s);
            writer.flush();
            writer.close();
       } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
       
   }
   
   public void addSharedPool(String query,String[] QEP) throws IOException{
       String cek= "";
       String hasil = "Query = "+query+"\n";
       for (int i = 1; i < QEP.length; i++) {
           hasil+= QEP[i]+"\n";
           
       }
       hasil+="--------------------------------------------------------------\n";
       cek=readCSV();
       if (cek!=null) {
           hasil=cek+hasil;
       }
       SaveData(hasil);
   }
   
   public String readCSV() throws FileNotFoundException, IOException{
      int i=0;
       BufferedReader csvReader = new BufferedReader(new FileReader("src/tubessbd2/sharedpool.csv"));  
        String row="";
        String data="";
        while ((row = csvReader.readLine()) != null) {  
            data+=row+"\n";
            i++;
        }
        csvReader.close(); 
       
       return data;
           
       }
   }
   
    
    
    
    

