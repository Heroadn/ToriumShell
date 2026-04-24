package org.example.core;

import org.example.core.Shell.ShellEnvironment;
import org.example.core.Shell.ShellExecutor;
import org.example.core.Shell.ShellRunner;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws Exception
    {
        ShellEnvironment env = new ShellEnvironment();
        ShellExecutor executor = new ShellExecutor(
                env.lexer,
                env.parser,
                env.handler);
        new ShellRunner().run(env, executor);
    }
}
