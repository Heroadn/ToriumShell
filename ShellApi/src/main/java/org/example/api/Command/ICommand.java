package org.example.api.Command;

import java.util.List;

public interface ICommand
{
    List<String> getFlags();
    List<String> getArgs();

    void setFlags(List<String> flags);
    void setArgs(List<String> args);

    boolean has(String flag);
}