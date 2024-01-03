package org.cugos.mbtiles;

import java.util.Objects;

public class GridData {

    private final int zoom;

    private final int column;

    private final int row;

    private final String name;
    
    private final String json;

    public GridData(int zoom, int column, int row, String name, String json) {
        this.zoom = zoom;
        this.column = column;
        this.row = row;
        this.name = name;
        this.json = json;
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

    public String getName() {
        return name;
    }

    public String getJson() {
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridData gridData = (GridData) o;
        return zoom == gridData.zoom && column == gridData.column && row == gridData.row && Objects.equals(name, gridData.name) && Objects.equals(json, gridData.json);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zoom, column, row, name, json);
    }

    @Override
    public String toString() {
        return "GridData{" +
                "zoom=" + zoom +
                ", column=" + column +
                ", row=" + row +
                ", name='" + name + '\'' +
                ", json='" + json + '\'' +
                '}';
    }

    public static GridData of(int zoom, int column, int row, String name, String json) {
        return new GridData(zoom, column, row, name, json);
    }

    public static GridData of(int zoom, int column, int row, String name) {
        return new GridData(zoom, column, row, name, null);
    }


}
