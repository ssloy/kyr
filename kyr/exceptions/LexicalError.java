package kyr.exceptions;

public class LexicalError extends CompilerError {
    public LexicalError(String message) {
        super("ERREUR LEXICALE : " + message);
    }
}
