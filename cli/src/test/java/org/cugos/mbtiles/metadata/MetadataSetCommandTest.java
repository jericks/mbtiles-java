package org.cugos.mbtiles.metadata;

import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.cugos.mbtiles.Metadatum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetadataSetCommandTest {

    @Test
    void setAdd(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);

        int exitCode = commandLine.execute("metadata","set", "-f", file.getAbsolutePath(), "-n", "author", "-v", "Jane Doe");
        assertEquals(0, exitCode);

        Optional<Metadatum> metadatum = mbtiles.getMetadatum("author");
        assertTrue(metadatum.isPresent());
        assertEquals("Jane Doe", metadatum.get().getValue());
    }

    @Test
    void setUpdate(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.setMetadatum(Metadatum.of("author", "John Doe"));

        int exitCode = commandLine.execute("metadata","set", "-f", file.getAbsolutePath(), "-n", "author", "-v", "Jane Doe");
        assertEquals(0, exitCode);

        Optional<Metadatum> metadatum = mbtiles.getMetadatum("author");
        assertTrue(metadatum.isPresent());
        assertEquals("Jane Doe", metadatum.get().getValue());
    }

}
