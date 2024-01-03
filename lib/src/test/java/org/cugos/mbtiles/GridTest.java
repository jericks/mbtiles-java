package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    @Test
    void createWithData() {
        Grid grid = Grid.of(1,2,3, "1234".getBytes());
        assertEquals(1, grid.getZoom());
        assertEquals(2, grid.getColumn());
        assertEquals(3, grid.getRow());
        assertArrayEquals("1234".getBytes(), grid.getData());
        assertFalse(grid.isEmpty());
        assertEquals("Grid{zoom=1, column=2, row=3, data=[49, 50, 51, 52]}", grid.toString());
    }

    @Test
    void createWithOutData() {
        Grid grid = Grid.of(1,2,3);
        assertEquals(1, grid.getZoom());
        assertEquals(2, grid.getColumn());
        assertEquals(3, grid.getRow());
        assertTrue(grid.isEmpty());
        assertEquals("Grid{zoom=1, column=2, row=3, data=[]}", grid.toString());
    }

    @Test
    void equalsAndHashCode() {
        Grid grid1 = Grid.of(1,2,3, "1234".getBytes());
        Grid grid2 = Grid.of(1,2,3, "1234".getBytes());
        Grid grid3 = Grid.of(1,2,3, "5678".getBytes());
        assertEquals(grid1, grid2);
        assertEquals(grid2, grid1);
        assertNotEquals(grid1, grid3);
        assertEquals(grid1.hashCode(), grid2.hashCode());
        assertEquals(grid2.hashCode(), grid1.hashCode());
        assertNotEquals(grid1.hashCode(), grid3.hashCode());
    }

}
