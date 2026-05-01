package org.example.api.Parser;

//TODO: record class
public record Token(TokenType key, String value)
{
    public String toString()
    {
        return "[" + key + " : \"" + value + "\"]";
    }
}