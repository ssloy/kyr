package kyr;

public class LabelFactory {
    private static int counter = 0;

    public static String newLabel() {
        return "uniqueLabel" + (counter++);
    }
}

