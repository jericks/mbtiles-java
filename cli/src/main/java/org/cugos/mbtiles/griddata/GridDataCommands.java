package org.cugos.mbtiles.griddata;

import picocli.CommandLine.Command;

@Command(name = "griddata", subcommands = {
        GridDataSetCommand.class,
        GridDataGetCommand.class,
        GridDataDeleteCommands.class
})
public class GridDataCommands {
}
