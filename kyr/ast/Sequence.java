package kyr.ast;

import kyr.ast.statements.Statement;
import java.util.ArrayList;

public class Sequence extends ASTNode {
    protected ArrayList<Statement> statements = new ArrayList<>();

    public Sequence(int n) {
        super(n);
    }

    public void add(Statement s) {
        statements.add(s);
    }

    @Override
    public void analyzeSemantics() {
        throw new UnsupportedOperationException("semantic analysis is not implemented");
    }

    @Override
    public String toMIPS() {
        StringBuilder sb = new StringBuilder("");
        for (Statement s : statements)
            sb.append(s.toMIPS());
        return sb.toString();
    }
}
