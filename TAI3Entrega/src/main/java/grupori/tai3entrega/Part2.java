package grupori.tai3entrega;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Part2 {
    private static Map<Double, String> tm = new TreeMap<>();
    
    private static void ndc(String caminhoFichExcerto, String PastaMusicaCaminho, String mergePath) throws IOException {
        File excerto = new File(caminhoFichExcerto);
        System.out.println("excerto: " + caminhoFichExcerto + ":" + excerto.length());

        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();

        File listaMerge = new File(mergePath);
        File[] listOfMergeFiles = listaMerge.listFiles();

        for (File mfile : listOfMergeFiles) {
            System.out.println("lista do merge: " + mfile.getName());
            if (mfile.isFile()) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".7z") && mfile.getName().equals("excerto_" + file.getName()) && !file.getName().contains("excerto")) {
                        System.out.println("lista ficheiros: " + file.getName());
                        System.out.println("Tamanho combinação ("+mfile.getName()+"): " + mfile.length());
                        System.out.println("Tamanho excerto ("+excerto.getName()+"): " + excerto.length());
                        System.out.println("Tamanho comparação ("+file.getName()+"): " + file.length());
                        double calculo = ((double) mfile.length() - Math.min((double) file.length(), (double) excerto.length())) / Math.max((double) file.length(), (double) excerto.length());
                        System.out.println(calculo);
                        //tm.put(calculo, file.getName());*/
                    }
                }
            }
        }

        /* teste */
        /*for (Map.Entry<Double, String> entrySet : tm.entrySet()) {
            Double key = entrySet.getKey();
            String value = entrySet.getValue();

            System.out.println(key + " :: " + value);
        }*/
    }

    public static void main(String[] args) throws IOException {
        String PastaMusicaCaminho = "src\\main\\java\\grupori\\tai3entrega\\clientes";//sc.next();
        
        System.out.println("A calcular ndc");
        ndc("src\\main\\java\\grupori\\tai3entrega\\clientes\\excerto.7z", PastaMusicaCaminho, "src\\main\\java\\grupori\\tai3entrega\\merges");
    }
    
}
