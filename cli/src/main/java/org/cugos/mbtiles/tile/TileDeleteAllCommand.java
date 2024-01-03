package org.cugos.mbtiles.tile;

import org.cugos.mbtiles.MBTiles;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name="all")
public class TileDeleteAllCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.deleteTiles();
        return 0;
    }
}
