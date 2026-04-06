package org.example.Command;

import java.util.List;

public class ListCommand extends BaseCommand{
    private String currentPath;

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    @Override
    public String getType() {
        return "ls";
    }
}