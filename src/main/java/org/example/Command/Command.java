package org.example.Command;

import java.util.List;

public interface Command
{
    List<String> getFlags();
    List<String> getArgs();

    void setFlags(List<String> flags);
    void setArgs(List<String> args);

    String getType();
}