/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.tai3entrega;

import java.io.*;
import SevenZip.Compression.LZMA.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class main {

    private static int contadorFreqs = 0;
    private static int contadorCorrerArrayFiles = 0;
    Map<Integer, String> tm = new TreeMap<Integer,String>();

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
    
    private static void sox(File ficheiro, int start , int end) {
        //String nomeFicheiro[] = ficheiro.split("\\.");
         System.out.println(ficheiro);
        try {
            String CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega//sox && sox.exe "+ ficheiro +" ..//clientes//excerto.wav trim "+start+" "+end;
            System.out.println(CMD);
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
                    contadorFreqs++;
                }
            }
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

     
        
        for (File f : files) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = in.readLine()) != null) {
                    out.write(aLine);
                    out.newLine();
                }

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

    // LMZA
    private static void comprimir(String ficheiro) throws FileNotFoundException, IOException {
        //Ler input para ser comprimido
        File inputToCompress = new File(ficheiro);
        BufferedInputStream inStream = new BufferedInputStream(new java.io.FileInputStream(inputToCompress));
        //Criar o ficheiro 7zip
        String nomeFicheiro[] = ficheiro.split("\\.");
        File compressedOutput = new File(nomeFicheiro[0] + ".7z");
        BufferedOutputStream outStream = new BufferedOutputStream(new java.io.FileOutputStream(compressedOutput));
        // Criar p objecto encoder do LZMA / Header Information */
        Encoder encoder = new Encoder();
        encoder.SetAlgorithm(2);
        encoder.SetDictionarySize(8388608);
        encoder.SetNumFastBytes(128);
        encoder.SetMatchFinder(1);
        encoder.SetLcLpPb(3, 0, 2);
        encoder.SetEndMarkerMode(false);
        encoder.WriteCoderProperties(outStream);
        long fileSize;
        fileSize = inputToCompress.length();
        for (int i = 0; i < 8; i++) {
            outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
        }
        // Escrever dados compressados para ficheiro
        encoder.Code(inStream, outStream, -1, -1, null);

        outStream.flush();
        outStream.close();
        inStream.close();
    }

    private static void criariFicheirosComp(String PastaMusicaCaminho) throws IOException {
        File VerAPastaMusica = new File(PastaMusicaCaminho);
        File[] listOfFilesMusica = VerAPastaMusica.listFiles();
        File VerAPastaMerge = new File("src\\main\\java\\grupori\\tai3entrega\\merges\\");
        File[] listOfFilesMerge = VerAPastaMerge.listFiles();

        for (File file : listOfFilesMusica) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    comprimir(file.getPath());
                    file.delete();
                }
            }
        }
         for (File file : listOfFilesMerge) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    comprimir(file.getPath());
                    file.delete();
                }
            }
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
                    File mergedFile = new File(mergePath + "\\exerto_"+file.getName());
                    mergeFiles(listaMerge, mergedFile);
                }
            }
        }
    }
    
    private static void ndc(String caminhoFichExcerto, String PastaMusicaCaminho, String mergePath) throws IOException {
        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();
        
        File[] listaMerge = new File[2];
        listaMerge[0] = new File(caminhoFichExcerto);

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    listaMerge[1] = new File(file.getAbsolutePath());
                    File mergedFile = new File(mergePath + "\\exerto_"+file.getName());
                    mergeFiles(listaMerge, mergedFile);
                    file.delete();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Insera o caminho para a musica desejada: ");
        String input = "C:\\Users\\joaoa\\Desktop\\Tai\\TAI3entrega\\TAI3Entrega\\src\\main\\java\\grupori\\tai3entrega\\clientes\\Catarina-Deixaoventopassar.wav";//c.nextLine();
        System.out.println("Insira o começo:");
        int musicaComeco = sc.nextInt();
        System.out.println("Insira o limite:");
        int musicaSeleccao = sc.nextInt();

        //System.out.println("Insira o caminho da pasta:");
        String PastaMusicaCaminho = "src\\main\\java\\grupori\\tai3entrega\\clientes";//sc.next();
      
        //copyAudio("src\\main\\java\\grupori\\tai3entrega\\k.wav", "src\\main\\java\\grupori\\tai3entrega\\k-edited.wav", 0, 3);
        System.out.println("A cortar musica");
        //copyAudio(input,"src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.wav",musicaComeco,musicaSeleccao);
        File inputFile = new File(input);
        sox(inputFile,musicaComeco,musicaSeleccao);
        
        System.out.println("A correr o programa do prof");
        gerarFreqs(PastaMusicaCaminho);
        
        System.out.println("Merge");
        merge("src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.wav",PastaMusicaCaminho,"src\\main\\java\\grupori\\tai3entrega\\merges");

        System.out.println("A comprimir os ficheiros:");
        criariFicheirosComp(PastaMusicaCaminho);
        
        System.out.println("A calcular ndc");
        
        //devolver resultado menor + musica
        
        System.out.println("Done");
    }
}
