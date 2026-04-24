package org.example.api.Command;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand implements ICommand
{
    private List<String> flags = new ArrayList<>();
    private List<String> args = new ArrayList<>();

    @Override
    public List<String> getFlags() { return flags; }

    @Override
    public void setFlags(List<String> flags) { this.flags = flags; }

    @Override
    public List<String> getArgs() {
        return args;
    }

    @Override
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public boolean has(String flag)
    {
        return this.getFlags().contains(flag);
    }
}