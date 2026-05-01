package org.example.api.Lexer;

import org.example.api.Parser.Token;
import org.example.api.Parser.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//scanner? never heard of it lol
public class Lexer {
    private CharStream stream;

    private static final Map<Character, TokenType> SYMBOLS = Map.ofEntries(
            Map.entry('*', TokenType.ASTERISK),
            Map.entry('=', TokenType.OPERATOR),
            Map.entry('(', TokenType.LPAREM),
            Map.entry(')', TokenType.RPAREM),
            Map.entry(',', TokenType.COMMA),
            Map.entry(';', TokenType.SEMICOLON),
            Map.entry('.', TokenType.DOT),
            Map.entry('>', TokenType.OPERATOR),
            Map.entry('<', TokenType.OPERATOR),
            Map.entry('!', TokenType.OPERATOR),
            Map.entry('\\', TokenType.BACK_SLASH),
            Map.entry('/',  TokenType.FORWARD_SLASH),
            Map.entry('&',  TokenType.OPERATOR),
            Map.entry('|',  TokenType.OPERATOR)
    );


    public List<Token> tokenizer()
    {
        List<Token> tokens = new ArrayList<>();
        this.stream.skip(c -> ((Character.isWhitespace(c) || c == '\n')) );

        while(stream.hasNext())
        {
            if(Character.isWhitespace(stream.peek()))
            {
                this.stream.advance();
                continue;
            }

            tokens.add(nextToken());
        }

        stream.reset();
        return tokens;
    }

    public Token nextToken()
    {
        //
        Token token = new Token(TokenType.UNDEFINED, "");

        if(!this.stream.hasNext())
            return token;

        //
        this.stream.skip(c -> ((Character.isWhitespace(c) || c == '\n')) );
        char ch = this.stream.peek();

        //starts with a 'letter' or '_', pos++ for every char
        if(isStringPrefix(ch))
        {
            token = readString();
            return token;
        }

        if(isNumberPrefix(ch))
        {
            token = readNumber();
            return token;
        }

        return readMultipleOperators();
    }

    private Token readMultipleOperators() {
        //handling single * = < + . , ;
        Token token = readOperators(this.stream.consume());
        //handling, !=, <>, ...
        Token next = this.stream.hasNext() ? readOperators(this.stream.peek()) : new Token(TokenType.UNDEFINED, "");

        if(isOperator(next))
        {
            Token result = new Token(
                    token.key(),
                    (token.value() + next.value()));
            this.stream.advance();
            return result;
        }

        //TODO: check if (token.value() + next.value()) is a valid operator

        return token;
    }

    private static boolean isOperator(Token tmp) {
        return tmp.key() == TokenType.OPERATOR;
    }

    private boolean isNumberPrefix(char ch) {
        return Character.isDigit(ch);
    }

    private boolean isStringPrefix(char ch) {
        Set<Character> valid = Set.of('_', '-', '.', '\\', '/');
        return Character.isLetter(ch) || valid.contains(ch);
    }

    private Token readString()
    {
        TokenType tokenType = TokenType.STRING;
        StringBuilder builder = new StringBuilder();

        while (this.stream.hasNext())
        {
            char ch = this.stream.peek();
            if (!isValidCharInString(ch)) break;

            builder.append(this.stream.consume());
        }

        return new Token(tokenType, builder.toString());
    }

    private Token readNumber()
    {
        TokenType tokenType = TokenType.NUMBER;
        StringBuilder builder = new StringBuilder();

        while (this.stream.hasNext())
        {
            char ch = this.stream.peek();
            if (!isValidCharInNumber(ch)) break;

            builder.append(this.stream.consume());
        }

        return new Token(tokenType, builder.toString());
    }

    private Token readOperators(char ch) {
        TokenType type = SYMBOLS.getOrDefault(ch, TokenType.UNDEFINED);
        String value = (type == TokenType.UNDEFINED) ? "" : String.valueOf(ch);
        return new Token(type, value);
    }

    public void setInput(String input) {
        this.stream = new CharStream(input);
    }

    private static boolean isValidCharInString(char ch) {
        Set<Character> valid = Set.of('_', '-', '.', '\\', '/', ':');
        return Character.isLetterOrDigit(ch) || valid.contains(ch);
    }

    private boolean isValidCharInNumber(char ch) {
        return isNumberPrefix(ch) || ch == '.';
    }
}