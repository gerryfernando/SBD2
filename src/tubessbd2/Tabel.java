/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessbd2;

import static java.lang.Math.ceil;

/**
 *
 * @author liuslangobelen
 */
public class Tabel {
        String nama_tabel;
        String[] kolom=new String[10];
        int R,n,V ;
       
    public Tabel(String[] tabel){
        this.nama_tabel = tabel[0];
        boolean tstop=false;
        for(int i = 1; i<tabel.length;i++){
            if(!tabel[i].equals("R") && tstop==false){
                this.kolom[i-1]=tabel[i];
            }else if(tabel[i].equals("R")){
                this.R=Integer.parseInt(tabel[i+1]);
                tstop=true;
            }
            else if(tabel[i].equals("n")){
                this.n=Integer.parseInt(tabel[i+1]);
            }
            else if(tabel[i].equals("V")){
                this.V=Integer.parseInt(tabel[i+1]);
            }
            
        }
    }
    public int Bfr(int B,Tabel t){
        return B/t.R;
    }
    public int Fan(int B,int V,int P){
        return B/(V+P);
    }
    public int Blockdata(int Bfr,int n){
        return (n/Bfr)+1;
    }
    public int BlockIndex(int n,int fan){
        return (n/fan)+1;
    }
    public int seqRecordSearch(int input, int bfr){
        return (input/bfr)+1;
    }
    public int indexedRecordSearch(int input, int fan){
        return (input/fan)+1+1;
    }
}
