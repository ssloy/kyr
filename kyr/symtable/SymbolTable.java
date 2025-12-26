package kyr.symtable;

import kyr.ast.expressions.StringConstant;
import java.util.ArrayList;

public class SymbolTable {
    static final SymbolTable instance = new SymbolTable(); // singleton pattern
    protected ArrayList<StringConstant> strings = new ArrayList<StringConstant>();

    private SymbolTable() {
    }

    public static SymbolTable getInstance() {
        return instance;
    }

    public void add(StringConstant c) {
        strings.add(c);
    }

    public String toMIPS_DataSegment() {
        StringBuilder sb = new StringBuilder("");
        for (StringConstant c : strings)
            sb.append(c.toMIPS_DataSegment());
        return sb.toString();
    }
}

