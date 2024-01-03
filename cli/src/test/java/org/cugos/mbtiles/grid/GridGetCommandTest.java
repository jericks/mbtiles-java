package org.cugos.mbtiles.grid;

import org.cugos.mbtiles.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridGetCommandTest {

    @Test
    void getGrid(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addGrid(Grid.of(0,0,0, new byte[]{4,5,6,7}));
        assertEquals(1, mbtiles.countGrids());

        File gridFile = new File(directory, "data.txt");
        int exitCode = commandLine.execute("grid","get", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-i", gridFile.getAbsolutePath());
        assertEquals(0, exitCode);

        assertTrue(gridFile.exists());
    }

}
