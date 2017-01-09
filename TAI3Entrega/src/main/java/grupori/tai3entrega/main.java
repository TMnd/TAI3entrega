/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.tai3entrega;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author joaoa
 */
public class main {
    private static void freqsGenerator(String ficheiro){
        String nomeFicheiro[] = ficheiro.split("\\.");
        try {
            //String CMD = "\\GetMaxFreqs.exe " + musicaCaminho +".freq " + musicaCaminho +".wav";
            String CMD ="cmd.exe /c cd src//main//java//grupori//tai3entrega && GetMaxFreqs.exe -w "+nomeFicheiro[0]+".freqs "+nomeFicheiro[0]+".wav";
            // Run "script" Windows command
            Process process = Runtime.getRuntime().exec(CMD);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        //System.out.println("Insira o caminho da pasta:");
        String PastaMusicaCaminho = "src\\main\\java\\grupori\\tai3entrega";//sc.next();
        
        File folder = new File(PastaMusicaCaminho);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(file.getName().endsWith(".wav")){
                    freqsGenerator(file.getName());
                }
            }
        }
        System.out.println("Done");
    }
}
