package org.cugos.mbtiles.metadata;
import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.Metadatum;
import org.cugos.mbtiles.Tile;
import picocli.CommandLine.*;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name="metadatum")
public class MetadataDeleteMetatumCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-n", required = true, description = "The name of the Metadatum")
    private String name;

    @Override
    public Integer call() throws Exception {
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.deleteMetadatum(Metadatum.of(name, null));
        return 0;
    }

}
