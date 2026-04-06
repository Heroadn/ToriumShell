package org.example.Command;

import java.nio.file.Path;

public class ChangeDirectoryCommand extends BaseCommand{
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getType() {
        return "cd";
    }
}