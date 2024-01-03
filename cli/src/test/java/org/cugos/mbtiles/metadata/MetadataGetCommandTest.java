package org.cugos.mbtiles.metadata;

import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetadataGetCommandTest {

    @Test
    void get(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);

        int exitCode = commandLine.execute("metadata","get", "-f", file.getAbsolutePath(), "-n", "format");
        assertEquals(0, exitCode);
        assertEquals("jpeg\n", writer.toString());
    }

}
