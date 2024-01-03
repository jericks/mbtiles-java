package org.cugos.mbtiles.metadata;

import picocli.CommandLine.Command;

@Command(name = "metadata", subcommands = {
        MetadataListCommand.class,
        MetadataGetCommand.class,
        MetadataSetCommand.class,
        MetadataDeleteCommands.class
})
public class MetadataCommands {
}
