package org.example.core.Shell;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Parser.*;
import org.example.core.CommandRegistry;
import org.example.api.Event.IEventBus;
import org.example.core.Exception.UnknownCommandException;
import org.example.core.Parser.ASTNode;
import org.example.core.Parser.AndNode;
import org.example.core.Parser.CommandNode;
import org.example.core.Parser.OrNode;

import java.util.*;

public class ShellParser extends BaseParser{
    private final CommandRegistry registry;
    private final IEventBus bus;

    public ShellParser(CommandRegistry registry, IEventBus bus)
    {
        this.registry = registry;
        this.bus = bus;
    }

    public ASTNode parse(List<Token> tokens) throws Exception
    {
        setTokens(tokens);
        return parseExpression();
    }

    //ls && ls
    private ASTNode parseExpression() throws Exception {
        ASTNode left = parseSingleCommand();

        if (!isEmpty() && isOperator(peek())) {
            Token op = consume(); // "&&" ou "||"

            ASTNode right = parseExpression();

            if (op.value().equals("&&")) return new AndNode(left, right);
            if (op.value().equals("||")) return new OrNode(left, right);
        }

        return left;
    }

    private ASTNode parseSingleCommand() throws Exception {

        List<String> parts = new ArrayList<>();
        String command = parseSubCommand(parts);

        if (command == null)
            throw new UnknownCommandException(parts.isEmpty() ? "" : parts.getFirst());

        // tokens slice before &&, ||, |
        List<Token> argumentTokens = new ArrayList<>();
        while (!isEmpty() && !isOperator(peek())) {
            argumentTokens.add(consume());
        }

        return dispatch(command, argumentTokens);
    }

    private String parseSubCommand(List<String> parts) {
        String last = null;

        while (!isEmpty()) {
            String current = peek().value().toLowerCase();
            parts.add(current);
            String candidate = String.join("-", parts);

            if (registry.has(candidate)) {
                last = candidate;
                consume();
            } else {
                parts.removeLast();
                break;
            }
        }
        return last;
    }

    private ASTNode dispatch(
            String first, List<Token> tokens) throws Exception
    {
        //
        IParser parser = this.registry.getParser(first).get();
        if (parser == null) throw new UnknownCommandException(first);
        ICommand command = parser.parse(tokens);

        //
        IHandler handler = registry.getHandler(command.getClass()).get();
        if (handler == null) throw new Exception("Handler não encontrado para: " + first);

        return new CommandNode(command, handler, bus);
    }

}