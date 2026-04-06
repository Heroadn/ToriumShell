package org.example.Parser;

import org.example.Lexer.Token;

import java.util.*;

public class Parser {

    private List<Token> tokens;
    protected Set<String> allowedFlags = new HashSet<>();
    private int pos;

    public Parser() {
        this.pos = 0;
        this.tokens = new ArrayList<>();
    }

    public Token peek()
    {
        Token token = new Token();

        if(pos >= this.tokens.size())
            return token;

        token = this.tokens.get(pos);
        return token;
    }

    public Boolean peek(Token.TYPES type)
    {
        return (peek().key == type);
    }

    public Boolean peek(String value)
    {
        return (Objects.equals(peek().value, value));
    }

    public Token consume()
    {
        Token token = new Token();

        if(pos >= this.tokens.size())
            return token;

        token = this.tokens.get(pos);
        pos++;
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

    protected List<String> consumeArgs() throws Exception {
        List<String> args = new ArrayList<>();
        while(!isEmpty())
        {
            Token t = peek();
            if(t.key == Token.TYPES.EOF) break;
            String value = consume().value;

            if(isFlagPrefix(value) && !isFlagAllowed(value))
                throw new Exception("ERROR: flag not allowed: " + value);

            args.add(value);
        }
        return args;
    }

    private boolean isFlagPrefix(String value) {
        return value.startsWith("-");
    }

    public boolean isFlagAllowed(String flag)
    {
        return allowedFlags.contains(flag);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    public boolean isEmpty()
    {
        return this.pos >= this.tokens.size();
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
