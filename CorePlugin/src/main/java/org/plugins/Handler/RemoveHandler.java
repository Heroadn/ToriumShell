package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

public class RemoveHandler implements IHandler {

    @Override
    public int execute(ICommand command, IContext context, IConsole console) throws Exception
    {
        List<String> args = command.getArgs();
        String fileName = args.getFirst();
        Path dir = context.getCurrentDir().resolve(fileName);

        if(fileExists(dir) && !isInCurrentDir(context, dir))
            throw new Exception("ERROR: Não pode deletar arquivo(s)" +
                    "fora do diretorio atual");

        //is a folder but not -r
        if(isDirectory(dir) && command.has("-r"))
        {
            throw new Exception("ERROR: Não é um arquivo: " +
                    "use -r para apagar recursivamente");
        }

        //all checked
        if (isDirectory(dir) && command.has("-r")) {
            deleteFolder(dir);
            return 0;
        }

        //only one file to delete
        deleteFile(dir);
        return 0;
    }

    private static boolean isInCurrentDir(IContext context, Path dir) {
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

    private static boolean fileExists(Path dir) {
        return Files.exists(dir);
    }
}
