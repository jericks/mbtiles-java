package org.cugos.mbtiles.grid;

import org.cugos.mbtiles.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridSetCommandTest {

    @Test
    void setGridAdd(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);

        File dataFile = new File(directory, "test.data");
        Files.write(new byte[]{1,2,3}, dataFile);
        int exitCode = commandLine.execute("grid","set", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-i", dataFile.getAbsolutePath());
        assertEquals(0, exitCode);

        Optional<Grid> grid = mbtiles.getGrid(Grid.of(0,0,0));
        assertTrue(grid.isPresent());
    }

    @Test
    void setGridUpdate(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addGrid(Grid.of(0,0,0, new byte[]{4,5,6,7}));
        assertEquals(1, mbtiles.countGrids());

        File dataFile = new File(directory, "test.data");
        Files.write(new byte[]{1,2,3}, dataFile);
        int exitCode = commandLine.execute("grid","set", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-i", dataFile.getAbsolutePath());
        assertEquals(0, exitCode);

        assertEquals(1, mbtiles.countGrids());
        Optional<Grid> grid = mbtiles.getGrid(Grid.of(0,0,0));
        assertTrue(grid.isPresent());
    }

}
