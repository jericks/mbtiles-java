package org.cugos.mbtiles.griddata;

import org.cugos.mbtiles.Grid;
import org.cugos.mbtiles.GridData;
import org.cugos.mbtiles.MBTiles;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name="griddata")
public class GridDataDeleteGridDataCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-z", required = true, description   = "The zoom level")
    private int zoomLevel;

    @Option(names = "-c", required = true, description = "The column")
    private int column = -1;

    @Option(names = "-r", required = true, description = "The row")
    private int row  = -1;

    @Option(names = "-n", required = true, description = "The name")
    private String name;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.deleteGridData(GridData.of(zoomLevel, column, row, name, null));
        return 0;
    }
}
