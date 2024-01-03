package org.cugos.mbtiles.grid;

import org.cugos.mbtiles.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name="get")
public class GridGetCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-z", required = true, description   = "The zoom level")
    private int zoomLevel;

    @Option(names = "-c", required = true, description = "The column")
    private int column;

    @Option(names = "-r", required = true, description = "The row")
    private int row;

    @Option(names = "-i", required = true, description = "The data file")
    private File dataFile;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        Optional<Grid> grid = mbtiles.getGrid(Grid.of(zoomLevel, column, row));
        if (grid.isPresent()) {
            Files.write(grid.get().getData(), dataFile);
            return 0;
        } else {
            return 1;
        }
    }
}
