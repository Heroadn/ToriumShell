import org.example.api.Runtime.IRuntimeContext;
import org.example.api.Runtime.ISessionContext;
import org.example.api.Runtime.Mode;
import org.example.core.Runtime.Context;
import org.example.core.Runtime.RuntimeContext;
import org.example.core.Runtime.SessionContext;

import java.nio.file.Path;
import java.time.Clock;

class MockContext extends Context {
    private static Path currentDir = Path.of("testDir");
    private static boolean running = true;
    private static Mode mode = Mode.NORMAL;
    private static String userName = "testuser";
    private static String prompt = "{user}@shell:{dir} $ ";
    private Clock clock = Clock.systemDefaultZone();

    public MockContext(ISessionContext sessionContext, IRuntimeContext runtimeContext) {
        super(sessionContext, runtimeContext);
    }

    public MockContext(Path testDir)
    {
        super(
                new SessionContext(
                        Path.of(System.getProperty("user.home")),
                        System.getProperty("user.name"),
                        prompt),
                new RuntimeContext(testDir,
                        true,
                        Mode.NORMAL,
                        0)
        );
    }

    public void setClock(Clock utc)
    {
        this.clock = utc;
    }


}
