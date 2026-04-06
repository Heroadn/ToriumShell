package org.example.Lexer;

import java.util.ArrayList;
import java.util.List;

//scanner? never heard of it lol
public class Lexer {
    private int pos;
    private String input;

    public Lexer() {
        this.pos = 0;
        this.input = "";
    }

    public List<Token> tokenizer()
    {
        List<Token> tokens = new ArrayList<>();
        char[] array = input.toCharArray();

        while(pos < array.length)
        {
            tokens.add(nextToken());
        }

        this.setPos(0);
        return tokens;
    }

    public Token nextToken()
    {
        char[] array = input.toCharArray();
        Token token = new Token();
        Token tmp = new Token();

        if(pos >= array.length) return new Token();
        //skipping white spaces
        while (pos < array.length
                && (Character.isWhitespace(array[pos]) || array[pos] == '\n'))
            pos++;

        //starts with a 'letter' or '_', pos++ for every char
        if(Character.isLetter(array[pos]) || array[pos] == '_')
        {
            readString(array[pos], token);
            return token;
        }

        if(Character.isDigit(array[pos]) || array[pos] == '_')
        {
            readNumber(array[pos], token);
            return token;
        }

        //* = < + <= . , ;
        readSymbols(array[pos], token);
        if((pos + 1) < array.length)
        {
            //handling, !=, <>, ...
            readSymbols(array[pos + 1], tmp);
            if(tmp.key == Token.TYPES.OPERATOR)
            {
                token.value += tmp.value;
                this.pos++;
            }
        }

        this.pos++;
        return token;
    }

    private void readString(char c, Token token)
    {
        char[] array = this.input.toCharArray();
        token.key = Token.TYPES.STRING;

        while (this.pos < array.length &&
                (Character.isLetterOrDigit(array[pos]) || array[pos] == '_'))
        {
            token.value += array[pos];
            pos++;
        }
    }

    private void readNumber(char c, Token token)
    {
        char[] array = this.input.toCharArray();
        token.key = Token.TYPES.NUMBER;

        while (this.pos < array.length &&
                (Character.isDigit(array[pos]) || array[pos] == '.'))
        {
            token.value += array[pos];
            pos++;
        }
    }

    private void readSymbols(char c, Token token) {
        switch (c)
        {
            case '*' ->
            {
                token.key  = Token.TYPES.ASTERISK;
                token.value = "*";
            }
            case '=' ->
            {
                token.key = Token.TYPES.OPERATOR;
                token.value = "=";
            }
            case '(' ->
            {

                token.key = Token.TYPES.LPAREM;
                token.value = "(";
            }
            case ')' ->
            {
                token.key = Token.TYPES.RPAREM;
                token.value = ")";
            }
            case ',' ->
            {
                token.key = Token.TYPES.COMMA;
                token.value = ",";
            }
            case ';' ->
            {
                token.key = Token.TYPES.SEMICOLON;
                token.value = ";";
            }
            case '.' ->
            {
                token.key = Token.TYPES.DOT;
                token.value = ".";
            }
            case '>' ->
            {
                token.key = Token.TYPES.OPERATOR;
                token.value = ">";
            }
            case '<' ->
            {
                token.key = Token.TYPES.OPERATOR;
                token.value = "<";
            }
            case '!' ->
            {
                token.key = Token.TYPES.OPERATOR;
                token.value = "!";
            }
            default -> {}
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}