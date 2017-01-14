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
import java.util.zip.GZIPOutputStream;

public class main {

    private static int contadorFreqs = 0;
    private static int contadorCorrerArrayFiles = 0;
    private static Map<Double, String> tm = new TreeMap<>();
    static Scanner sc = new Scanner(System.in);

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

    //GZIP
    private static void compressGzipFile(String ficheiro) {
        String nomeFicheiro[] = ficheiro.split("\\.");
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            FileOutputStream fos = new FileOutputStream(nomeFicheiro[0] + ".gz");
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    compressGzipFile(file.getPath());
                    file.delete();
                }
            }
        }
        for (File file : listOfFilesMerge) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    comprimir(file.getPath());
                    compressGzipFile(file.getPath());
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
                    File mergedFile = new File(mergePath + "\\excerto_" + file.getName());
                    mergeFiles(listaMerge, mergedFile);
                }
            }
        }
    }

    private static void ndc(String caminhoFichExcerto, String PastaMusicaCaminho, String mergePath) throws IOException {
        File excerto = new File(caminhoFichExcerto);
        System.out.println("excerto: " + caminhoFichExcerto + ":" + excerto.length());

        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();

        File listaMerge = new File(mergePath);
        File[] listOfMergeFiles = listaMerge.listFiles();

        for (File mfile : listOfMergeFiles) {
            if (mfile.isFile()) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".7z") && mfile.getName().equals("excerto_" + file.getName()) && !file.getName().contains("excerto")) {
                        //System.out.println(file.getName() + ":" + mfile.getName());
                        //System.out.println(mfile.length() + ":" + file.length() + ":" + excerto.length());
                        double calculo = ((double) mfile.length() - Math.min((double) file.length(), (double) excerto.length())) / Math.max((double) file.length(), (double) excerto.length());
                        tm.put(calculo, file.getName());
                    }
                }

            }
        }

        /* teste */
        for (Map.Entry<Double, String> entrySet : tm.entrySet()) {
            Double key = entrySet.getKey();
            String value = entrySet.getValue();

            System.out.println(key + " :: " + value);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
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
            System.out.println("--");
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

        System.out.println("A comprimir os ficheiros:");
        criariFicheirosComp(PastaMusicaCaminho);

        System.out.println("A calcular ndc");
        ndc("src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.7z", PastaMusicaCaminho, "src\\main\\java\\grupori\\tai3entrega\\merges");

        //devolver resultado menor + musica
        System.out.println("Done");
    }
}
