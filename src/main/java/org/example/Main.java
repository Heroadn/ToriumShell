package org.example;

import org.example.Handler.ShellHandler;
import org.example.IO.ShellConsole;
import org.example.Lexer.Lexer;
import org.example.Parser.ShellParser;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();
        StringBuilder buffer = new StringBuilder();

        ShellContext context = new ShellContext();
        ShellConsole console = new ShellConsole();

        ShellHandler handler = new ShellHandler(context, console);
        ShellParser  parser = new ShellParser();
        context.setCurrentDir(Path.of(System.getProperty("user.home")));

        while(context.isRunning()){
            IO.print(context.getCurrentDir() + " $ ");
            String line = scanner.nextLine();
            appendLine(buffer, line);

            if(isExitCommand(line))
            {
                context.setRunning(false);
                return;
            }

            try {
                execute(buffer, lexer, handler, parser);
            } catch (Exception e) {
                //throw new RuntimeException(e);
                IO.println("ERRO: " + e);
            }
        }

        scanner.close();
    }

    private static void appendLine(StringBuilder buffer, String line) {
        buffer.append(line);
    }

    private static void execute(
            StringBuilder buffer,
            Lexer lexer,
            ShellHandler handler,
            ShellParser parser) throws Exception {

        lexer.setInput(buffer.toString());
        parser.add(lexer.tokenizer());

        handler.execute(parser.parse());
        buffer.setLength(0);
    }

    private static boolean isExitCommand(String line) {
        return line.equalsIgnoreCase("EXIT");
    }

    private static boolean isCompleteCommand(String line) {
        return line.contains(";");
    }
}
