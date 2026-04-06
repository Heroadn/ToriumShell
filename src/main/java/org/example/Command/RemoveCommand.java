package org.example.Command;

public class RemoveCommand extends BaseCommand{
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getType() {
        return "rm";
    }
}