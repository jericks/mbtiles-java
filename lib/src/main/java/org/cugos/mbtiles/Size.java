package org.cugos.mbtiles;

import java.util.Objects;

public class Size<T> {

    private final T width;
    private final T height;

    public Size(T width, T height) {
        this.width = width;
        this.height = height;
    }

    public T getWidth() {
        return width;
    }

    public T getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size<?> size = (Size<?>) o;
        return Objects.equals(width, size.width) && Objects.equals(height, size.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    public static Size<Integer> of(int width, int height) {
        return new Size<Integer>(width, height);
    }

    public static Size<Double> of(double width, double height) {
        return new Size<Double>(width, height);
    }

}
