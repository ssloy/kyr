package kyr.ast;

import kyr.ast.Sequence;
import kyr.symtable.SymbolTable;

public class Program extends ASTNode {
    protected Sequence sequence;

    public Program(Sequence s) {
        super(-1);
        sequence = s;
    }

    @Override
    public void analyzeSemantics() {
        throw new UnsupportedOperationException("semantic analysis is not implemented");
    }

    @Override
    public String toMIPS() {
        StringBuilder sb = new StringBuilder("");
        sb.append("""
                .data
                    vrai: .asciiz "vrai"
                    faux: .asciiz "faux"
                 """);
        sb.append(SymbolTable.getInstance().toMIPS_DataSegment());
        sb.append("""
                .text
                main:
                """);
        sb.append(sequence.toMIPS());
        sb.append("""
                end:
                    li $v0, 10          # terminate execution
                syscall
                """);
        return sb.toString();
    }
}
