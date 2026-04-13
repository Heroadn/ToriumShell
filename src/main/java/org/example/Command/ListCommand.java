package org.example.Command;

import java.util.List;

public class ListCommand extends BaseCommand{

    @Override
    public String getType() {
        return "ls";
    }
}