package org.cugos.mbtiles.metadata;

import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.cugos.mbtiles.Metadatum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataDeleteAllCommandTest {

    @Test
    void deleteAll(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(mbtiles.getMetadata().size() > 0);

        int exitCode = commandLine.execute("metadata","delete", "all", "-f", file.getAbsolutePath());
        assertEquals(0, exitCode);
        assertEquals(0, mbtiles.getMetadata().size());
    }

}
