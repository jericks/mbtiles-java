package org.cugos.mbtiles;

import java.util.Arrays;
import java.util.Objects;

public class Grid {

    private final int zoom;

    private final int column;

    private final int row;

    private byte[] data;

    public Grid(int zoom, int column, int row, byte[] data) {
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
        return "Grid{" +
                "zoom=" + zoom +
                ", column=" + column +
                ", row=" + row +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid = (Grid) o;
        return zoom == grid.zoom && column == grid.column && row == grid.row && Arrays.equals(data, grid.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(zoom, column, row);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    public static Grid of(int zoom, int column, int row, byte[] data) {
        return new Grid(zoom, column, row, data);
    }

    public static Grid of(int zoom, int column, int row) {
        return new Grid(zoom, column, row, new byte[0]);
    }

}
