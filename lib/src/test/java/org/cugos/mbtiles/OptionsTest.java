package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionsTest {

    @Test
    void createWithDefaults() {
        Options options = Options.of();
        assertEquals("world", options.getName());
        assertEquals("1.0", options.getVersion());
        assertEquals("Tiles", options.getDescription());
        assertEquals("Create with mbtiles-java", options.getAttribution());
        assertEquals("base_layer", options.getType());
        assertEquals("jpeg", options.getFormat());
        assertEquals("-180,-85,180,85", options.getBounds());
        assertEquals(0, options.getMinZoom());
        assertEquals(1, options.getMaxZoom());
    }

    @Test
    void createWithCustomValues() {
        Options options = Options.builder()
                .setName("Basemap")
                .setVersion("2.0")
                .setDescription("World Basemap")
                .setAttribution("Me")
                .setType("BASE_LAYER")
                .setFormat("png")
                .setBounds("-80,-85,80,85")
                .setMinZoom(2)
                .setMaxZoom(4)
                .build();
        assertEquals("Basemap", options.getName());
        assertEquals("2.0", options.getVersion());
        assertEquals("World Basemap", options.getDescription());
        assertEquals("Me", options.getAttribution());
        assertEquals("BASE_LAYER", options.getType());
        assertEquals("png", options.getFormat());
        assertEquals("-80,-85,80,85", options.getBounds());
        assertEquals(2, options.getMinZoom());
        assertEquals(4, options.getMaxZoom());

    }

}
