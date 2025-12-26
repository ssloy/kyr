package kyr.ast.statements;

import kyr.ast.expressions.*;

public class Print extends Statement {
    protected Expression exp;

    public Print(Expression e, int n) {
        super(n);
        exp = e;
    }

    @Override
    public void analyzeSemantics() {
        throw new UnsupportedOperationException("semantic analysis is not implemented");
    }

    @Override
    public String toMIPS() {
        return exp.toMIPS() + String.format("""
                    move $a0, $v0
                    li $v0, %d           # set the syscall code for printing
                    syscall
                """, StringConstant.class.isInstance(exp) ? 4 : 1);
    }
}
