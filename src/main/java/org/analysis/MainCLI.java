package org.analysis;

import org.analysis.cli.AbstractCLI;
import org.analysis.cli.processor.IntegerInputProcessor;
import org.analysis.core.Analyzer;

import java.io.File;
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
            String projetPath = System.getProperty("user.dir");
            System.setProperty("org.graphstream.ui", "swing");
            System.out.printf("URL absolue de votre projet [%s] >>  ", Analyzer.getDefaultProjectDirPath());
            String projectPath = inputReader.readLine();
            Path path = Paths.get(projectPath);

            if (!Files.exists(path)) {
                System.err.println("Le dossier est introuvable");
                return;
            }
            analyzer = Analyzer.getInstance(projectPath);

            for (int i = 0; i <= 14; i++)
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
        stringBuilder.append("\n14. Visualiser le graphe d'appel de l'application.");
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
            case "4":
                System.out.println("Nombre de Packages Totales: " + analyzer.getPackageCount());
                break;
            case "5":
                System.out.println("Nombre Moyen de Méthodes par Classes : " + analyzer.getAverageMethodCountPerClass());
                break;
            case "6":
                System.out.println("Nombre Moyen de Ligne de Code par Méthodes : " + analyzer.getAverageLOCPerMethod());
                break;
            case "7":
                System.out.println("Nombre Moyen d'Attributs par Classes : " + analyzer.getAverageAttributeCountPerClass());
                break;
            case "8":
                analyzer.show10PercentClassWithHighestNumberOfMethods();
                break;
            case "9":
                analyzer.show10PercentClassWithHighestNumberOfAttributes();
                break;
            case "10":
                analyzer.showClassIn2PreviousCategories();
                break;
            case "11":
                IntegerInputProcessor xProcessor = new IntegerInputProcessor("Saisir la valeur de X : ");
                int x  = xProcessor.process();
                analyzer.showClassWithMoreThanXMethods(x);
                break;
            case "12":
                analyzer.show10PercentMethodsWithHighestLOC();
                break;
            case "13":
                System.out.println("\nLe nombre maximal de paramètres par rapport à toutes les méthodes de l’application : " + analyzer.getMaximumNumberOfParameter());
                break;
            case "14":
                analyzer.buildAndShowCallGraph();
                break;
        }

    }
}
