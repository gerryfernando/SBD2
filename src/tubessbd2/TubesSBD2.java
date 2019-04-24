/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubessbd2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import tubessbd2.Tabel;
import tubessbd2.Parser;
/**
 *
 * @author liuslangobelen
 */
public class TubesSBD2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
    	
    	System.out.println(">> Menu Utama : ");
    	System.out.println("    1. Tampilkan BFR dan Fannout Ratio Setiap Tabel ");
    	System.out.println("    2. Tampilkan Total Blok Data + Blok Index Setiap Tabel ");
    	System.out.println("    3. Tampilkan Jumlah Blok yang Diakses Untuk Pencarian Record ");
    	System.out.println("    4. Tampilkan QEP dan Cost ");
    	System.out.println("    5. Tampilkan Isi File Shared Pool ");
    	System.out.print(">> Masukkan Pilihan Anda : ");
        
        Scanner input = new Scanner(System.in);
        Scanner str = new Scanner(System.in);
        int number = input.nextInt();
        
        int B=0,P=0,i=0;
        String[] temp = new String[10];
        String[] tabel1 = new String[10];
        String[] tabel2 = new String[10];
        String[] tabel3 = new String[10];

        BufferedReader csvReader = new BufferedReader(new FileReader("src/tubessbd2/data.csv"));  
        String row;
        String[] data = new String[4];
        while ((row = csvReader.readLine()) != null) {  
            data[i]=row;
            i++;
        }
        csvReader.close(); 
        temp=data[0].split(";");
        for(i=0;i<temp.length;i++){
            if(temp[i].equals("P"))P=Integer.parseInt(temp[i+1]);
            else if(temp[i].equals("B")) B=Integer.parseInt(temp[i+1]);
        }
        for(i=1;i<data.length;i++){
            temp=data[i].split(";");
            if(i==1)tabel1=temp;
            else if(i==2)tabel2=temp;
            else if(i==3)tabel3=temp;
        }
        Tabel tabeli = new Tabel(tabel1);
        Tabel tabelii = new Tabel(tabel2);
        Tabel tabeliii = new Tabel(tabel3);
        
        switch(number) {
            case 1:
              // code block
                System.out.println("> Menu 1 : BFR dan Fan Out Ratio");
                System.out.println("    BFR "+tabeli.nama_tabel+" : "+tabeli.Bfr(B,tabeli));
                System.out.println("    Fan Out Ratio "+tabeli.nama_tabel+" : "+tabeli.Fan(B,tabeli.V,P));
                System.out.println("    BFR "+tabelii.nama_tabel+" : "+tabelii.Bfr(B,tabelii));
                System.out.println("    Fan Out Ratio "+tabelii.nama_tabel+" : "+tabelii.Fan(B,tabelii.V,P));
                System.out.println("    BFR "+tabeliii.nama_tabel+" : "+tabeliii.Bfr(B,tabeliii));
                System.out.println("    Fan Out Ratio "+tabeliii.nama_tabel+" : "+tabeliii.Fan(B,tabeliii.V,P));
              break;
            case 2:
              // code block
                System.out.println("> Menu 2 : Jumlah Blok");
                System.out.println("    Tabel Data "+tabeli.nama_tabel+" : "+tabeli.Blockdata(tabeli.Bfr(B,tabeli),tabeli.n)+" blok");
                System.out.println("    Index "+tabeli.nama_tabel+" : "+tabeli.BlockIndex(tabeli.n,tabeli.Fan(B,tabeli.V,P))+" blok");
                System.out.println("    Tabel Data "+tabelii.nama_tabel+" : "+tabelii.Blockdata(tabeli.Bfr(B,tabelii),tabelii.n)+" blok");
                System.out.println("    Index "+tabelii.nama_tabel+" : "+tabelii.BlockIndex(tabelii.n,tabelii.Fan(B,tabelii.V,P))+" blok");
                System.out.println("    Tabel Data "+tabeliii.nama_tabel+" : "+tabeliii.Blockdata(tabeli.Bfr(B,tabeliii),tabeliii.n)+" blok");
                System.out.println("    Index "+tabeliii.nama_tabel+" : "+tabeliii.BlockIndex(tabeliii.n,tabeliii.Fan(B,tabeliii.V,P))+" blok");
              break;
            case 3:
                System.out.println("> Menu 3 : Pencarian Record");
                System.out.print("    Cari Record ke : ");
                number = input.nextInt();
                System.out.print("    Nama Tabel : ");
                String tab = str.nextLine();
                if(tab.equals(tabeli.nama_tabel)){
                System.out.println("");
                System.out.println(">> Menggunakan index, jumlah blok yang diakses : "+tabeli.indexedRecordSearch(number, tabeli.Fan(B,tabeli.V,P)));
                System.out.println(">>Tanpa index, jumlah blok yang diakses : "+tabeli.seqRecordSearch(number,tabeli.Bfr(B,tabeli) ));
                }else if(tab.equals(tabelii.nama_tabel)){
                System.out.println("");
                System.out.println(">> Menggunakan index, jumlah blok yang diakses : "+tabelii.indexedRecordSearch(number, tabelii.Fan(B,tabeli.V,P)));
                System.out.println(">>Tanpa index, jumlah blok yang diakses : "+tabelii.seqRecordSearch(number,tabelii.Bfr(B,tabelii) ));
                }else if(tab.equals(tabeliii.nama_tabel)){
                System.out.println("");
                System.out.println(">> Menggunakan index, jumlah blok yang diakses : "+tabeliii.indexedRecordSearch(number, tabeliii.Fan(B,tabeli.V,P)));
                System.out.println(">>Tanpa index, jumlah blok yang diakses : "+tabeliii.seqRecordSearch(number,tabeliii.Bfr(B,tabeliii) ));
                }
                
              break;
            case 4:
                System.out.println("> Menu 4 : QEP dan COST");
                System.out.print("  Masukkan query : ");
                tab=str.nextLine();
                Parser query = new Parser(tab);
                Tabel tabs = query.cekTabel(tabeli, tabelii, tabeliii);
                if(tabs!=null){
                    tabs.kolom=query.cekKolom(tabs);
                    if(tabs.kolom!=null){
                        System.out.println("    Output : ");
                        System.out.println("    Tabel : "+query.nama_tabel);
                        System.out.print("    List kolom : ");
                        temp=query.isKondisi(query.data);
                        for(i=0 ;i<query.kolom.length;i++) System.out.print(query.kolom[i]+", ");
                        System.out.println("");
                        
                        for (int j = 0; j < temp.length; j++) {
                            System.out.println(temp[j]);
                        }
                        String[] kata=query.QEP1(tabs, temp, B,P );
                        for (int j = 0; j < 4; j++) {
                            System.out.println(kata[j]);
                        }
                    }
                }
              break;
            case 5:
                
              break;
            default:
                System.out.println("Masukkan harus sesuai dengan menu yang ada; ");
              // code block
          }
    }
    
}
