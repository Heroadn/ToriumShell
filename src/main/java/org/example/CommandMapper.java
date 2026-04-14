package org.example;

import org.example.Handler.*;
import org.example.Parser.*;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandMapper
{
    //TODO: mapear handler para parser
    public static Map<String, Handler> handlers() {
        Map<String, Handler> map = new LinkedHashMap<>();
        map.put("cd", new ChangeDirectoryHandler());
        map.put("ls", new ListHandler());
        map.put("rm", new RemoveHandler());
        map.put("mkdir", new MakeDirectoryHandler());
        map.put("exit", new ExitHandler());
        return map;
    }

    public static Map<String, Parser> parsers() {
        Map<String, Parser> map = new LinkedHashMap<>();
        map.put("cd", new ChangeDirectoryParser(new ArrayDeque<>()));
        map.put("ls", new ListParser(new ArrayDeque<>()));
        map.put("rm", new RemoveParser(new ArrayDeque<>()));
        map.put("mkdir", new MakeDirectoryParser(new ArrayDeque<>()));
        map.put("exit", new ExitParser(new ArrayDeque<>()));
        return map;
    }
}
