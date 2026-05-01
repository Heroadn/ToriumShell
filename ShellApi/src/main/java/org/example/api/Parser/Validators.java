package org.example.api.Parser;

import java.io.File;
import java.nio.file.Path;

public class Validators {
    public static boolean isInt(String s)  { return s.matches("\\d+"); }
    public static boolean isPositiveInt(String s)
    {
        return isInt(s) && Integer.parseInt(s) > 0;
    }

    public static boolean isPath(String s) { return Path.of(s).toFile().exists(); }
    public static boolean isFile(String s)
    {
        return isPath(s) && new File(s).isFile();
    }
    public static boolean isDirectory(String s)
    {
        return isPath(s) && new File(s).isDirectory();
    }
}
