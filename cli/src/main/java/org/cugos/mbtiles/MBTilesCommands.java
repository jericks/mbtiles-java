package org.cugos.mbtiles;

import org.cugos.mbtiles.grid.GridCommands;
import org.cugos.mbtiles.griddata.GridDataCommands;
import org.cugos.mbtiles.metadata.MetadataCommands;
import org.cugos.mbtiles.tile.TileCommands;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "mbtiles", subcommands = {
        CreateCommand.class,
        InfoCommand.class,
        MetadataCommands.class,
        TileCommands.class,
        GridDataCommands.class,
        GridCommands.class
})
public class MBTilesCommands {
    public static void main(String... args) {
        int exitCode = new CommandLine(new MBTilesCommands()).execute(args);
        System.exit(exitCode);
    }
}
