package org.cugos.mbtiles;

public class Options {

    private final String name;
    private final String version;
    private final String description;
    private final String attribution;
    private final String type;
    private final String format;
    private final String bounds;
    private final int minZoom;
    private final int maxZoom;

    public Options(String name, String version, String description, String attribution, String type, String format, String bounds, int minZoom, int maxZoom) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.attribution = attribution;
        this.type = type;
        this.format = format;
        this.bounds = bounds;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getAttribution() {
        return attribution;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getBounds() {
        return bounds;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Options of() {
        return builder().build();
    }

    public static class Builder {
        
        private String name = "world";
        private String version = "1.0";
        private String description = "Tiles";
        private String attribution = "Create with mbtiles-java";
        private String type = "base_layer";
        private String format = "jpeg";
        private String bounds = "-180,-85,180,85";
        private int minZoom = 0;
        private int maxZoom = 1;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAttribution(String attribution) {
            this.attribution = attribution;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        public Builder setBounds(String bounds) {
            this.bounds = bounds;
            return this;
        }

        public Builder setMinZoom(int minZoom) {
            this.minZoom = minZoom;
            return this;
        }

        public Builder setMaxZoom(int maxZoom) {
            this.maxZoom = maxZoom;
            return this;
        }

        public Options build() {
            return new Options(name, version, description,attribution,type, format, bounds, minZoom, maxZoom);
        }

    }
}
