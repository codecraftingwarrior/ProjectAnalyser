package org.analysis.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<>();

    public boolean visit(MethodDeclaration node) {
        methods.add(node);
        return super.visit(node);
        //condition body pas nul
        //node.getBody().toString().split("\n")
    }



    public List<MethodDeclaration> getMethods() {
        return methods;
    }
}