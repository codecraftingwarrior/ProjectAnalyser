package org.analysis.core;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.LinkedHashSet;
import java.util.Set;

public class CallGraph {
    private final Set<MethodDeclaration> nodes;
    private final Set<MethodInvocation> edges;

    public CallGraph() {
        nodes = new LinkedHashSet<>();
        edges = new LinkedHashSet<>();
    }



}
