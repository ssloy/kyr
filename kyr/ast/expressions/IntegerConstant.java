package kyr.ast.expressions;

public class IntegerConstant extends Constant {
    public IntegerConstant(String text, int n) {
        super(text, n);
    }

    @Override
    public void analyzeSemantics() {
        throw new UnsupportedOperationException("semantic analysis is not implemented");
    }

    @Override
    public String toMIPS() {
        return String.format("""
                    li $v0, %s
                """, cst);
    }
}
