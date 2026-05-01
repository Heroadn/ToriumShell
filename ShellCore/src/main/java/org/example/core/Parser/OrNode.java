package org.example.core.Parser;

import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public class OrNode implements ASTNode {
    private ASTNode left;
    private ASTNode right;

    public OrNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int execute(IContext context, IConsole console) throws Exception {
        int leftExitCode = left.execute(context, console);

        // 2. Regra de negócio do "&&": Só executa o direito se o esquerdo deu sucesso (0)
        if (leftExitCode == 0) {
            return right.execute(context, console);
        }

        return leftExitCode;
    }
}
