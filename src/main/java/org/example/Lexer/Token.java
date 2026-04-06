package org.example.Lexer;

public class Token
{
    public enum TYPES
    {
        ASTERISK,
        NUMBER,
        STRING,
        LPAREM,
        RPAREM,
        COMMA,
        SEMICOLON,
        EQUALS,
        OPERATOR,
        DOT,
        EOF,
        UNDEFINED
    }

    public TYPES key;
    public String value;

    public Token()
    {
        this.key = TYPES.UNDEFINED;
        this.value = "";
    }

    public Token(TYPES key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString()
    {
        return "[" + key + " : \"" + value + "\"]";
    }
}