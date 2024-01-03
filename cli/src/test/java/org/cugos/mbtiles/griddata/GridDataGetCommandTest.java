package org.cugos.mbtiles.griddata;

import org.cugos.mbtiles.Grid;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridDataGetCommandTest {

    @Test
    void getGridData(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addGridData(GridData.of(0,0,0, "values", "[1,2,3,4]"));
        assertEquals(1, mbtiles.countGridData());

        int exitCode = commandLine.execute("griddata","get", "-f", file.getAbsolutePath(), "-z", "0", "-c", "0", "-r", "0", "-n", "values");
        assertEquals(0, exitCode);
        assertEquals("[1,2,3,4]", writer.toString());
    }

}
