package grupori.tai3entrega;

import java.io.*;
import java.util.Scanner;

public class main {

    //private static int contadorFreqs = 0;
    //private static int contadorCorrerArrayFiles = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        clean();
        System.out.println("Insera o caminho para a musica desejada: ");
        String input = sc.nextLine();

        //System.out.println("Insira o caminho da pasta:");
        String PastaMusicaCaminho = "src\\main\\java\\grupori\\tai3entrega\\clientes\\";//sc.next();

        generator t = new generator();
        compressor c = new compressor();
        calculator p = new calculator();
        String option = null;
        String compressor = null;
        
        System.out.println("Escolha o compressor:");
        System.out.println("1 - LZMA");
        System.out.println("2 - BZip2");
        
        while (true) {
            System.out.println("--");
            option = sc.next();
            if (option.equals("1")) {
                compressor = ".7z";
                break;
            }else if(option.equals("2")){
                compressor = ".bz2";
                break;
            }
            System.out.println("Erro, escolha outro!");
        }

        //copyAudio("src\\main\\java\\grupori\\tai3entrega\\k.wav", "src\\main\\java\\grupori\\tai3entrega\\k-edited.wav", 0, 3);
        System.out.println("Escolha a opção:");
        System.out.println("1 - Adicionar Whitenoise");
        System.out.println("2 - Adicionar Brownnoise");
        System.out.println("3 - Adicionar Pinknoise");
        System.out.println("4 - Selecionar intervalo de tempo");

        while (true) {
            System.out.println("--");
            option = sc.next();
            if (option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4")) {
                System.out.println("aplicar o sox");
                File inputFile = new File(input);
                t.sox(inputFile, Integer.parseInt(option));
                break;
            }
            System.out.println("Erro, escolha outro!");
        }

        System.out.println("A correr o programa do prof");
        t.gerarFreqs(PastaMusicaCaminho);

        System.out.println("Merge");
        t.merge("src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.freqs", "src\\main\\java\\grupori\\tai3entrega\\clientes\\", "src\\main\\java\\grupori\\tai3entrega\\merges");

        System.out.println("A comprimir os ficheiros:");
        c.criarFicheirosComp(PastaMusicaCaminho);

        System.out.println("A calcular ndc");
        p.ndc("src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.7z", PastaMusicaCaminho, "src\\main\\java\\grupori\\tai3entrega\\merges",compressor);

        //devolver resultado menor + musica
        System.out.println("Done");
    }

    private static void clean() {
        File VerAPastaMusica = new File("src\\main\\java\\grupori\\tai3entrega\\clientes");
        File[] listOfFilesMusica = VerAPastaMusica.listFiles();
        File VerAPastaMerge = new File("src\\main\\java\\grupori\\tai3entrega\\merges\\");
        File[] listOfFilesMerge = VerAPastaMerge.listFiles();

        for (File file : listOfFilesMusica) {
            if (file.isFile()) {
                if ((!file.getName().endsWith(".wav") && !file.getName().endsWith("exe")) || file.getName().contains("excerto")) {
                    file.delete();
                }
            }
        }
        for (File file : listOfFilesMerge) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
}
