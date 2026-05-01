package org.example.core;

import org.example.api.Runtime.IContext;

public class PromptPrinter {
    public static String print(IContext context) {
        String prompt = context.getSession().getPrompt();
        if(prompt == null) return "";

        return prompt
                .replace("{dir}",  context.getRuntime().getCurrentDir().toString())
                .replace("{user}", context.getSession().getUserName())
                .replace("{home}", context.getSession().getHome().toString());
    }
}