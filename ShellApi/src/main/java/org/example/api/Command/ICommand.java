package org.example.api.Command;

import java.util.List;

public interface ICommand {
    List<String> getArgs();
    String getValue(String flag);

    void setArgs(List<String> args);
    void put(String flag, String value);
    boolean has(String flag);
    boolean isFlagsEmpty();


}