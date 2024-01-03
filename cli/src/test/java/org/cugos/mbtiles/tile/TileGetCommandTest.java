package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.Images;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.cugos.mbtiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TileGetCommandTest {

    @Test
    void getTile(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        File tileFile = new File("src/test/resources/tiles/0/0/0.png");
        mbtiles.addTile(Tile.of(0,0,0, Images.getBytes(tileFile, Images.Type.PNG)));

        File getTileFile = new File(directory, "0.png");
        int exitCode = commandLine.execute("tile","get", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-i", getTileFile.getAbsolutePath());
        assertEquals(0, exitCode);

        assertTrue(getTileFile.exists());
    }

}
