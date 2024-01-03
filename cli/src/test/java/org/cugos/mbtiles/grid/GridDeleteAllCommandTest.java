package org.cugos.mbtiles.grid;

import org.cugos.mbtiles.Files;
import org.cugos.mbtiles.Grid;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridDeleteAllCommandTest {

    @Test
    void deleteAllGrids(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addGrid(Grid.of(0,0,0, new byte[]{4,5,6,7}));
        mbtiles.addGrid(Grid.of(1,0,0, new byte[]{4,5,6,7}));
        mbtiles.addGrid(Grid.of(1,0,1, new byte[]{4,5,6,7}));
        mbtiles.addGrid(Grid.of(1,1,0, new byte[]{4,5,6,7}));
        mbtiles.addGrid(Grid.of(1,1,1, new byte[]{4,5,6,7}));
        assertEquals(5, mbtiles.countGrids());

        File dataFile = new File(directory, "test.data");
        Files.write(new byte[]{1,2,3}, dataFile);
        int exitCode = commandLine.execute("grid","delete","all", "-f", file.getAbsolutePath());
        assertEquals(0, exitCode);

        assertEquals(0, mbtiles.countGrids());
    }

}
