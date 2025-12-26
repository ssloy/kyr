package kyr;

import kyr.parser.*;
import kyr.ast.ASTNode;
import kyr.exceptions.CompilerError;

import java.io.*;

public class Kyr {
    public Kyr(String kyrFile) {
        try {
            Parser parser = new Parser(new Lexer(new FileReader(kyrFile)));
            ASTNode ast = (ASTNode)parser.parse().value;

            ast.analyzeSemantics();
            System.out.println("COMPILATION OK");

            String asmFile = kyrFile.replaceAll("[.]kyr", ".mips");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(asmFile)));
            pw.println(ast.toMIPS());
            pw.close();
        } catch (FileNotFoundException e) {
            System.err.println("Echec d'ouverture du fichier " + kyrFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Nombre incorrect d'arguments :");
            System.err.println("\tjava -jar kyr.jar source.kyr");
            System.exit(1);
        }
        new Kyr(args[0]);
    }
}
