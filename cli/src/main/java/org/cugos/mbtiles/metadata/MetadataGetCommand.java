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

@Command(name="get")
public class MetadataGetCommand implements Callable<Integer> {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec commandSpec;

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Option(names = "-n", required = true, description = "The name of the Metadatum")
    private String name;

    @Override
    public Integer call() throws Exception {
        PrintWriter writer = commandSpec.commandLine().getOut();
        MBTiles mbtiles = MBTiles.of(file);
        Optional<Metadatum> metadatum = mbtiles.getMetadatum(name);
        if (metadatum.isPresent()) {
            writer.println(metadatum.get().getValue());
            return 0;
        } else {
            return 1;
        }
    }
}
