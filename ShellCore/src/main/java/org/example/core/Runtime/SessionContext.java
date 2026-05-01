package org.example.core.Runtime;

import org.example.api.Runtime.ISessionContext;

import java.nio.file.Path;

public class SessionContext implements ISessionContext
{
    private Path home;
    private String userName;
    private String prompt;

    public SessionContext(
            Path home,
            String userName,
            String prompt)
    {
        this.home = home;
        this.userName = userName;
        this.prompt = prompt;
    }

    @Override
    public Path getHome() {
        return this.home;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getPrompt() {
        return this.prompt;
    }

    @Override
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
