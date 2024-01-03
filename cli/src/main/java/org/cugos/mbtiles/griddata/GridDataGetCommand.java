package org.cugos.mbtiles.griddata;

import org.cugos.mbtiles.Files;
import org.cugos.mbtiles.Grid;
import org.cugos.mbtiles.GridData;
import org.cugos.mbtiles.MBTiles;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name="get")
public class GridDataGetCommand implements Callable<Integer> {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec commandSpec;

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

    @Override
    public Integer call() throws Exception {
        PrintWriter writer = commandSpec.commandLine().getOut();
        MBTiles mbtiles = MBTiles.of(file);
        Optional<GridData> gridData = mbtiles.getGridData(GridData.of(zoomLevel, column, row, name, null));
        if (gridData.isPresent()) {
            writer.write(gridData.get().getJson());
            return 0;
        } else {
            return 1;
        }
    }
}
