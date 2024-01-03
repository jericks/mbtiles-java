package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    @Test
    void createWithData() {
        Tile tile = Tile.of(1,2,3, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        assertEquals(1, tile.getZoom());
        assertEquals(2, tile.getColumn());
        assertEquals(3, tile.getRow());
        assertTrue(tile.getData().length > 0);
        assertFalse(tile.isEmpty());
        assertEquals("Tile{zoom=1, column=2, row=3}", tile.toString());
    }

    @Test
    void createWithOutData() {
        Tile tile = Tile.of(1,2,3);
        assertEquals(1, tile.getZoom());
        assertEquals(2, tile.getColumn());
        assertEquals(3, tile.getRow());
        assertTrue(tile.isEmpty());
        assertEquals("Tile{zoom=1, column=2, row=3}", tile.toString());
    }

    @Test
    void equalsAndHashCode() {
        Tile tile1 = Tile.of(1,2,3, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        Tile tile2 = Tile.of(1,2,3, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        Tile tile3 = Tile.of(2,1,0, Images.getBytes(Images.createImage(Size.of(256,256), Color.BLUE), Images.Type.PNG));
        assertEquals(tile1, tile2);
        assertEquals(tile2, tile1);
        assertNotEquals(tile1, tile3);
        assertEquals(tile1.hashCode(), tile2.hashCode());
        assertEquals(tile2.hashCode(), tile1.hashCode());
        assertNotEquals(tile1.hashCode(), tile3.hashCode());
    }

}
