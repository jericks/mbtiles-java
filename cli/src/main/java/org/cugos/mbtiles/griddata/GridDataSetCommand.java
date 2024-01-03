package org.cugos.mbtiles.griddata;

import org.cugos.mbtiles.GridData;
import org.cugos.mbtiles.MBTiles;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;
@Command(name="set")
public class GridDataSetCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-z", required = true, description   = "The zoom level")
    private int zoomLevel;

    @Option(names = "-c", required = true, description = "The column")
    private int column;

    @Option(names = "-r", required = true, description = "The row")
    private int row;

    @Option(names = "-n", required = true, description = "The name")
    private String name;

    @Option(names = "-j", required = true, description = "The json value")
    private String json;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.setGridData(GridData.of(zoomLevel, column, row, name, json));
        return 0;
    }
}
