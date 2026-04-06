package org.example.Handler;

import org.example.Command.Command;
import org.example.Command.RemoveCommand;
import org.example.ShellContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

public class RemoveHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context) throws Exception
    {
        List<String> args = command.getArgs();
        String fileName = ((RemoveCommand)command).getFileName();
        Path dir = context.getCurrentDir().resolve(fileName);

        if(!isInCurrentDir(context, dir))
            throw new Exception("ERROR: Não pode deletar" +
                    "fora do diretorio atual");

        //is a folder but not -r
        if(isDirectory(dir) && !isRecursiveArg(args))
        {
            throw new Exception("ERROR: Não é um arquivo: " +
                    "use -r para apagar recursivamente");
        }

        //all checked
        if (isDirectory(dir) && isRecursiveArg(args)) {
            deleteFolder(dir);
            return;
        }

        //only one file to delete
        deleteFile(dir);
    }

    private static boolean isInCurrentDir(ShellContext context, Path dir) {
        return dir.startsWith(context.getCurrentDir());
    }


    private void deleteFolder(Path path) throws Exception {
        try (Stream<Path> paths = Files.walk(path).sorted(reverseOrder()))
        {
            for (Path p : paths.toList())
                deleteFile(p);
        }
    }

    private void deleteFile(Path path) throws Exception {
        if(!Files.exists(path))
            throw new Exception("Arquivo não existe: " + path);
        Files.delete(path);
    }

    private static boolean isDirectory(Path dir) {
        return Files.isDirectory(dir);
    }

    private static boolean isRecursiveArg(List<String> args) {
        return args.contains("-r");
    }

    private static boolean fileExists(Path dir) {
        return Files.exists(dir);
    }
}
