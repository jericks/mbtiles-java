package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CreateCommandTest {

    @Test
    void create(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        int exitCode = commandLine.execute("create", "-f", file.getAbsolutePath());
        assertEquals(0, exitCode);
        assertTrue(file.exists());
    }

}
