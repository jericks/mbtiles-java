package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class SizeTest {

    @Test
    void createWithInteger() {
        Size<Integer> size = Size.of(256, 512);
        assertEquals(256, size.getWidth());
        assertEquals(512, size.getHeight());
        assertEquals("Size{width=256, height=512}", size.toString());
    }

    @Test
    void equalsAndHashCode() {
        Size<Integer> size1 = Size.of(256,256);
        Size<Integer> size2 = Size.of(256,256);
        Size<Double> size3 = Size.of(12.45,56.77);
        assertEquals(size1, size2);
        assertEquals(size2, size1);
        assertNotEquals(size1, size3);
        assertEquals(size1.hashCode(), size2.hashCode());
        assertEquals(size2.hashCode(), size1.hashCode());
        assertNotEquals(size1.hashCode(), size3.hashCode());
    }

}
