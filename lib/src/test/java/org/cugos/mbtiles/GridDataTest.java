package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridDataTest {

    @Test
    void create() {
        GridData gridData = GridData.of(1,2,3, "ids", "[1,2,3,4]");
        assertEquals(1, gridData.getZoom());
        assertEquals(2, gridData.getColumn());
        assertEquals(3, gridData.getRow());
        assertEquals("ids", gridData.getName());
        assertEquals("[1,2,3,4]", gridData.getJson());
        assertEquals("GridData{zoom=1, column=2, row=3, name='ids', json='[1,2,3,4]'}", gridData.toString());
    }

    @Test
    void equalsAndHashCode() {
        GridData gridData1 = GridData.of(1,2,3, "ids", "[1,2,3,4]");
        GridData gridData2 = GridData.of(1,2,3, "ids", "[1,2,3,4]");
        GridData gridData3 = GridData.of(1,2,3, "ids", "[5,6,7,8]");
        assertEquals(gridData1, gridData2);
        assertEquals(gridData2, gridData1);
        assertNotEquals(gridData1, gridData3);
        assertEquals(gridData1.hashCode(), gridData2.hashCode());
        assertEquals(gridData2.hashCode(), gridData1.hashCode());
        assertNotEquals(gridData1.hashCode(), gridData3.hashCode());
    }

}
