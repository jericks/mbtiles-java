package org.cugos.mbtiles.tile;

import picocli.CommandLine.Command;

@Command(name = "tile", subcommands = {
        TileSetCommand.class,
        TileGetCommand.class,
        TileDeleteCommands.class
})
public class TileCommands {
}
