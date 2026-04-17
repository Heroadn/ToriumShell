package org.example.api.Command;

import org.example.api.Handler.IHandler;
import org.example.api.Parser.IParser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Commands.class)
public @interface Command {
    String name();
    Class<? extends IParser> parser();
    Class<? extends IHandler> handler();

    String description() default "";
    String usage() default "";
    String[] flags() default {};
}
