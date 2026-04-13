package org.example.Lexer;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CharStream implements Iterable<Character>{
    private char[] source;
    private int pos;

    public CharStream(String input)
    {
        this.source = input.toCharArray();
        this.pos = 0;
    }

    public Iterator<Character> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return CharStream.this.hasNext();
            }

            @Override
            public Character next() {
                return consume();
            }
        };
    }

    public Stream<Character> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public char peek()    { return hasNext() ? source[pos] : '\0'; }
    public char consume() { return hasNext() ? advance() : '\0'; }
    public char advance() { return source[pos++]; }
    public boolean hasNext() { return pos < source.length; }
    public void reset()   { pos = 0; }
    public void reset(String input)
    {
        this.source = input.toCharArray();
        this.pos = 0;
    }

    public void skip(Function<Character, Boolean> isSkipChar) {
        while (hasNext() && isSkipChar.apply(peek()))
            advance();
    }
}