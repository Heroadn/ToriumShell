package org.example.Command;

import java.util.List;

public interface Command
{
    List<String> getArgs();

    void setArgs(List<String> args);

    String getType();
}