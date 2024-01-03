package org.cugos.mbtiles.tile;

import picocli.CommandLine.*;

@Command(name = "delete", subcommands = {
        TileDeleteAllCommand.class,
        TileDeleteByZoomLevelCommand.class,
        TileDeleteTileCommand.class
})
public class TileDeleteCommands {
}
