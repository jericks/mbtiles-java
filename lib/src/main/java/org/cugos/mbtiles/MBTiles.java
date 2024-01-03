package org.cugos.mbtiles;

import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MBTiles {

    private final File file;

    private final SQLiteDataSource dataSource;

    private static final Logger LOGGER = Logger.getLogger(MBTiles.class.getName());

    public MBTiles(File file) {
        this(file, Options.of());
    }

    public MBTiles(File file, Options options) {
        this.file = file;
        this.dataSource = new SQLiteDataSource();
        dataSource.setUrl(String.format("jdbc:sqlite:%s", this.file.getAbsolutePath()));
        createTables();
        addMetadata(options);
    }

    public static MBTiles of(File file) {
        return new MBTiles(file);
    }

    public static MBTiles of(File file, Options options) {
        return new MBTiles(file, options);
    }

    private void createTables() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS metadata (name text, value text);");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS tiles (zoom_level integer, tile_column integer, tile_row integer, tile_data blob);");
                statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS tile_index on tiles (zoom_level, tile_column, tile_row);");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS grids (zoom_level integer, tile_column integer, tile_row integer, grid blob);");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS grid_data (zoom_level integer, tile_column integer, tile_row integer, key_name text, key_json text);");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    // Tiles

    public int getMinZoom() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select min(zoom_level) as min_zoom_level from tiles");
                rs.next();
                return rs.getInt("min_zoom_level");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int getMaxZoom() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select max(zoom_level) as max_zoom_level from tiles");
                rs.next();
                return rs.getInt("max_zoom_level");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public MBTiles updateMinMaxZoom() {
        int max = getMaxZoom();
        int min = getMinZoom();
        setMetadatum(Metadatum.of("minzoom", String.valueOf(min)));
        setMetadatum(Metadatum.of("maxzoom", String.valueOf(max)));
        return this;
    }

    public int countTiles() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as tile_count from tiles")) {
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getInt("tile_count");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int countTiles(int zoomLevel) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as tile_count from tiles where zoom_level = ?")) {
                statement.setInt(1, zoomLevel);
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getInt("tile_count");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public MBTiles addTile(Tile tile) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO tiles (zoom_level, tile_column, tile_row, tile_data) VALUES (?,?,?,?)")) {
                statement.setInt(1, tile.getZoom());
                statement.setInt(2, tile.getColumn());
                statement.setInt(3, tile.getRow());
                statement.setBytes(4, tile.getData());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles updateTile(Tile tile) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE tiles SET tile_data = ? WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?")) {
                statement.setBytes(1, tile.getData());
                statement.setInt(2, tile.getZoom());
                statement.setInt(3, tile.getColumn());
                statement.setInt(4, tile.getRow());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles setTile(Tile tile) {
        if (getTile(tile).isPresent()) {
            updateTile(tile);
        } else {
            addTile(tile);
        }
        return this;
    }

    public MBTiles deleteTile(Tile tile) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tiles WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?")) {
                statement.setInt(1, tile.getZoom());
                statement.setInt(2, tile.getColumn());
                statement.setInt(3, tile.getRow());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles deleteTiles(int zoom) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tiles WHERE zoom_level = ?")) {
                statement.setInt(1, zoom);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles deleteTiles() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM tiles")) {
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public Optional<Tile> getTile(Tile tile) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, tile_data FROM tiles WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?")) {
                statement.setInt(1, tile.getZoom());
                statement.setInt(2, tile.getColumn());
                statement.setInt(3, tile.getRow());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return Optional.of(getTile(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return Optional.empty();
    }

    private Tile getTile(ResultSet rs) throws SQLException {
        int zoom = rs.getInt("zoom_level");
        int column = rs.getInt("tile_column");
        int row = rs.getInt("tile_row");
        byte[] data = rs.getBytes("tile_data");
        return Tile.of(zoom, column, row, data);
    }

    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        this.getTiles(tile -> {
            tiles.add(tile);
        });
        return tiles;
    }

    public MBTiles getTiles(Consumer<Tile> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, tile_data FROM tiles ORDER BY zoom_level ASC, tile_column ASC, tile_row ASC")) {
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getTile(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public List<Tile> getTiles(int zoom) {
        List<Tile> tiles = new ArrayList<>();
        this.getTiles(zoom, tile -> {
            tiles.add(tile);
        });
        return tiles;
    }

    public MBTiles getTiles(int zoom, Consumer<Tile> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, tile_data FROM tiles WHERE zoom_level = ? ORDER BY zoom_level ASC, tile_column ASC, tile_row ASC")) {
                statement.setInt(1, zoom);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getTile(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    // Metadata

    private void addMetadata(Options options) {
        setMetadatum(Metadatum.of("name", options.getName()));
        setMetadatum(Metadatum.of("version", options.getVersion()));
        setMetadatum(Metadatum.of("description", options.getDescription()));
        setMetadatum(Metadatum.of("attribution", options.getAttribution()));
        setMetadatum(Metadatum.of("bounds", options.getBounds()));
        setMetadatum(Metadatum.of("type", options.getType()));
        setMetadatum(Metadatum.of("format", options.getFormat()));
        setMetadatum(Metadatum.of("minzoom", String.valueOf(options.getMinZoom())));
        setMetadatum(Metadatum.of("maxzoom", String.valueOf(options.getMaxZoom())));
    }

    public MBTiles addMetadatum(Metadatum metadatum) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO metadata (name, value) VALUES (?,?)")) {
                statement.setString(1, metadatum.getName());
                statement.setString(2, metadatum.getValue());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles updateMetadatum(Metadatum metadatum) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE metadata SET value = ? WHERE name = ?")) {
                statement.setString(1, metadatum.getValue());
                statement.setString(2, metadatum.getName());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles setMetadatum(Metadatum metadatum) {
        if (getMetadatum(metadatum.getName()).isPresent()) {
            updateMetadatum(metadatum);
        } else {
            addMetadatum(metadatum);
        }
        return this;
    }

    public MBTiles deleteMetadatum(Metadatum metadatum) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM metadata WHERE name = ?")) {
                statement.setString(1, metadatum.getName());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }
    public MBTiles deleteMetadata() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM metadata")) {
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public Optional<Metadatum> getMetadatum(String nameToFind) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT name, value FROM metadata WHERE name = ?")) {
                statement.setString(1, nameToFind);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return Optional.of(getMetadatum(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return Optional.empty();
    }

    private Metadatum getMetadatum(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String value = rs.getString("value");
        return Metadatum.of(name, value);
    }

    public List<Metadatum> getMetadata() {
        List<Metadatum> metadata = new ArrayList<>();
        this.getMetadata(metadatum -> {
            metadata.add(metadatum);
        });
        return metadata;
    }
    public MBTiles getMetadata(Consumer<Metadatum> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT name, value FROM metadata ORDER BY name")) {
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getMetadatum(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    // Grid

    public int getMinZoomForGrids() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select min(zoom_level) as min_zoom_level from grids");
                rs.next();
                return rs.getInt("min_zoom_level");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int getMaxZoomForGrids() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select max(zoom_level) as max_zoom_level from grids");
                rs.next();
                return rs.getInt("max_zoom_level");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int countGrids() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as grid_count from grids")) {
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getInt("grid_count");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int countGrids(int zoomLevel) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as grid_count from grids where zoom_level = ?")) {
                statement.setInt(1, zoomLevel);
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getInt("grid_count");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public MBTiles addGrid(Grid grid) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO grids (zoom_level, tile_column, tile_row, grid) VALUES (?,?,?,?)")) {
                statement.setInt(1, grid.getZoom());
                statement.setInt(2, grid.getColumn());
                statement.setInt(3, grid.getRow());
                statement.setBytes(4, grid.getData());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles updateGrid(Grid grid) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE grids SET grid = ? WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?")) {
                statement.setBytes(1, grid.getData());
                statement.setInt(2, grid.getZoom());
                statement.setInt(3, grid.getColumn());
                statement.setInt(4, grid.getRow());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles setGrid(Grid grid) {
        if (getGrid(grid).isPresent()) {
            updateGrid(grid);
        } else {
            addGrid(grid);
        }
        return this;
    }

    public MBTiles deleteGrid(Grid grid) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM grids WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?")) {
                statement.setInt(1, grid.getZoom());
                statement.setInt(2, grid.getColumn());
                statement.setInt(3, grid.getRow());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles deleteGrids(int zoom) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM grids WHERE zoom_level = ?")) {
                statement.setInt(1, zoom);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles deleteGrids() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM grids")) {
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public Optional<Grid> getGrid(Grid tile) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, grid FROM grids WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?")) {
                statement.setInt(1, tile.getZoom());
                statement.setInt(2, tile.getColumn());
                statement.setInt(3, tile.getRow());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return Optional.of(getGrid(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return Optional.empty();
    }

    private Grid getGrid(ResultSet rs) throws SQLException {
        int zoom = rs.getInt("zoom_level");
        int column = rs.getInt("tile_column");
        int row = rs.getInt("tile_row");
        byte[] data = rs.getBytes("grid");
        return Grid.of(zoom, column, row, data);
    }

    public List<Grid> getGrids() {
        List<Grid> grids = new ArrayList<>();
        this.getGrids(grid -> {
            grids.add(grid);
        });
        return grids;
    }

    public MBTiles getGrids(Consumer<Grid> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, grid FROM grids ORDER BY zoom_level ASC, tile_column ASC, tile_row ASC")) {
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getGrid(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public List<Grid> getGrids(int zoom) {
        List<Grid> grids = new ArrayList<>();
        this.getGrids(zoom, grid -> {
            grids.add(grid);
        });
        return grids;
    }

    public MBTiles getGrids(int zoom, Consumer<Grid> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, grid FROM grids WHERE zoom_level = ? ORDER BY zoom_level ASC, tile_column ASC, tile_row ASC")) {
                statement.setInt(1, zoom);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getGrid(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    // GridData

    public int getMinZoomForGridData() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select min(zoom_level) as min_zoom_level from grid_data");
                rs.next();
                return rs.getInt("min_zoom_level");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int getMaxZoomForGridData() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select max(zoom_level) as max_zoom_level from grid_data");
                rs.next();
                return rs.getInt("max_zoom_level");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int countGridData() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as grid_data_count from grid_data")) {
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getInt("grid_data_count");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public int countGridData(int zoomLevel) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as grid_data_count from grid_data where zoom_level = ?")) {
                statement.setInt(1, zoomLevel);
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getInt("grid_data_count");
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return 0;
    }

    public MBTiles addGridData(GridData gridData) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO grid_data (zoom_level, tile_column, tile_row, key_name, key_json) VALUES (?,?,?,?,?)")) {
                statement.setInt(1, gridData.getZoom());
                statement.setInt(2, gridData.getColumn());
                statement.setInt(3, gridData.getRow());
                statement.setString(4, gridData.getName());
                statement.setString(5, gridData.getJson());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles updateGridData(GridData gridData) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE grid_data SET key_json = ? WHERE zoom_level = ? AND tile_column = ? AND tile_row = ? AND key_name = ?")) {
                statement.setString(1, gridData.getJson());
                statement.setInt(2, gridData.getZoom());
                statement.setInt(3, gridData.getColumn());
                statement.setInt(4, gridData.getRow());
                statement.setString(5, gridData.getName());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles setGridData(GridData gridData) {
        if (getGridData(gridData).isPresent()) {
            updateGridData(gridData);
        } else {
            addGridData(gridData);
        }
        return this;
    }

    public MBTiles deleteGridData(GridData gridData) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM grid_data WHERE zoom_level = ? AND tile_column = ? AND tile_row = ? AND key_name = ?")) {
                statement.setInt(1, gridData.getZoom());
                statement.setInt(2, gridData.getColumn());
                statement.setInt(3, gridData.getRow());
                statement.setString(4, gridData.getName());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles deleteGridData(int zoom) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM grid_data WHERE zoom_level = ?")) {
                statement.setInt(1, zoom);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public MBTiles deleteGridData() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM grid_data")) {
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public Optional<GridData> getGridData(GridData gridData) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, key_name, key_json FROM grid_data WHERE zoom_level = ? AND tile_column = ? AND tile_row = ? AND key_name = ?")) {
                statement.setInt(1, gridData.getZoom());
                statement.setInt(2, gridData.getColumn());
                statement.setInt(3, gridData.getRow());
                statement.setString(4, gridData.getName());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return Optional.of(getGridData(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return Optional.empty();
    }

    private GridData getGridData(ResultSet rs) throws SQLException {
        int zoom = rs.getInt("zoom_level");
        int column = rs.getInt("tile_column");
        int row = rs.getInt("tile_row");
        String name = rs.getString("key_name");
        String json = rs.getString("key_json");
        return GridData.of(zoom, column, row, name, json);
    }

    public List<GridData> getGridData() {
        List<GridData> gridData = new ArrayList<>();
        this.getGridData(gridDatum -> {
            gridData.add(gridDatum);
        });
        return gridData;
    }

    public MBTiles getGridData(Consumer<GridData> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, key_name, key_json FROM grid_data ORDER BY zoom_level ASC, tile_column ASC, tile_row ASC, key_name ASC")) {
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getGridData(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public List<GridData> getGridData(int zoom) {
        List<GridData> gridData = new ArrayList<>();
        this.getGridData(zoom, gridDatum -> {
            gridData.add(gridDatum);
        });
        return gridData;
    }

    public MBTiles getGridData(int zoom, Consumer<GridData> consumer) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT zoom_level, tile_column, tile_row, key_name, key_json FROM grid_data WHERE zoom_level = ? ORDER BY zoom_level ASC, tile_column ASC, tile_row ASC, key_name ASC")) {
                statement.setInt(1, zoom);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    consumer.accept(getGridData(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
        return this;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "MBTiles{" +
                "file=" + file +
                '}';
    }

}
