/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.tai3entrega;

import java.io.*;
import SevenZip.Compression.LZMA.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class main {

    private static int contadorFreqs = 0;
    private static int contadorCorrerArrayFiles = 0;

    private static void freqsGenerator(String ficheiro) {
        String nomeFicheiro[] = ficheiro.split("\\.");
        try {
            String CMD = "cmd.exe /c cd src//main//java//grupori//tai3entrega && GetMaxFreqs.exe -w " + nomeFicheiro[0] + ".freqs " + nomeFicheiro[0] + ".wav";
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

    private static void juntarFicheiros(String PastaMusicaCaminho, String PathParaFichMerged) throws InterruptedException {
        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listarFicheiros = VerAPasta.listFiles();

        File[] files = new File[contadorFreqs];

        for (File file : listarFicheiros) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    files[contadorCorrerArrayFiles] = new File(file.getAbsolutePath());
                    contadorCorrerArrayFiles++;
                }
            }
        }
        File mergedFile = new File(PathParaFichMerged);
        mergeFiles(files, mergedFile);
    }

    private static void comprimir(String ficheiro) throws FileNotFoundException, IOException {
        //Ler input para ser comprimido
        File inputToCompress = new File(ficheiro);
        BufferedInputStream inStream = new BufferedInputStream(new java.io.FileInputStream(inputToCompress));
        //Criar o ficheiro 7zip
        String nomeFicheiro[] = ficheiro.split("\\.");
        File compressedOutput = new File(nomeFicheiro[0] + "_c.7z");
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
        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    comprimir(file.getPath());
                }
            }
        }
    }

    /*
     * referir no relatório que foi baseado neste tópico: https://goo.gl/iJKpTb
     */
    public static void copyAudio(String sourceFileName, String destinationFileName, int startSecond, int secondsToCopy) {
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;
        try {
            File file = new File(sourceFileName);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(file);
            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
            inputStream.skip(startSecond * bytesPerSecond);
            long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
            shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File(destinationFileName);
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (shortenedStream != null) {
                try {
                    shortenedStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //Scanner sc = new Scanner(System.in);
        //System.out.println("Insira o caminho da pasta:");
        String PastaMusicaCaminho = "src\\main\\java\\grupori\\tai3entrega";//sc.next();
        String PathParaFichMerged = "src\\main\\java\\grupori\\tai3entrega\\merge.freqs"; //Caso queiram meter noutro sitio

        //copyAudio("src\\main\\java\\grupori\\tai3entrega\\k.wav", "src\\main\\java\\grupori\\tai3entrega\\k-edited.wav", 0, 3);

        System.out.println("A correr o programa do prof");
        gerarFreqs(PastaMusicaCaminho);

        System.out.println("Merge dos ficheiros .freqs");
        juntarFicheiros(PastaMusicaCaminho, PathParaFichMerged);

        System.out.println("A comprimir os ficheiros:");
        criariFicheirosComp(PastaMusicaCaminho);

        System.out.println("Done");
    }
}
