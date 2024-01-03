package org.cugos.mbtiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.File;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @Value("${file}")
    public MBTiles mbtiles(String file) {
        return MBTiles.of(new File(file));
    }

}
