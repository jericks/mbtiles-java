package org.cugos.mbtiles;

import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.File;
import java.util.concurrent.Callable;
@Command(name="create")
public class CreateCommand implements Callable<Integer> {

    @Option(names = "-f", required = true, description = "The file name")
    private File file;
    @Option(names = "--name", required = false, description = "The name")
    private String name = "world";
    @Option(names = "--version", required = false, description = "The version")
    private String version = "1.0";
    @Option(names = "--description", required = false, description = "The description")
    private String description = "Tiles";
    @Option(names = "--attribution", required = false, description = "The attribution")
    private String attribution = "Create with mbtiles-java";
    @Option(names = "--type", required = false, description = "The type")
    private String type = "base_layer";
    @Option(names = "--format", required = false, description = "The format")
    private String format = "jpeg";
    @Option(names = "--bounds", required = false, description = "The bounds")
    private String bounds = "-180,-85,180,85";
    @Option(names = "--minzoom", required = false, description = "The min zoom level")
    private int minZoom = 0;
    @Option(names = "--maxzoom", required = false, description = "The max zoom level")
    private int maxZoom = 1;

    @Override
    public Integer call() throws Exception {
        MBTiles.of(file, Options.builder()
            .setName(name)
            .setVersion(version)
            .setDescription(description)
            .setAttribution(attribution)
            .setType(type)
            .setFormat(format)
            .setBounds(bounds)
            .setMinZoom(minZoom)
            .setMaxZoom(maxZoom)
            .build()
        );
        return 0;
    }
}
