package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.Images;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.cugos.mbtiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TileDeleteTileCommandTest {

    @Test
    void deleteTile(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        File tileFile = new File("src/test/resources/tiles/0/0/0.png");
        mbtiles.addTile(Tile.of(0,0,0, Images.getBytes(tileFile, Images.Type.PNG)));
        assertTrue(mbtiles.getTile(Tile.of(0,0,0)).isPresent());

        int exitCode = commandLine.execute("tile","delete", "tile", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0");
        assertEquals(0, exitCode);

        assertFalse(mbtiles.getTile(Tile.of(0,0,0)).isPresent());
    }

}
