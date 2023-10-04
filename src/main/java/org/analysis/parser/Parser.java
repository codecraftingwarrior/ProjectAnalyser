package org.analysis.parser;

import org.analysis.visitor.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Parser {

    public static final String projectPath = "/Users/nat/Downloads/org.anonbnr.design_patterns-main";
    public static final String projectSourcePath = projectPath + "/src";
    public static final String jrePath = "/System/Library/Frameworks/JavaVM.framework/";
    private static final PackageVisitor packageVisit = new PackageVisitor();
    private static final TypeDeclarationVisitor classVisit = new TypeDeclarationVisitor();
    private static final MethodDeclarationVisitor methodVisit = new MethodDeclarationVisitor();
    private static final AttributeVisitor attributeVisit = new AttributeVisitor();
    public static int packageNumber = 0, classNumber = 0, methodNumber = 0, attributeNumber = 0, totalLineCounter = 0, totalLineMethod = 0, packageCounter = 0, classCounter = 0, methodCounter = 0, attributeCounter = 0;

    public static void main(String[] args) throws IOException {

        // Récupère tout les fichiers Java du répertoire
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
        totalLineCounter = lineCounter(javaFiles);

        // Lit Fichiers Java
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry, StandardCharsets.UTF_8);
            CompilationUnit parse = parse(content.toCharArray());

            // Visite Packages
            parse.accept(packageVisit);
            // Visite Classes
            parse.accept(classVisit);
            // Visite Méthodes
            parse.accept(methodVisit);
            //Nombre total de ligne dans les méthodes
            totalLineMethod += methodVisit.getNumberLineMethod();
            // Visite Attributs
            parse.accept(attributeVisit);
        }

        packageNumber = packageVisit.getPackages().size();
        classNumber = classVisit.getClasses().size();
        methodNumber = methodVisit.getMethodDeclarations().size();
        attributeNumber = attributeVisit.getAttributes().size();

        System.out.println("Nombre de Classes Totales: " + classNumber);
        System.out.println("Nombre de Ligne de Code de l'Application : " + totalLineCounter);
        System.out.println("Nombre de Méthodes Totales: " + methodNumber);
        System.out.println("Nombre de Packages Totales: " + packageNumber);
        System.out.println("Nombre Moyen de Méthodes par Classes : " + (methodNumber/classNumber));
        System.out.println("Nombre Total de Ligne de Code dans toutes les Méthodes : " + totalLineMethod);
        System.out.println("Nombre Moyen de Ligne de Code par Méthodes : " + (totalLineMethod/methodNumber));
        System.out.println("Nombre d'Attributs Totals : " + attributeNumber);
        System.out.println("Nombre Moyen d'Attributs par Classes : " + (attributeNumber/classNumber));
    }

    // Comptage des lignes de code de tout le fichier
    public static int lineCounter(@NotNull ArrayList<File> javaFiles) {
        int totalLines = 0;

        for (File file : javaFiles) totalLines += countLines(file);

        return totalLines;
    }

    // Comptage des lignes de code d'un fichier
    public static int countLines(File file) {
        int lines = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    // Lit tout les fichiers Java
    public static @NotNull ArrayList<File> listJavaFilesForFolder(final @NotNull File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();

        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    // Création de l'AST
    private static CompilationUnit parse(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = {projectSourcePath};
        String[] classpath = {jrePath};

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }

    // Extrait Informations des Packages
    public static void packageInfo(CompilationUnit parse) {
        PackageVisitor visitor = new PackageVisitor();
        parse.accept(visitor);

        for (String ignored : visitor.getPackages())
            packageCounter++;
    }

    // Extrait Informations des Classes
    public static void classInfo(@NotNull CompilationUnit parse) {
        TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
        parse.accept(visitor);

        for (TypeDeclaration ignored : visitor.getClasses())
            classCounter++;
    }

    // Extrait Informations des Méthodes
    public static void methodInfo(@NotNull CompilationUnit parse) {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        parse.accept(visitor);

        for (MethodDeclaration ignored : visitor.getMethodDeclarations())
            methodCounter++;
    }

    // Extrait Informations des Attributs
    public static void attributeInfo(@NotNull CompilationUnit parse) {
        AttributeVisitor visitor = new AttributeVisitor();
        parse.accept(visitor);

        for (FieldDeclaration ignored : visitor.getAttributes())
            attributeCounter++;
    }
}