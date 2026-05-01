package org.example.core.Runtime;

import org.example.api.Runtime.IContext;
import org.example.api.Runtime.IRuntimeContext;
import org.example.api.Runtime.ISessionContext;
import org.example.api.Runtime.Mode;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class Context implements IContext
{
    private final ISessionContext sessionContext;
    private final IRuntimeContext runtimeContext;

    public Context(ISessionContext sessionContext, IRuntimeContext runtimeContext) {
        this.sessionContext = sessionContext;
        this.runtimeContext = runtimeContext;
    }

    @Override
    public ISessionContext getSession() {
        return sessionContext;
    }

    @Override
    public IRuntimeContext getRuntime() {
        return runtimeContext;
    }
}
