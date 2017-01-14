/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupori.tai3entrega;

import SevenZip.Compression.LZMA.Encoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.codehaus.plexus.archiver.bzip2.CBZip2OutputStream;

/**
 *
 * @author Mariana
 */
public class compressor {

    public void criarFicheirosComp(String PastaMusicaCaminho) throws IOException {
        File VerAPastaMusica = new File(PastaMusicaCaminho);
        File[] listOfFilesMusica = VerAPastaMusica.listFiles();
        File VerAPastaMerge = new File("src\\main\\java\\grupori\\tai3entrega\\merges\\");
        File[] listOfFilesMerge = VerAPastaMerge.listFiles();

        for (File file : listOfFilesMusica) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    comprimir(file.getPath());
                    compressGzipFile(file.getPath());
                    compressBZip2File(file.getPath());
                    //file.delete();
                }
            }
        }
        for (File file : listOfFilesMerge) {
            if (file.isFile()) {
                if (file.getName().endsWith(".freqs")) {
                    comprimir(file.getPath());
                    compressGzipFile(file.getPath());
                    compressBZip2File(file.getPath());
                    //file.delete();
                }
            }
        }
    }

    // LMZA
    public void comprimir(String ficheiro) throws FileNotFoundException, IOException {
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
    public void compressGzipFile(String ficheiro) {
        String nomeFicheiro[] = ficheiro.split("\\.");
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            FileOutputStream fos = new FileOutputStream(nomeFicheiro[0] + ".gz");
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
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

    // BZIP
    public void compressBZip2File(String ficheiro) throws IOException {
        String nomeFicheiro[] = ficheiro.split("\\.");
        FileInputStream fis = new FileInputStream(ficheiro);
        FileOutputStream fos = new FileOutputStream(nomeFicheiro[0] + ".bz2");

        fos.write("BZ".getBytes());
        CBZip2OutputStream out = new CBZip2OutputStream(fos);
        int content;
        while ((content = fis.read()) != -1) {
            out.write(content);
        }
        out.flush();
        out.close();
    }
}
