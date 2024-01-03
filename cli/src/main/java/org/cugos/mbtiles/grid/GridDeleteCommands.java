package org.cugos.mbtiles.grid;

import picocli.CommandLine.Command;

@Command(name = "delete", subcommands = {
        GridDeleteAllCommand.class,
        GridDeleteByZoomLevelCommand.class,
        GridDeleteGridCommand.class
})
public class GridDeleteCommands {
}
