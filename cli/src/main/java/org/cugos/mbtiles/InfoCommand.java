package org.cugos.mbtiles;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

@Command(name="info")
public class InfoCommand implements Callable<Integer> {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec commandSpec;

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Override
    public Integer call() throws Exception {
        PrintWriter writer = commandSpec.commandLine().getOut();
        MBTiles mbtiles = MBTiles.of(file);
        writer.println("Metadata");
        mbtiles.getMetadata(metadatum -> writer.println(String.format("%s = %s", metadatum.getName(), metadatum.getValue())));
        writer.println();
        writer.println("Tiles");
        int minZoomLevel = mbtiles.getMinZoom();
        int maxZoomLevel = mbtiles.getMaxZoom();
        for(int zoomLevel = minZoomLevel; zoomLevel <= maxZoomLevel; zoomLevel++) {
            writer.println(String.format("%s = %s", zoomLevel, mbtiles.countTiles(zoomLevel)));
        }
        return 0;
    }
}
