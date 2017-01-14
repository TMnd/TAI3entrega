/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.tai3entrega;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author joaoa
 */
public class generator {

    private Scanner sc = new Scanner(System.in);

    public void freqsGenerator(String ficheiro) {
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

    public void gerarFreqs(String PastaMusicaCaminho) {
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

    public void sox(File ficheiro, int option) {
        String CMD = null;
        try {
            switch (option) {
                case 1:
                    System.out.println("Adicionar whitenoise"); //adcionar uma opção para aumentar o "grayu"?
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe " + ficheiro.getAbsoluteFile() + " -p synth brownnoise vol 0.02 | sox.exe -m " + ficheiro.getAbsoluteFile() + " - ..//clientes//excerto.wav";
                    System.out.println(CMD);
                    break;
                case 2:
                    System.out.println("Adicionar brownnoise"); //adcionar uma opção para aumentar o "grayu"?
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe " + ficheiro.getAbsoluteFile() + " -p synth brownnoise vol 0.02 | sox.exe -m " + ficheiro.getAbsoluteFile() + " - ..//clientes//excerto.wav";
                    System.out.println(CMD);
                    break;
                case 3:
                    System.out.println("Adicionar pinknoise"); //adcionar uma opção para aumentar o "grayu"?
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe " + ficheiro.getAbsoluteFile() + " -p synth pinknoise vol 0.02 | sox.exe -m " + ficheiro.getAbsoluteFile() + " - ..//clientes//excerto.wav";
                    System.out.println(CMD);
                    break;
                case 4:
                    System.out.print("Insira o começo: ");
                    int musicaComeco = sc.nextInt();
                    System.out.print("Insira o limite: ");
                    int musicaSeleccao = sc.nextInt();
                    CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe " + ficheiro.getAbsoluteFile() + " ..//clientes//excerto.wav trim " + musicaComeco + " " + musicaSeleccao;
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

    public void mergeFiles(File[] files, File mergedFile) {
        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(mergedFile, true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        StringBuilder sb = null;
        for (File f : files) {
            sb = new StringBuilder();
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

    public void merge(String caminhoFichExcerto, String PastaMusicaCaminho, String mergePath) throws IOException {
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
}
