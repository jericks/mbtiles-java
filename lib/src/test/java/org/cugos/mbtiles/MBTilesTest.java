package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MBTilesTest {

    @Test
    void create(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file, Options.builder().setMinZoom(0).setMaxZoom(2).setFormat("png").build());
        assertTrue(file.exists());
        assertEquals(file, mbtiles.getFile());
        assertEquals(String.format("MBTiles{file=%s}", file.getAbsolutePath()), mbtiles.toString());
        List<Metadatum> metadata = mbtiles.getMetadata();
        assertEquals(9, metadata.size());
        assertEquals("Metadatum{name='attribution', value='Create with mbtiles-java'}", metadata.get(0).toString());
        assertEquals("Metadatum{name='bounds', value='-180,-85,180,85'}", metadata.get(1).toString());
        assertEquals("Metadatum{name='description', value='Tiles'}", metadata.get(2).toString());
        assertEquals("Metadatum{name='format', value='png'}", metadata.get(3).toString());
        assertEquals("Metadatum{name='maxzoom', value='2'}", metadata.get(4).toString());
        assertEquals("Metadatum{name='minzoom', value='0'}", metadata.get(5).toString());
        assertEquals("Metadatum{name='name', value='world'}", metadata.get(6).toString());
        assertEquals("Metadatum{name='type', value='base_layer'}", metadata.get(7).toString());
        assertEquals("Metadatum{name='version', value='1.0'}", metadata.get(8).toString());
        loadTiles(mbtiles);
    }

    // Tiles

    @Test
    void addTile(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        Tile tile = Tile.of(0,1,2, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        mbtiles.addTile(tile);

        Optional<Tile> addedTile = mbtiles.getTile(tile);
        assertTrue(addedTile.isPresent());
        assertEquals(0, addedTile.get().getZoom());
        assertEquals(1, addedTile.get().getColumn());
        assertEquals(2, addedTile.get().getRow());
        assertTrue(addedTile.get().getData().length > 0);
    }

    @Test
    void updateTile(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        Tile tile = Tile.of(0,1,2, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        mbtiles.addTile(tile);

        Optional<Tile> addedTile = mbtiles.getTile(tile);
        assertTrue(addedTile.isPresent());
        assertEquals(0, addedTile.get().getZoom());
        assertEquals(1, addedTile.get().getColumn());
        assertEquals(2, addedTile.get().getRow());
        assertTrue(addedTile.get().getData().length > 0);

        Tile tileToUpdate = Tile.of(
                addedTile.get().getZoom(),
                addedTile.get().getColumn(),
                addedTile.get().getRow(),
                Images.getBytes(Images.createImage(Size.of(256,256), Color.RED), Images.Type.PNG)
        );
        mbtiles.updateTile(tileToUpdate);

        Optional<Tile> updatedTile = mbtiles.getTile(tileToUpdate);
        assertTrue(updatedTile.isPresent());
        assertEquals(0, updatedTile.get().getZoom());
        assertEquals(1, updatedTile.get().getColumn());
        assertEquals(2, updatedTile.get().getRow());
        assertTrue(updatedTile.get().getData().length > 0);
    }

    @Test
    void getTile(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        Tile tile = Tile.of(0,1,2, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        mbtiles.addTile(tile);

        Optional<Tile> addedTile = mbtiles.getTile(tile);
        assertTrue(addedTile.isPresent());
        assertEquals(0, addedTile.get().getZoom());
        assertEquals(1, addedTile.get().getColumn());
        assertEquals(2, addedTile.get().getRow());
        assertTrue(addedTile.get().getData().length > 0);
    }

    @Test
    void deleteTile(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        Tile tile = Tile.of(0,1,2, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        mbtiles.addTile(tile);

        Optional<Tile> addedTile = mbtiles.getTile(tile);
        assertTrue(addedTile.isPresent());
        assertEquals(0, addedTile.get().getZoom());
        assertEquals(1, addedTile.get().getColumn());
        assertEquals(2, addedTile.get().getRow());
        assertTrue(addedTile.get().getData().length > 0);

        assertFalse(mbtiles.deleteTile(tile).getTile(tile).isPresent());
    }

    private void loadTiles(MBTiles mbtiles) {
        // 0
        mbtiles.addTile(Tile.of(0,0,0, Images.getBytes(new File("src/test/resources/tiles/0/0/0.png"), Images.Type.PNG)));

        // 1
        mbtiles.addTile(Tile.of(1,0,0, Images.getBytes(new File("src/test/resources/tiles/1/0/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,0,1, Images.getBytes(new File("src/test/resources/tiles/1/0/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,1,0, Images.getBytes(new File("src/test/resources/tiles/1/1/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(1,1,1, Images.getBytes(new File("src/test/resources/tiles/1/1/1.png"), Images.Type.PNG)));

        // 2
        mbtiles.addTile(Tile.of(2,3,0, Images.getBytes(new File("src/test/resources/tiles/2/3/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,3,1, Images.getBytes(new File("src/test/resources/tiles/2/3/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,3,2, Images.getBytes(new File("src/test/resources/tiles/2/3/2.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,3,3, Images.getBytes(new File("src/test/resources/tiles/2/3/3.png"), Images.Type.PNG)));

        mbtiles.addTile(Tile.of(2,2,0, Images.getBytes(new File("src/test/resources/tiles/2/2/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,2,1, Images.getBytes(new File("src/test/resources/tiles/2/2/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,2,2, Images.getBytes(new File("src/test/resources/tiles/2/2/2.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,2,3, Images.getBytes(new File("src/test/resources/tiles/2/2/3.png"), Images.Type.PNG)));

        mbtiles.addTile(Tile.of(2,1,0, Images.getBytes(new File("src/test/resources/tiles/2/1/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,1,1, Images.getBytes(new File("src/test/resources/tiles/2/1/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,1,2, Images.getBytes(new File("src/test/resources/tiles/2/1/2.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,1,3, Images.getBytes(new File("src/test/resources/tiles/2/1/3.png"), Images.Type.PNG)));

        mbtiles.addTile(Tile.of(2,0,0, Images.getBytes(new File("src/test/resources/tiles/2/0/0.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,0,1, Images.getBytes(new File("src/test/resources/tiles/2/0/1.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,0,2, Images.getBytes(new File("src/test/resources/tiles/2/0/2.png"), Images.Type.PNG)));
        mbtiles.addTile(Tile.of(2,0,3, Images.getBytes(new File("src/test/resources/tiles/2/0/3.png"), Images.Type.PNG)));
    }

    @Test
    void countTiles(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        assertEquals(21, mbtiles.countTiles());
        assertEquals(1, mbtiles.countTiles(0));
        assertEquals(4, mbtiles.countTiles(1));
        assertEquals(16, mbtiles.countTiles(2));
    }

    @Test
    void getMinMaxZoom(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        assertEquals(0, mbtiles.getMinZoom());
        assertEquals(2, mbtiles.getMaxZoom());
    }

    @Test
    void getTilesForZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        assertEquals(1, mbtiles.getTiles(0).size());
        assertEquals(4, mbtiles.getTiles(1).size());
    }

    @Test
    void getTiles(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        assertEquals(21, mbtiles.getTiles().size());
    }

    @Test
    void getTilesWithConsumer(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        AtomicInteger counter = new AtomicInteger(0);
        mbtiles.getTiles(tile -> counter.incrementAndGet());
        assertEquals(21, counter.get());
    }

    @Test
    void deleteAllTiles(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        assertEquals(21, mbtiles.countTiles());
        mbtiles.deleteTiles();
        assertEquals(0, mbtiles.countTiles());
    }

    @Test
    void deleteForZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        loadTiles(mbtiles);
        assertEquals(4, mbtiles.countTiles(1));
        assertEquals(16, mbtiles.countTiles(2));
        mbtiles.deleteTiles(1);
        assertEquals(0, mbtiles.countTiles(1));
        assertEquals(16, mbtiles.countTiles(2));
    }

    // Metadata

    @Test
    void addMetadatum(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.deleteMetadata();
        mbtiles.addMetadatum(Metadatum.of("name", "value"));
        Optional<Metadatum> metadatum = mbtiles.getMetadatum("name");
        assertTrue(metadatum.isPresent());
        assertEquals("name", metadatum.get().getName());
        assertEquals("value", metadatum.get().getValue());
    }

    @Test
    void updateMetadatum(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.deleteMetadata();
        mbtiles.addMetadatum(Metadatum.of("name", "value"));
        Optional<Metadatum> metadatum = mbtiles.getMetadatum("name");
        assertTrue(metadatum.isPresent());
        assertEquals("name", metadatum.get().getName());
        assertEquals("value", metadatum.get().getValue());

        mbtiles.updateMetadatum(Metadatum.of("name", "value changed"));
        metadatum = mbtiles.getMetadatum("name");
        assertTrue(metadatum.isPresent());
        assertEquals("name", metadatum.get().getName());
        assertEquals("value changed", metadatum.get().getValue());
    }

    @Test
    void setMetadatum(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.deleteMetadata();
        mbtiles.setMetadatum(Metadatum.of("name", "value"));
        assertEquals(1, mbtiles.getMetadata().size());
        Optional<Metadatum> metadatum = mbtiles.getMetadatum("name");
        assertTrue(metadatum.isPresent());
        assertEquals("name", metadatum.get().getName());
        assertEquals("value", metadatum.get().getValue());

        mbtiles.setMetadatum(Metadatum.of("name", "value changed"));
        assertEquals(1, mbtiles.getMetadata().size());
        metadatum = mbtiles.getMetadatum("name");
        assertTrue(metadatum.isPresent());
        assertEquals("name", metadatum.get().getName());
        assertEquals("value changed", metadatum.get().getValue());
    }

    @Test
    void deleteMetadatum(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        //Add
        mbtiles.deleteMetadata();
        Metadatum metadatum = Metadatum.of("name", "value");
        mbtiles.addMetadatum(metadatum);

        //Get
        Optional<Metadatum> addedMetadatum = mbtiles.getMetadatum("name");
        assertTrue(addedMetadatum.isPresent());
        assertEquals("name", addedMetadatum.get().getName());
        assertEquals("value", addedMetadatum.get().getValue());

        // Delete
        mbtiles.deleteMetadatum(addedMetadatum.get());
        Optional<Metadatum> deletedMetadatum = mbtiles.getMetadatum("name");
        assertFalse(deletedMetadatum.isPresent());
    }

    @Test
    void getMetadatum(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.deleteMetadata();
        mbtiles.addMetadatum(Metadatum.of("name", "value"));

        //Get
        Optional<Metadatum> metadatum = mbtiles.getMetadatum("name");
        assertTrue(metadatum.isPresent());
        assertEquals("name", metadatum.get().getName());
        assertEquals("value", metadatum.get().getValue());
    }

    @Test
    void getMetadataConsumer(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.deleteMetadata();
        mbtiles.addMetadatum(Metadatum.of("name", "Tiles"));
        mbtiles.addMetadatum(Metadatum.of("version", "1.0"));
        mbtiles.addMetadatum(Metadatum.of("description", "World wide tiles"));

        //Get
        List<Metadatum> metadata = new ArrayList<>();
        mbtiles.getMetadata(metadatum -> {
            metadata.add(metadatum);
        });
        assertEquals(3, metadata.size());
        assertEquals("description", metadata.get(0).getName());
        assertEquals("World wide tiles", metadata.get(0).getValue());
        assertEquals("name", metadata.get(1).getName());
        assertEquals("Tiles", metadata.get(1).getValue());
        assertEquals("version", metadata.get(2).getName());
        assertEquals("1.0", metadata.get(2).getValue());
    }

    @Test
    void getMetadataList(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.deleteMetadata();
        mbtiles.addMetadatum(Metadatum.of("name", "Tiles"));
        mbtiles.addMetadatum(Metadatum.of("version", "1.0"));
        mbtiles.addMetadatum(Metadatum.of("description", "World wide tiles"));

        //Get
        List<Metadatum> metadata = mbtiles.getMetadata();
        assertEquals(3, metadata.size());
        assertEquals("description", metadata.get(0).getName());
        assertEquals("World wide tiles", metadata.get(0).getValue());
        assertEquals("name", metadata.get(1).getName());
        assertEquals("Tiles", metadata.get(1).getValue());
        assertEquals("version", metadata.get(2).getName());
        assertEquals("1.0", metadata.get(2).getValue());
    }

    // Grid

    @Test
    void getMinZoomForGrids(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(1,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(2,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(3,2,3, "12345".getBytes()));

        assertEquals(1, mbtiles.getMinZoomForGrids());
    }

    @Test
    void getMaxZoomForGrids(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(1,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(2,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(3,2,3, "12345".getBytes()));

        assertEquals(3, mbtiles.getMaxZoomForGrids());
    }

    @Test
    void countGrids(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(1,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(2,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(3,2,3, "12345".getBytes()));

        assertEquals(3, mbtiles.countGrids());
    }

    @Test
    void countGridsForZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(1,1,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,2,3, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(2,2,3, "12345".getBytes()));

        assertEquals(2, mbtiles.countGrids(1));
    }

    @Test
    void addGrid(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        Grid grid = Grid.of(1,2,3, "12345".getBytes());
        mbtiles.addGrid(grid);

        // Get
        Optional<Grid> addedGrid = mbtiles.getGrid(grid);
        assertTrue(addedGrid.isPresent());
        assertEquals(1, addedGrid.get().getZoom());
        assertEquals(2, addedGrid.get().getColumn());
        assertEquals(3, addedGrid.get().getRow());
        assertArrayEquals("12345".getBytes(), addedGrid.get().getData());
    }

    @Test
    void updateGrid(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        Grid grid = Grid.of(1,2,3, "12345".getBytes());
        mbtiles.addGrid(grid);

        // Get
        Optional<Grid> addedGrid = mbtiles.getGrid(grid);
        assertTrue(addedGrid.isPresent());
        assertEquals(1, addedGrid.get().getZoom());
        assertEquals(2, addedGrid.get().getColumn());
        assertEquals(3, addedGrid.get().getRow());
        assertArrayEquals("12345".getBytes(), addedGrid.get().getData());

        // Update
        Grid gridToUpdate = Grid.of(1,2,3, "678910".getBytes());
        mbtiles.updateGrid(gridToUpdate);

        // Get
        Optional<Grid> updatedGrid = mbtiles.getGrid(gridToUpdate);
        assertTrue(updatedGrid.isPresent());
        assertEquals(1, updatedGrid.get().getZoom());
        assertEquals(2, updatedGrid.get().getColumn());
        assertEquals(3, updatedGrid.get().getRow());
        assertArrayEquals("678910".getBytes(), updatedGrid.get().getData());
    }

    @Test
    void setGrid(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        Grid grid = Grid.of(1,2,3, "12345".getBytes());
        mbtiles.setGrid(grid);

        // Get
        Optional<Grid> addedGrid = mbtiles.getGrid(grid);
        assertTrue(addedGrid.isPresent());
        assertEquals(1, addedGrid.get().getZoom());
        assertEquals(2, addedGrid.get().getColumn());
        assertEquals(3, addedGrid.get().getRow());
        assertArrayEquals("12345".getBytes(), addedGrid.get().getData());

        // Update
        Grid gridToUpdate = Grid.of(1,2,3, "678910".getBytes());
        mbtiles.setGrid(gridToUpdate);

        // Get
        Optional<Grid> updatedGrid = mbtiles.getGrid(gridToUpdate);
        assertTrue(updatedGrid.isPresent());
        assertEquals(1, updatedGrid.get().getZoom());
        assertEquals(2, updatedGrid.get().getColumn());
        assertEquals(3, updatedGrid.get().getRow());
        assertArrayEquals("678910".getBytes(), updatedGrid.get().getData());
    }

    @Test
    void deleteGrid(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        Grid grid = Grid.of(1,2,3, "12345".getBytes());
        mbtiles.addGrid(grid);

        // Get
        Optional<Grid> addedGrid = mbtiles.getGrid(grid);
        assertTrue(addedGrid.isPresent());
        assertEquals(1, addedGrid.get().getZoom());
        assertEquals(2, addedGrid.get().getColumn());
        assertEquals(3, addedGrid.get().getRow());
        assertArrayEquals("12345".getBytes(), addedGrid.get().getData());

        // Delete
        mbtiles.deleteGrid(addedGrid.get());
        assertFalse(mbtiles.getGrid(addedGrid.get()).isPresent());
    }

    @Test
    void deleteGridsByZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(0,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,1, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,1, "12345".getBytes()));
        assertEquals(5, mbtiles.countGrids());

        // Delete
        mbtiles.deleteGrids(1);
        assertEquals(1, mbtiles.countGrids());
    }

    @Test
    void deleteAllGrids(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(0,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,1, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,1, "12345".getBytes()));
        assertEquals(5, mbtiles.countGrids());

        // Delete
        mbtiles.deleteGrids();
        assertEquals(0, mbtiles.countGrids());
    }

    @Test
    void getGrid(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        Grid grid = Grid.of(1,2,3, "12345".getBytes());
        mbtiles.addGrid(grid);

        // Get
        Optional<Grid> addedGrid = mbtiles.getGrid(grid);
        assertTrue(addedGrid.isPresent());
        assertEquals(1, addedGrid.get().getZoom());
        assertEquals(2, addedGrid.get().getColumn());
        assertEquals(3, addedGrid.get().getRow());
        assertArrayEquals("12345".getBytes(), addedGrid.get().getData());
    }

    @Test
    void getGrids(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(0,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,1, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,1, "12345".getBytes()));
        assertEquals(5, mbtiles.countGrids());

        // List
        List<Grid> grids = mbtiles.getGrids();
        assertEquals(5, grids.size());
    }

    @Test
    void getGridsWithConsumer(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(0,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,1, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,1, "12345".getBytes()));
        assertEquals(5, mbtiles.countGrids());

        // List
        AtomicInteger counter = new AtomicInteger(0);
        mbtiles.getGrids(grid -> {
           counter.incrementAndGet();
        });
        assertEquals(5, counter.get());
    }

    @Test
    void getGridsByZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(0,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,1, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,1, "12345".getBytes()));
        assertEquals(5, mbtiles.countGrids());

        // List
        List<Grid> grids = mbtiles.getGrids(1);
        assertEquals(4, grids.size());
    }

    @Test
    void getGridsByZoomLevelWithConsumer(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        mbtiles.addGrid(Grid.of(0,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,0,1, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,0, "12345".getBytes()));
        mbtiles.addGrid(Grid.of(1,1,1, "12345".getBytes()));
        assertEquals(5, mbtiles.countGrids());

        // List
        AtomicInteger counter = new AtomicInteger(0);
        mbtiles.getGrids(1, grid -> {
            counter.incrementAndGet();
        });
        assertEquals(4, counter.get());
    }

    // GridData

    @Test
    void getMinMaxZoomForGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(2,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(3,0,0, "name", "[1,2,3]"));

        assertEquals(0, mbtiles.getMinZoomForGridData());
        assertEquals(3, mbtiles.getMaxZoomForGridData());
    }

    @Test
    void countGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));

        assertEquals(5, mbtiles.countGridData());
        assertEquals(1, mbtiles.countGridData(0));
        assertEquals(4, mbtiles.countGridData(1));
    }

    @Test
    void addGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        GridData gridData = GridData.of(1,2,3, "name", "[1,2,3]");
        mbtiles.addGridData(gridData);

        // Get
        Optional<GridData> addedGridData = mbtiles.getGridData(gridData);
        assertTrue(addedGridData.isPresent());
        assertEquals(1, addedGridData.get().getZoom());
        assertEquals(2, addedGridData.get().getColumn());
        assertEquals(3, addedGridData.get().getRow());
        assertEquals("name", addedGridData.get().getName());
        assertEquals("[1,2,3]", addedGridData.get().getJson());
    }

    @Test
    void updateGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        GridData gridData = GridData.of(1,2,3, "name", "[1,2,3]");
        mbtiles.addGridData(gridData);

        // Get
        Optional<GridData> addedGridData = mbtiles.getGridData(gridData);
        assertTrue(addedGridData.isPresent());

        // Update
        mbtiles.updateGridData(GridData.of(1,2,3, "name", "[4,5,6]"));
        Optional<GridData> updatedGridData = mbtiles.getGridData(gridData);
        assertTrue(updatedGridData.isPresent());
        assertEquals(1, updatedGridData.get().getZoom());
        assertEquals(2, updatedGridData.get().getColumn());
        assertEquals(3, updatedGridData.get().getRow());
        assertEquals("name", updatedGridData.get().getName());
        assertEquals("[4,5,6]", updatedGridData.get().getJson());
    }

    @Test
    void setGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        GridData gridData = GridData.of(1,2,3, "name", "[1,2,3]");
        mbtiles.setGridData(gridData);

        // Get
        Optional<GridData> addedGridData = mbtiles.getGridData(gridData);
        assertTrue(addedGridData.isPresent());

        // Update
        mbtiles.setGridData(GridData.of(1,2,3, "name", "[4,5,6]"));
        Optional<GridData> updatedGridData = mbtiles.getGridData(gridData);
        assertTrue(updatedGridData.isPresent());
        assertEquals(1, updatedGridData.get().getZoom());
        assertEquals(2, updatedGridData.get().getColumn());
        assertEquals(3, updatedGridData.get().getRow());
        assertEquals("name", updatedGridData.get().getName());
        assertEquals("[4,5,6]", updatedGridData.get().getJson());
    }

    @Test
    void deleteGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        GridData gridData = GridData.of(1,2,3, "name", "[1,2,3]");
        mbtiles.setGridData(gridData);

        // Get
        assertTrue(mbtiles.getGridData(gridData).isPresent());

        // Delete
        mbtiles.deleteGridData(gridData);
        assertFalse(mbtiles.getGridData(gridData).isPresent());
    }

    @Test
    void deleteGridDataByZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));

        assertEquals(5, mbtiles.countGridData());
        assertEquals(1, mbtiles.countGridData(0));
        assertEquals(4, mbtiles.countGridData(1));

        mbtiles.deleteGridData(1);

        assertEquals(1, mbtiles.countGridData());
        assertEquals(1, mbtiles.countGridData(0));
        assertEquals(0, mbtiles.countGridData(1));
    }

    @Test
    void deleteAllGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        assertEquals(5, mbtiles.countGridData());

        mbtiles.deleteGridData();
        assertEquals(0, mbtiles.countGridData());
    }

    @Test
    void getGridData(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        // Add
        GridData gridData = GridData.of(1,2,3, "name", "[1,2,3]");
        mbtiles.addGridData(gridData);

        // Get
        Optional<GridData> addedGridData = mbtiles.getGridData(gridData);
        assertTrue(addedGridData.isPresent());
        assertEquals(1, addedGridData.get().getZoom());
        assertEquals(2, addedGridData.get().getColumn());
        assertEquals(3, addedGridData.get().getRow());
        assertEquals("name", addedGridData.get().getName());
        assertEquals("[1,2,3]", addedGridData.get().getJson());
    }

    @Test
    void getGridDataList(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));

        List<GridData> gridData = mbtiles.getGridData();
        assertEquals(5, gridData.size());
    }

    @Test
    void getGridDataWithConsumer(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));

        AtomicInteger counter = new AtomicInteger();
        mbtiles.getGridData(gridData -> counter.incrementAndGet());
        assertEquals(5, counter.get());
    }

    @Test
    void getGridDataListByZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));

        List<GridData> gridData = mbtiles.getGridData(1);
        assertEquals(4, gridData.size());
    }

    @Test
    void getGridDataWithConsumerByZoomLevel(@TempDir File directory) {
        File file = new File(directory, "tiles.mbtiles");
        MBTiles mbtiles = MBTiles.of(file);
        assertTrue(file.exists());

        mbtiles.addGridData(GridData.of(0,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,0,0, "name", "[1,2,3]"));
        mbtiles.addGridData(GridData.of(1,1,0, "name", "[1,2,3]"));

        AtomicInteger counter = new AtomicInteger();
        mbtiles.getGridData(1, gridData -> counter.incrementAndGet());
        assertEquals(4, counter.get());
    }

}
