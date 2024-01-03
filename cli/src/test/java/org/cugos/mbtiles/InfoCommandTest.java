package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfoCommandTest {

    @Test
    void info(@TempDir File directory) {
        MBTilesCommands app = new MBTilesCommands();
        CommandLine commandLine = new CommandLine(app);
        StringWriter writer = new StringWriter();
        commandLine.setOut(new PrintWriter(writer));

        File file = new File(directory, "world.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        mbtiles.addTile(Tile.of(0,0,0, Images.getBytes(new File("src/test/resources/tiles/0/0/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,0,0, Images.getBytes(new File("src/test/resources/tiles/1/0/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,0,1, Images.getBytes(new File("src/test/resources/tiles/1/0/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,1,0, Images.getBytes(new File("src/test/resources/tiles/1/1/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,1,1, Images.getBytes(new File("src/test/resources/tiles/1/1/1.png"), Images.Type.PNG)));

        int exitCode = commandLine.execute("info", "-f", file.getAbsolutePath());
        assertEquals(0, exitCode);
        assertEquals("Metadata\n" +
                "attribution = Create with mbtiles-java\n" +
                "bounds = -180,-85,180,85\n" +
                "description = Tiles\n" +
                "format = jpeg\n" +
                "maxzoom = 1\n" +
                "minzoom = 0\n" +
                "name = world\n" +
                "type = base_layer\n" +
                "version = 1.0\n" +
                "\n" +
                "Tiles\n" +
                "0 = 1\n" +
                "1 = 4\n", writer.toString());


    }

}
