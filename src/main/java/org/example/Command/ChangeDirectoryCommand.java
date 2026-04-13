package org.example.Command;

import java.nio.file.Path;

public class ChangeDirectoryCommand extends BaseCommand{

    @Override
    public String getType() {
        return "cd";
    }
}