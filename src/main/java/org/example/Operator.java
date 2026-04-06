package org.example;

public class Operator {
    public enum Type
    {
        GREATER, //>
        LESS,    //<
        EQUAL,   //=

        GREATER_EQUAL, //>=
        LESS_EQUAL,    //<=
        NOT_EQUAL      //!= , <>
    }

    private Type type;

    public Operator(Type type) {
        this.type = type;
    }

    public Operator(String operator) throws Exception {
        this.setType(operator);
    }

    public static Type fromString(String op) throws Exception
    {
        switch(op)
        {
            case ">" ->  { return Type.GREATER; }
            case "<" ->  { return Type.LESS; }
            case "="  -> { return Type.EQUAL; }
            case ">=" -> { return Type.GREATER_EQUAL; }
            case "<=" -> { return Type.LESS_EQUAL; }
            case "!=", "<>" -> { return Type.NOT_EQUAL; }
            default   -> throw new Exception("ERROR: UNKNOWN COMPARATOR " + op);
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String operator) throws Exception {
        this.type = fromString(operator);
    }
}