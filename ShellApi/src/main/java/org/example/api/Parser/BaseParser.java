package org.example.api.Parser;

import org.example.api.Command.ICommand;

import java.util.*;

public abstract class BaseParser {
    protected Queue<Token> tokens = new ArrayDeque<>();
    private final Set<String> operators = Set.of("&&", "||", "|");

    public void setTokens(List<Token> tokens) {
        this.tokens = new ArrayDeque<>(tokens);
    }

    public Token peek() {
        Token t = tokens.peek();
        return (t != null) ? t : new Token(TokenType.EOF, "");
    }

    public Token consume() {
        Token t = tokens.poll();
        return (t != null) ? t : new Token(TokenType.EOF, "");
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    public boolean expect(String value){
        Token t = consume();
        return t.value().equalsIgnoreCase(value);
    }

    public boolean expect(TokenType type)
    {
        Token t = consume();
        return Objects.equals(t.key(), type);
    }

    protected boolean isOperator(Token t) {
        String val = t.value();
        return operators.contains(val);
    }
}
