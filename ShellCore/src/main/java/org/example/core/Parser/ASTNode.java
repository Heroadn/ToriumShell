package org.example.core.Parser;

import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public interface ASTNode {
    //(0 = success, >0 = error)
    int execute(IContext context, IConsole console) throws Exception;
}
