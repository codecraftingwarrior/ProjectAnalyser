package org.analysis.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageVisitor extends ASTVisitor {

    HashSet<String> packages = new HashSet<String>();

    public boolean visit(PackageDeclaration node) {
        packages.add(node.toString());
        return super.visit(node);
    }

    public HashSet<String> getPackages() {
        return packages;
    }
}
