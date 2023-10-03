package org.analysis;

import org.analysis.visitor.ClassVisitor;
import org.analysis.visitor.MethodDeclarationVisitor;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Analyzer {
    private static String projectPath;
    private static String projectSourcePath;
    private static final String jrePath = "/System/Library/Frameworks/JavaVM.framework/";

    List<File> javaFiles = new ArrayList<>();

    private static Analyzer instance = null;

    private Analyzer(String projectUrl) {
        projectPath = projectUrl;
        projectSourcePath = projectPath + "/src";
        final File folder = new File(projectSourcePath);
        javaFiles = listJavaFilesForFolder(folder);
    }

    public static Analyzer getInstance( String projectPath) {
        if(instance == null)
            instance = new Analyzer(projectPath);

        return instance;
    }

    private @NotNull ArrayList<File> listJavaFilesForFolder(final @NotNull File folder) {
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
    private CompilationUnit parse(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = { projectSourcePath };
        String[] classpath = {jrePath};

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }

    public int getClassCount() throws IOException {
        int classCounter = 0;
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry, StandardCharsets.UTF_8);
            CompilationUnit ast = parse(content.toCharArray());

            ClassVisitor visitor = new ClassVisitor();
            ast.accept(visitor);

            classCounter += visitor.getClasses().size();
        }

        return classCounter;
    }

    public int getLineCount(){
        int totalLines = 0;

        for (File file : javaFiles)
            totalLines += countLines(file);

        return totalLines;
    }

    private static int countLines(File file) {
        int lines = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null)
                lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public int getMethodCount() throws IOException {
        int methodCounter = 0;
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry, StandardCharsets.UTF_8);
            CompilationUnit ast = parse(content.toCharArray());

            MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
            ast.accept(visitor);

            for (MethodDeclaration ignored : visitor.getMethodDeclarations())
                methodCounter++;
        }

        return methodCounter;
    }
}
