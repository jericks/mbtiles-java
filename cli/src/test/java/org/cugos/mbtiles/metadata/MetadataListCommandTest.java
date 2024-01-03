package org.cugos.mbtiles.metadata;

import org.cugos.mbtiles.Images;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.cugos.mbtiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MetadataListCommandTest {

    @Test
    void list(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);

        int exitCode = commandLine.execute("metadata","list", "-f", file.getAbsolutePath());
        assertEquals(0, exitCode);
        assertEquals("attribution = Create with mbtiles-java\n" +
                "bounds = -180,-85,180,85\n" +
                "description = Tiles\n" +
                "format = jpeg\n" +
                "maxzoom = 1\n" +
                "minzoom = 0\n" +
                "name = world\n" +
                "type = base_layer\n" +
                "version = 1.0\n", writer.toString());
    }

}
