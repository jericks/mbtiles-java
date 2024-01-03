mbtiles-java
============

mbtiles-java is a low level java library for creating and modifying MBTiles files.  

This project contains a Java Library, a Command line interface (CLI), and a Web app.

Library
-------

* MBTiles
* Tile
* Metadatum
* Grid
* GridData
  * Get all data for 0/0/0 (keys, values)

```java
// Create a new MBTiles database
File file = new File("tiles.mbtiles");
MBTiles mbtiles = MBTiles.of(file);

// Add a tile
File tileFile = new File("0/0/0.png");
Tile tile = Tile.of(0,1,2, Images.getBytes(tileFile, Images.Type.PNG));
mbtiles.addTile(tile);
```

Command line interface (CLI)
----------------------------

Create a MBTiles file.

| Flag | Description       | Required |
| ---- |-------------------| -------- |
| -f   | MBTiles file name | Yes      |

```bash
mbtiles-java create -f basemap.mbtiles
```

Web Application
---------------

mvn spring-boot:run -Dspring-boot.run.arguments="--file=countries.mbtiles"
