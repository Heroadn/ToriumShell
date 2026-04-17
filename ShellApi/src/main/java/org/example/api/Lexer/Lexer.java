package org.example.api.Lexer;

import org.example.api.Parser.Token;import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//scanner? never heard of it lol
//TODO: separar interface, provavelmente classe abstrata com
//metodos de read
public class Lexer {
    private CharStream stream;

    private static final Map<Character, Token.TYPES> SYMBOLS = Map.ofEntries(
            Map.entry('*', Token.TYPES.ASTERISK),
            Map.entry('=', Token.TYPES.OPERATOR),
            Map.entry('(', Token.TYPES.LPAREM),
            Map.entry(')', Token.TYPES.RPAREM),
            Map.entry(',', Token.TYPES.COMMA),
            Map.entry(';', Token.TYPES.SEMICOLON),
            Map.entry('.', Token.TYPES.DOT),
            Map.entry('>', Token.TYPES.OPERATOR),
            Map.entry('<', Token.TYPES.OPERATOR),
            Map.entry('!', Token.TYPES.OPERATOR),
            Map.entry('\\', Token.TYPES.BACK_SLASH),
            Map.entry('/', Token.TYPES.FORWARD_SLASH)
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
        Token token = new Token();

        if(!this.stream.hasNext())
            return token;

        //
        this.stream.skip(c -> ((Character.isWhitespace(c) || c == '\n')) );
        char ch = this.stream.peek();

        //starts with a 'letter' or '_', pos++ for every char
        if(isStringPrefix(ch))
        {
            readString(token);
            return token;
        }

        if(isNumberPrefix(ch))
        {
            readNumber(token);
            return token;
        }

        return readMultipleOperators(token);
    }

    private Token readMultipleOperators(Token token) {
        Token next = new Token();

        //handling single * = < + . , ;
        readOperators(this.stream.consume(), token);

        //handling, !=, <>, ...
        if(this.stream.hasNext())
            readOperators(this.stream.peek(), next);

        if(isOperator(next))
        {
            token.value += next.value;
            this.stream.advance();
            return token;
        }

        return token;
    }

    private static boolean isOperator(Token tmp) {
        return tmp.key == Token.TYPES.OPERATOR;
    }

    private boolean isNumberPrefix(char ch) {
        return Character.isDigit(ch);
    }

    //TODO: para os prefixo e letras do meio fazer um mapa
    //TODO: classe mais especializada para leitura de strings
    //TODO: BACK_SLASH '/' pode ser operador de divisao
    //somente caracter de diretorio quando seguido por letra
    private boolean isStringPrefix(char ch) {
        return Character.isLetter(ch)
                || ch == '_'
                || ch == '-'
                || ch == '.'
                || ch == '/';
    }

    private void readString(Token token)
    {
        token.key = Token.TYPES.STRING;
        StringBuilder builder = new StringBuilder();

        while (this.stream.hasNext())
        {
            char ch = this.stream.peek();
            if (!isValidCharInString(ch)) break;

            builder.append(this.stream.consume());
        }

        token.value = builder.toString();
    }

    private void readNumber(Token token)
    {
        token.key = Token.TYPES.NUMBER;
        StringBuilder builder = new StringBuilder();

        while (this.stream.hasNext())
        {
            char ch = this.stream.peek();
            if (!isValidCharInNumber(ch)) break;

            builder.append(this.stream.consume());
        }

        token.value = builder.toString();
    }

    private void readOperators(char ch, Token token) {
        Token.TYPES type = SYMBOLS.getOrDefault(ch, Token.TYPES.UNDEFINED);
        token.key = type;
        token.value = (type == Token.TYPES.UNDEFINED) ? "" : String.valueOf(ch);
    }

    public void setInput(String input) {
        this.stream = new CharStream(input);
    }

    private static boolean isValidCharInString(char ch) {
        return Character.isLetterOrDigit(ch)
                || ch == '_'
                || ch == '-'
                || ch == '.'
                || ch == '\\'
                || ch == '/';
    }

    private boolean isValidCharInNumber(char ch) {
        return isNumberPrefix(ch) || ch == '.';
    }
}