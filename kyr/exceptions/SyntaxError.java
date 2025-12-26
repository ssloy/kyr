package kyr.exceptions;

public class SyntaxError extends CompilerError {
    public SyntaxError(String message) {
        super("ERREUR SYNTAXIQUE : " + message);
    }
}

