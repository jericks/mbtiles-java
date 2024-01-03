package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MetadatumTest {

    @Test
    void create() {
        Metadatum metadatum = Metadatum.of("type", "tiles");
        assertEquals("type", metadatum.getName());
        assertEquals("tiles", metadatum.getValue());
        assertEquals("Metadatum{name='type', value='tiles'}", metadatum.toString());
    }

    @Test
    void equalsAndHashCode() {
        Metadatum metadatum1 = Metadatum.of("type", "tiles");
        Metadatum metadatum2 = Metadatum.of("type", "tiles");
        Metadatum metadatum3 = Metadatum.of("name", "world");
        assertEquals(metadatum1, metadatum2);
        assertEquals(metadatum2, metadatum1);
        assertNotEquals(metadatum1, metadatum3);
        assertEquals(metadatum1.hashCode(), metadatum2.hashCode());
        assertEquals(metadatum2.hashCode(), metadatum1.hashCode());
        assertNotEquals(metadatum1.hashCode(), metadatum3.hashCode());
    }

}
