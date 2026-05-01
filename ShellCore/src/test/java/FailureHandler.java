import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public class FailureHandler implements IHandler {
    @Override
    public int execute(ICommand command, IContext context, IConsole console) throws Exception {
        return 1;
    }
}
