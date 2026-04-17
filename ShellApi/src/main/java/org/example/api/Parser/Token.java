package org.example.api.Parser;

//TODO: record class
public class Token
{
    public enum TYPES
    {
        ASTERISK,
        NUMBER,
        STRING,
        LPAREM,
        RPAREM,
        FORWARD_SLASH,
        BACK_SLASH,
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

    //concatenates values of token
    public void concat(Token token)
    {
        this.value += token.value;
    }

    public String toString()
    {
        return "[" + key + " : \"" + value + "\"]";
    }
}