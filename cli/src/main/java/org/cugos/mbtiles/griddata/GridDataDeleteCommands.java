package org.cugos.mbtiles.griddata;

import picocli.CommandLine.Command;

@Command(name = "delete", subcommands = {
        GridDataDeleteAllCommand.class,
        GridDataDeleteByZoomLevelCommand.class,
        GridDataDeleteGridDataCommand.class
})
public class GridDataDeleteCommands {
}
