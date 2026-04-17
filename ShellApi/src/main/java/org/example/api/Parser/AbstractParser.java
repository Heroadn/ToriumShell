package org.example.api.Parser;
import org.example.api.Command.ICommand;

import java.util.*;

public abstract class AbstractParser implements IParser {

    protected Queue<Token> tokens = new LinkedList<>();
    public Set<String> allowedFlags = new HashSet<>();

    public ICommand parse(List<Token> tokens) throws Exception {
        this.tokens = new ArrayDeque<>(tokens);
        return parse();
    }

    protected abstract ICommand parse() throws Exception;

    public Token peek()
    {
        Token token = this.tokens.peek();
        if(token == null) token = new Token(Token.TYPES.EOF, "");

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

    public boolean expect(String value){
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

    public boolean isEmpty()
    {
        return this.tokens.isEmpty();
    }

    public int size()
    {
        return this.tokens.size();
    }

    public Queue<Token> getTokens() {
        return tokens;
    }
}
