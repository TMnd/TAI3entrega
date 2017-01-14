package grupori.tai3entrega;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class calculator {

    private Map<Double, String> tm = new TreeMap<>();

    public void ndc(String caminhoFichExcerto, String PastaMusicaCaminho, String mergePath) throws IOException {
        File excerto = new File(caminhoFichExcerto);
        System.out.println("excerto: " + caminhoFichExcerto + ":" + excerto.length() * 8);

        File VerAPasta = new File(PastaMusicaCaminho);
        File[] listOfFiles = VerAPasta.listFiles();

        File listaMerge = new File(mergePath);
        File[] listOfMergeFiles = listaMerge.listFiles();

        double calculo = 0;
        for (File mfile : listOfMergeFiles) {
            //System.out.println("lista do merge: " + mfile.getName());
            if (mfile.isFile()) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".7z") && mfile.getName().equals("excerto_" + file.getName()) && !file.getName().contains("excerto")) {
                        //System.out.println("lista ficheiros: " + file.getName());
                        //System.out.println("Tamanho combinação (" + mfile.getName() + "): " + mfile.length() * 8);
                        //System.out.println("Tamanho excerto (" + excerto.getName() + "): " + excerto.length() * 8);
                        //System.out.println("Tamanho comparação (" + file.getName() + "): " + file.length() * 8);
                        calculo = (((double) mfile.length() * 8) - (Math.min((double) file.length() * 8, (double) excerto.length() * 8))) / Math.max((double) file.length() * 8, (double) excerto.length() * 8);
                        //System.out.println(calculo);
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

}
