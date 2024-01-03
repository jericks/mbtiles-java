package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.Images;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.cugos.mbtiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileDeleteByZoomLevelCommandTest {

    @Test
    void deleteAllTiles(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addTile(Tile.of(0,0,0, Images.getBytes(new File("src/test/resources/tiles/0/0/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,0,0, Images.getBytes(new File("src/test/resources/tiles/1/0/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,0,1, Images.getBytes(new File("src/test/resources/tiles/1/0/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,1,0, Images.getBytes(new File("src/test/resources/tiles/1/1/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,1,1, Images.getBytes(new File("src/test/resources/tiles/1/1/1.png"), Images.Type.PNG)));
        assertEquals(1, mbtiles.countTiles(0));
        assertEquals(4, mbtiles.countTiles(1));

        int exitCode = commandLine.execute("tile","delete", "zoom", "-f", file.getAbsolutePath(), "-z", "1");
        assertEquals(0, exitCode);

        assertEquals(1, mbtiles.countTiles(0));
        assertEquals(0, mbtiles.countTiles(1));
    }

}
