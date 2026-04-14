package org.example.Parser;

import org.example.Command.BaseCommand;
import org.example.Command.Command;
import org.example.Lexer.Token;

import java.util.*;

public abstract class Parser {

    private Queue<Token> tokens = new LinkedList<>();
    public Set<String> allowedFlags = new HashSet<>();

    public Command Parser()
    {
        return new BaseCommand();
    }

    public Token peek()
    {
        Token token = this.tokens.peek();

        //returns an undefined token
        if(token == null)
            token = new Token();

        return token;
    }

    public Boolean peek(Token.TYPES type)
    {
        return isEqualType(type);
    }

    public Boolean peek(String value)
    {
        return (Objects.equals(peek().value, value));
    }

    public Token consume()
    {
        Token token = tokens.poll();
        if(token == null)
            token = new Token();

        return token;
    }

    public boolean expect(String value)
    {
        Token token = consume();
        return token.value.equalsIgnoreCase(value);
    }

    public boolean expect(Token.TYPES type)
    {
        Token token = consume();
        return Objects.equals(token.key, type);
    }

    public ParsedArgs consumeArgs() throws Exception {
        List<String> flags = new ArrayList<>();
        List<String> args = new ArrayList<>();

        while(!isEmpty())
        {
            Token t = peek();
            String value = consume().value;

            if(isEndOfFileToken(t)) break;

            if(isFlagPrefix(t.value) && !isFlagAllowed(value))
                throw new Exception("ERROR: flag not allowed: " + value);

            //
            if(isFlagPrefix(t.value))
                flags.add(value);
            else
                args.add(value);
        }

        return new ParsedArgs(flags, args);
    }

    private boolean isEqualType(Token.TYPES type) {
        return peek().key == type;
    }

    private boolean isEndOfFileToken(Token t) {
        return t.key == Token.TYPES.EOF;
    }

    private boolean isFlagPrefix(String value) {
        return value.startsWith("-");
    }

    public boolean isFlagAllowed(String flag)
    {
        return allowedFlags.contains(flag);
    }

    public void add(List<Token> tokens)
    {
        this.tokens.addAll(tokens);
    }

    public void add(Queue<Token> tokens)
    {
        this.setTokens(tokens);
    }

    public void reset(Queue<Token> tokens)
    {
        this.tokens.clear();
        this.add(tokens);
    }

    public void reset(List<Token> tokens)
    {
        this.tokens.clear();
        this.tokens.addAll(tokens);
    }

    public boolean isEmpty()
    {
        return this.tokens.isEmpty();
    }

    public int size()
    {
        return this.tokens.size();
    }

    public void setTokens(Queue<Token> tokens) {
        this.tokens = tokens;
    }

    public Queue<Token> getTokens() {
        return tokens;
    }

    //TODO: estrutura melhor, codigo de comandos espalhados
    public abstract Command parse() throws Exception;
}
