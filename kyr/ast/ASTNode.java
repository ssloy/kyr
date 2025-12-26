package kyr.ast;

public abstract class ASTNode {
    protected int lineNumber;

    protected ASTNode(int n) {
        lineNumber = n;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public abstract void analyzeSemantics();
    public abstract String toMIPS();
}
