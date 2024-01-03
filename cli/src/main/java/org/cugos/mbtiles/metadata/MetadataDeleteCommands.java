package org.cugos.mbtiles.metadata;

import picocli.CommandLine.*;

@Command(name = "delete", subcommands = {
        MetadataDeleteMetatumCommand.class,
        MetadataDeleteAllCommand.class
})
public class MetadataDeleteCommands {
}
