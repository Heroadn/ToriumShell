package org.example.api.Runtime;

import java.io.IOException;

public class InteractiveLoop
{
    private final InteractiveScreen screen;
    private final IConsole console;

    public InteractiveLoop(InteractiveScreen screen, IConsole console) {
        this.screen = screen;
        this.console = console;
    }

    public void run() throws IOException {
        console.enterRawMode();
        try {
            int tick = 0;
            while (screen.isRunning()) {
                //wait for input
                int key = console.readKey(300);
                if (key != -1) screen.onKey((char) key);

                screen.onTick(tick++);
                console.clear();
                screen.render(console);
            }
        } finally {
            console.exitRawMode();
        }
    }
}
