package org.cugos.mbtiles.grid;

import picocli.CommandLine.Command;

@Command(name = "grid", subcommands = {
        GridSetCommand.class,
        GridGetCommand.class,
        GridDeleteCommands.class
})
public class GridCommands {
}
