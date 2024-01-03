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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridDataDeleteGridDataCommandTest {

    @Test
    void deleteGridData(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addGridData(GridData.of(0,0,0, "values", "[1,2,3,4]"));
        mbtiles.addGridData(GridData.of(1,0,0, "values", "[1,2,3,4]"));
        mbtiles.addGridData(GridData.of(1,0,1, "values", "[1,2,3,4]"));
        mbtiles.addGridData(GridData.of(1,1,0, "values", "[1,2,3,4]"));
        mbtiles.addGridData(GridData.of(1,1,1, "values", "[1,2,3,4]"));
        assertEquals(5, mbtiles.countGridData());
        assertEquals(1, mbtiles.countGridData(0));
        assertEquals(4, mbtiles.countGridData(1));

        int exitCode = commandLine.execute("griddata","delete", "griddata", "-f", file.getAbsolutePath(), "-z", "1", "-c", "1", "-r", "1", "-n", "values");
        assertEquals(0, exitCode);
        assertEquals(4, mbtiles.countGridData());
        assertEquals(1, mbtiles.countGridData(0));
        assertEquals(3, mbtiles.countGridData(1));
    }

}
