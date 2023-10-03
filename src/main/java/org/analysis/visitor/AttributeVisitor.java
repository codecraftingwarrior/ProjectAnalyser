package org.analysis.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class AttributeVisitor extends ASTVisitor {

    List<FieldDeclaration> attributes = new ArrayList<>();

    public boolean visit(FieldDeclaration node) {
        attributes.add(node);
        return super.visit(node);
    }

    public List<FieldDeclaration> getAttributes() {
        return attributes;
    }
}
