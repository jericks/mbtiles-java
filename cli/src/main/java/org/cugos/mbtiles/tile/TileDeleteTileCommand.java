package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.Tile;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name="tile")
public class TileDeleteTileCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-z", required = true, description   = "The zoom level")
    private int zoomLevel;

    @Option(names = "-c", required = true, description = "The column")
    private int column = -1;

    @Option(names = "-r", required = true, description = "The row")
    private int row  = -1;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.deleteTile(Tile.of(zoomLevel, column, row));
        return 0;
    }
}
