package kyr.exceptions;

public class SemanticError extends CompilerError {
    public SemanticError(String message) {
        super("ERREUR SEMANTIQUE : " + message);
    }
}
