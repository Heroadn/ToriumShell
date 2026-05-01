package org.example.api.Runtime;

import java.nio.file.Path;

public interface ISessionContext {
    Path getHome();
    String getUserName();
    String getPrompt();
    void setPrompt(String prompt);
}