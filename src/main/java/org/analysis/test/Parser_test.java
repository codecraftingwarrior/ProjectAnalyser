package org.analysis.test;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Parser_test {

    public static void main(String[] args){
        ASTParser parser = ASTParser.newParser(AST.JLS4);

        // La source est une chaîne de caractère et non un fichier comme dans le parser
        parser.setSource("public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }".toCharArray());
        //parser.setSource("/*abc*/".toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        //ASTNode node = parser.createAST(null);

        //Création de l'AST
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // Création du Visiteur qui visite seulement les variables
        cu.accept(new ASTVisitor() {

            Set names = new HashSet();

            // On dit de visiter seulement les déclarations de variables
            public boolean visit(VariableDeclarationFragment node) {
                SimpleName name = node.getName();
                this.names.add(name.getIdentifier());
                System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));
                return false; // do not continue to avoid usage info
            }

            // Sortie système pour afficher les variables visitées
            public boolean visit(SimpleName node) {
                if (this.names.contains(node.getIdentifier())) {
                    System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
                }
                return true;
            }

        });
    }
}