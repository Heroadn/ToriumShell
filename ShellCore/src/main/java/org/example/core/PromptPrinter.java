package org.example.core;

import org.example.api.Runtime.IContext;

public class PromptPrinter {
    public static String print(IContext context) {
        if(context.getPrompt() == null)
            return "";

        return context.getPrompt()
                .replace("{dir}",  context.getCurrentDir().toString())
                .replace("{user}", context.getUserName())
                .replace("{home}", context.getHome().toString());
    }
}