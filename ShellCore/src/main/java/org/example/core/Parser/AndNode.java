package org.example.core.Parser;

import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public class AndNode implements ASTNode {
    private ASTNode left;
    private ASTNode right;

    public AndNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int execute(IContext context, IConsole console) throws Exception {
        int leftExitCode = left.execute(context, console);

        //AND short circuit
        if (leftExitCode == 0) {
            return right.execute(context, console);
        }

        return leftExitCode;
    }
}
