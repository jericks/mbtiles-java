package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.Images;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.Tile;
import picocli.CommandLine.*;

import java.io.File;
import java.util.concurrent.Callable;
@Command(name="set")
public class TileSetCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-z", required = true, description   = "The zoom level")
    private int zoomLevel;

    @Option(names = "-c", required = true, description = "The column")
    private int column;

    @Option(names = "-r", required = true, description = "The row")
    private int row;

    @Option(names = "-i", required = true, description = "The image file")
    private File imageFile;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.setTile(Tile.of(zoomLevel, column, row, Images.getBytes(imageFile)));
        return 0;
    }
}
