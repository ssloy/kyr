package kyr.exceptions;

public abstract class CompilerError extends RuntimeException {
    protected CompilerError(String message) {
        super(message);
    }
}
