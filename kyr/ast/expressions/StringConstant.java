package kyr.ast.expressions;

import kyr.LabelFactory;

public class StringConstant extends Constant {
    protected String mipsLabel;

    public StringConstant(String text, int n) {
        super(text, n);
        mipsLabel = LabelFactory.newLabel();
    }

    @Override
    public void analyzeSemantics() {
        // nothing to do here, no string manipulation in Kyr
    }

    @Override
    public String toMIPS() {
        return String.format("""
                    la $v0, %s
                """, mipsLabel);
    }

    public String toMIPS_DataSegment() {
        return String.format("%s: .asciiz \"%s\"\n",
            mipsLabel,
            cst.replace("\\", "\\\\")
               .replace("\t", "\\t")
               .replace("\b", "\\b")
               .replace("\n", "\\n")
               .replace("\r", "\\r")
               .replace("\f", "\\f")
               .replace("\'", "\\'")
               .replace("\"", "\\\""));
    }
}
