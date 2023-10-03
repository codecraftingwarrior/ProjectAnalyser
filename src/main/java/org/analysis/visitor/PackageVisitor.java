package org.analysis.visitor;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageVisitor extends ASTVisitor {

    //HashSet pour ne pas ajouter de doublons
    HashSet<String> packages = new HashSet<String>();

    public boolean visit(PackageDeclaration node) {
        packages.add(node.toString());
        return super.visit(node);
    }

    public HashSet<String> getPackages() {
        return packages;
    }
}
