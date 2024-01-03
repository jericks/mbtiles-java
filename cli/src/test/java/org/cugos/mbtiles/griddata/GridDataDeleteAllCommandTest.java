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

public class GridDataDeleteAllCommandTest {

    @Test
    void deleteAllGridData(@TempDir File directory) {
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

        int exitCode = commandLine.execute("griddata","delete", "all", "-f", file.getAbsolutePath());
        assertEquals(0, exitCode);
        assertEquals(0, mbtiles.countGridData());
    }

}
