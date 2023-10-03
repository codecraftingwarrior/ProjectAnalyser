package org.analysis;

import org.analysis.cli.AbstractCLI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainCLI extends AbstractCLI {

    private static Analyzer analyzer;
    private static Set<String> validChoices = new LinkedHashSet<>();

    public MainCLI() {
    }

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        try {
            System.out.print("URL absolue de votre projet >>  ");
            String projectPath = inputReader.readLine();
            Path path = Paths.get(projectPath);

            if (!Files.exists(path)) {
                System.err.println("Le dossier est introuvable");
                return;
            }
            analyzer = Analyzer.getInstance(projectPath);

            for (int i = 0; i <= 13; i++)
                validChoices.add(String.valueOf(i));

            MainCLI mainCLI = new MainCLI();
            mainCLI.run();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void mainMenu() {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println();
        System.out.print("---------------------------------\n");
        stringBuilder.append(String.format("%s. QUIT", QUIT));
        stringBuilder.append("\n1. Nombre de classes de l’application.");
        stringBuilder.append("\n2. Nombre de lignes de code de l’application.");
        stringBuilder.append("\n3. Nombre total de méthodes de l’application.");
        stringBuilder.append("\n4. Nombre total de packages de l’application.");
        stringBuilder.append("\n5. Nombre moyen de méthodes par classe.");
        stringBuilder.append("\n6. Nombre moyen de lignes de code par méthode.");
        stringBuilder.append("\n7. Nombre moyen d’attributs par classe.");
        stringBuilder.append("\n8. Les 10% des classes qui possèdent le plus grand nombre de méthodes.");
        stringBuilder.append("\n9. Les 10% des classes qui possèdent le plus grand nombre d’attributs.");
        stringBuilder.append("\n10. Les classes qui font partie en même temps des deux catégories précédentes.");
        stringBuilder.append("\n11. Les classes qui possèdent plus de X méthodes (la valeur de X est donnée).");
        stringBuilder.append("\n12. Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe).");
        stringBuilder.append("\n13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l’application.");
        stringBuilder.append("\n---------------------------------");

        System.out.println(stringBuilder);
    }

    @Override
    protected void processUserInput(String userInput) throws IOException {

        if (userInput.equals(QUIT)) {
            System.out.println("À très bientôt");
            return;
        }

        if(!validChoices.contains(userInput)) {
            System.out.println("Valeur saisie incorrect merci de réssayer");
            System.out.println();
            return;
        }

        switch(userInput) {
            case "1":
                System.out.printf("Nombre de Classes Totales: " + analyzer.getClassCount());
                break;
            case "2":
                System.out.println("Nombre de Ligne de Code de l'Application : " + analyzer.getLineCount());
                break;
            case "3":
                System.out.println("Nombre de Méthodes Totales: " + analyzer.getMethodCount());
                break;
        }

    }
}
