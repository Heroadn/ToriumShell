package org.example.Handler;

import org.example.Command.Command;
import org.example.Command.RemoveCommand;
import org.example.IO.ShellConsole;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

public class RemoveHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context, ShellConsole console) throws Exception
    {
        List<String> flags = command.getFlags();
        List<String> args = command.getArgs();

        String fileName = args.getFirst();
        Path dir = context.getCurrentDir().resolve(fileName);

        if(fileExists(dir) && !isInCurrentDir(context, dir))
            throw new Exception("ERROR: Não pode deletar arquivo(s)" +
                    "fora do diretorio atual");

        //is a folder but not -r
        if(isDirectory(dir) && !isRecursiveFlag(flags))
        {
            throw new Exception("ERROR: Não é um arquivo: " +
                    "use -r para apagar recursivamente");
        }

        //all checked
        if (isDirectory(dir) && isRecursiveFlag(flags)) {
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

    private static boolean isRecursiveFlag(List<String> args) {
        return args.contains("-r");
    }

    private static boolean fileExists(Path dir) {
        return Files.exists(dir);
    }
}
