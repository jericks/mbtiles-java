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

public class MetadataDeleteMetadatumCommandTest {

    @Test
    void delete(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.setMetadatum(Metadatum.of("author", "Jane Doe"));
        assertTrue(mbtiles.getMetadatum("author").isPresent());

        int exitCode = commandLine.execute("metadata","delete", "metadatum", "-f", file.getAbsolutePath(), "-n", "author");
        assertEquals(0, exitCode);
        assertFalse(mbtiles.getMetadatum("author").isPresent());
    }

}
