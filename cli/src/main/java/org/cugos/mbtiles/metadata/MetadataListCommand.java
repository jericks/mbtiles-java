package org.cugos.mbtiles.metadata;

import org.cugos.mbtiles.MBTiles;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

@Command(name="list")
public class MetadataListCommand implements Callable<Integer> {

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec commandSpec;

    @Option(names = "-f", required = true, description = "The file name")
    private File file;

    @Override
    public Integer call() throws Exception {
        PrintWriter writer = commandSpec.commandLine().getOut();
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.getMetadata(metadatum -> writer.println(String.format("%s = %s", metadatum.getName(), metadatum.getValue())));
        return 0;
    }
}
