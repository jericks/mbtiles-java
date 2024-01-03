package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TileSetCommandTest {

    @Test
    void setTileAdd(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);

        File tileFile = new File("src/test/resources/tiles/0/0/0.png");
        int exitCode = commandLine.execute("tile","set", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-i", tileFile.getAbsolutePath());
        assertEquals(0, exitCode);

        Optional<Tile> tile = mbtiles.getTile(Tile.of(0,0,0));
        assertTrue(tile.isPresent());
    }

    @Test
    void setTileUpdate(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        File tileFile = new File("src/test/resources/tiles/0/0/0.png");
        mbtiles.addTile(Tile.of(0,0,0, Images.getBytes(tileFile, Images.Type.PNG)));
        assertEquals(1, mbtiles.countTiles());

        int exitCode = commandLine.execute("tile","set", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-i", tileFile.getAbsolutePath());
        assertEquals(0, exitCode);

        assertEquals(1, mbtiles.countTiles());
        Optional<Tile> tile = mbtiles.getTile(Tile.of(0,0,0));
        assertTrue(tile.isPresent());
    }

}
