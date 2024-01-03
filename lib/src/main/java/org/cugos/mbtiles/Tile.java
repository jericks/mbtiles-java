package org.cugos.mbtiles;

import java.util.Arrays;
import java.util.Objects;

public class Tile {

    private final int zoom;

    private final int column;

    private final int row;

    private byte[] data;

    public Tile(int zoom, int column, int row, byte[] data) {
        this.zoom = zoom;
        this.column = column;
        this.row = row;
        this.data = data;
    }

    public int getZoom() {
        return zoom;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "zoom=" + zoom +
                ", column=" + column +
                ", row=" + row +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return zoom == tile.zoom && column == tile.column && row == tile.row && Arrays.equals(data, tile.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(zoom, column, row);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    public static Tile of(int zoom, int column, int row, byte[] data) {   
        return new Tile(zoom, column, row, data);
    }

    public static Tile of(int zoom, int column, int row) {   
        return new Tile(zoom, column, row, new byte[0]);
    }

}
