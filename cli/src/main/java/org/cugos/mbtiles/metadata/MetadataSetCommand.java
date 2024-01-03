package org.cugos.mbtiles.metadata;

import org.cugos.mbtiles.MBTiles;
import org.cugos.mbtiles.Metadatum;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name="set")
public class MetadataSetCommand implements Callable<Integer> {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec commandSpec;

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-n", required = true, description = "The name of the Metadatum")
    private String name;

    @Option(names = "-v", required = true, description = "The value of the Metadatum")
    private String value;

    @Override
    public Integer call() throws Exception {
        PrintWriter writer = commandSpec.commandLine().getOut();
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.setMetadatum(Metadatum.of(name, value));
        return 0;
    }
}
