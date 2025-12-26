package kyr.ast.expressions;

public abstract class Constant extends Expression {
    protected String cst;

    protected Constant(String text, int n) {
        super(n);
        cst = text;
    }
}
