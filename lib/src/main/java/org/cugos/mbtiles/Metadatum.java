package org.cugos.mbtiles;

import java.util.Objects;

public class Metadatum {

    private final String name;

    private final String value;

    public Metadatum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Metadatum of(String name, String value) {
        return new Metadatum(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadatum metadatum = (Metadatum) o;
        return Objects.equals(name, metadatum.name) && Objects.equals(value, metadatum.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "Metadatum{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
