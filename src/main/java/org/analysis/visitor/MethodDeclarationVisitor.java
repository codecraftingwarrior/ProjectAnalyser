package org.analysis.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<>();
    int numberLineMethod = 0;

    public boolean visit(MethodDeclaration node) {
        methods.add(node);
        if(node.getBody() != null) numberLineMethod = node.getBody().toString().split("\n").length;
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethodDeclarations() {
        return methods;
    }

    public int getNumberLineMethod() { return numberLineMethod; }
}