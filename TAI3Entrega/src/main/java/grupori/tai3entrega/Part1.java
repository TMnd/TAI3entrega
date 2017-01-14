/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.tai3entrega;

import static grupori.tai3entrega.main.sc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author joaoa
 */
public class Part1 {
    private static void freqsGenerator(String ficheiro) {
        String nomeFicheiro[] = ficheiro.split("\\.");
        try {
            String CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//clientes && GetMaxFreqs.exe -w " + nomeFicheiro[0] + ".freqs " + nomeFicheiro[0] + ".wav";
            // Correr o "script" do cmd
            Process process = Runtime.getRuntime().exec(CMD);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    private static void gerarFreqs(String PastaMusicaCaminho) {
        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().endsWith(".wav")) {
                    freqsGenerator(file.getName());
                }
            }
        }
    }
    
    private static void sox(File ficheiro, int option) {
        String CMD = null;
        try {
            switch (option) {
                case 1:
                    System.out.println("Adicionar whitenoise"); //adcionar uma opção para aumentar o "grayu"?
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe "+ficheiro.getAbsoluteFile()+" -p synth brownnoise vol 0.02 | sox.exe -m "+ficheiro.getAbsoluteFile()+" - ..//clientes//excerto.wav";
                    System.out.println(CMD);
                    break;
                case 2:
                    System.out.println("Adicionar brownnoise"); //adcionar uma opção para aumentar o "grayu"?
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe "+ficheiro.getAbsoluteFile()+" -p synth brownnoise vol 0.02 | sox.exe -m "+ficheiro.getAbsoluteFile()+" - ..//clientes//excerto.wav";
                    System.out.println(CMD);
                    break;
                case 3:
                    System.out.println("Adicionar pinknoise"); //adcionar uma opção para aumentar o "grayu"?
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe "+ficheiro.getAbsoluteFile()+" -p synth pinknoise vol 0.02 | sox.exe -m "+ficheiro.getAbsoluteFile()+" - ..//clientes//excerto.wav";
                    System.out.println(CMD);
                    break;
                case 4:
                    System.out.println("Insira o começo:");
                    int musicaComeco = sc.nextInt();
                    System.out.println("Insira o limite:");
                    int musicaSeleccao = sc.nextInt();
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe "+ficheiro.getAbsoluteFile()+" ..//clientes//excerto.wav trim " + musicaComeco+ " " + musicaSeleccao;
                    break;
                default:
                    break;
            }
            // Correr o "script" do cmd
            Process process = Runtime.getRuntime().exec(CMD);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    private static void mergeFiles(File[] files, File mergedFile) {
        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(mergedFile, true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = in.readLine()) != null) {
                    // out.write(aLine);
                    // out.newLine();
                    sb.append(aLine);
                }
                out.write(sb.toString());
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void merge(String caminhoFichExcerto, String PastaMusicaCaminho, String mergePath) throws IOException {
        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();

        File[] listaMerge = new File[2];
        listaMerge[0] = new File(caminhoFichExcerto);

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    listaMerge[1] = new File(file.getAbsolutePath());
                    File mergedFile = new File(mergePath + "\\excerto_" + file.getName());
                    mergeFiles(listaMerge, mergedFile);
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("Insera o caminho para a musica desejada: ");
        String input = "src\\main\\java\\grupori\\tai3entrega\\clientes\\Catarina-Motivação.wav";//c.nextLine();
        
        //System.out.println("Insira o caminho da pasta:");
        String PastaMusicaCaminho = "src\\main\\java\\grupori\\tai3entrega\\clientes";//sc.next();

        //copyAudio("src\\main\\java\\grupori\\tai3entrega\\k.wav", "src\\main\\java\\grupori\\tai3entrega\\k-edited.wav", 0, 3);
        
        System.out.println("Escolha a opção:");
        System.out.println("1 - Adcionar whitenoise");
        System.out.println("2 - Adcionar Brownnoise");
        System.out.println("3 - Adcionar Pinknoise");
        System.out.println("4 - Selecionar intervalo de tempo");

        while (true){ 
            String option = sc.next();
            if(option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4")){
                System.out.println("aplicar o sox");
                File inputFile = new File(input);
                sox(inputFile, Integer.parseInt(option));
                break;
            }
            System.out.println("Erro, escolha outro!");
        }
        
        System.out.println("A correr o programa do prof");
        gerarFreqs(PastaMusicaCaminho);

        System.out.println("Merge");
        merge("src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.freqs", PastaMusicaCaminho, "src\\main\\java\\grupori\\tai3entrega\\merges");

        System.out.println("Done");
    }  
}
