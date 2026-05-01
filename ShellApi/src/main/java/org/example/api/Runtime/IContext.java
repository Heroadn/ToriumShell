package org.example.api.Runtime;

import java.nio.file.Path;
import java.time.LocalDateTime;

public interface IContext {
    ISessionContext getSession();
    IRuntimeContext getRuntime();
}
