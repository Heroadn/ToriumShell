package org.example.api.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseCommand implements ICommand {
    private List<String> args = new ArrayList<>();
    private final Map<String, String> flagValues = new HashMap<>();

    @Override
    public List<String> getArgs() { return args; }

    @Override
    public void setArgs(List<String> args) { this.args = args; }

    @Override
    public boolean has(String flag) {
        return flagValues.containsKey(flag);
    }

    @Override
    public void put(String flag, String value) {
        flagValues.put(flag, value);
    }

    @Override
    public String getValue(String flag) {
        return flagValues.get(flag);
    }

    @Override
    public boolean isFlagsEmpty() {
        return this.flagValues.isEmpty();
    }

    @Override
    public String toString() {
        return "BaseCommand{" +
                "args=" + args +
                ", flagValues=" + flagValues +
                '}';
    }
}