package org.cugos.mbtiles.griddata;

import org.cugos.mbtiles.GridData;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.MBTilesCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GridDataSetCommandTest {

    @Test
    void setGridData(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);

        int exitCode = commandLine.execute("griddata","set", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-n", "values", "-j", "[1,2,3,4]");
        assertEquals(0, exitCode);

        Optional<GridData> gridData = mbtiles.getGridData(GridData.of(0,0,0,"values"));
        assertTrue(gridData.isPresent());
        assertEquals("[1,2,3,4]", gridData.get().getJson());
    }

}
