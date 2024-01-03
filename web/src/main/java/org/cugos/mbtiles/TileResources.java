package org.cugos.mbtiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class TileResources {

    @Autowired
    private MBTiles mbtiles;

    @RequestMapping(value = "tile/{z}/{x}/{y}", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<byte[]> getTile(
        @PathVariable int z,
        @PathVariable int x,
        @PathVariable int y
    ) {
        Optional<Tile> tile = mbtiles.getTile(Tile.of(z, x, y));
        if (tile.isPresent()) {
            byte[] data = tile.get().getData();
            return createHttpEntity(data, "png");
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tile/{z}/{x}/{y}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String setTile(
        @PathVariable int z,
        @PathVariable int x,
        @PathVariable int y,
        @RequestParam MultipartFile file
    ) throws IOException {
        Tile tile = Tile.of(z,x,y, file.getBytes());
        mbtiles.setTile(tile);
        return String.format("Tile %s/%s/%s set!", z,x,y);
    }

    @RequestMapping(value = "tile/{z}/{x}/{y}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String deleteTile(
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y
    ) throws IOException {
        Tile tile = Tile.of(z,x,y);
        mbtiles.deleteTile(tile);
        return String.format("Tile %s/%s/%s deleted!", z,x,y);
    }

    @RequestMapping(value = "tile/{z}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String deleteTiles(
            @PathVariable int z
    ) throws IOException {
        mbtiles.deleteTiles(z);
        return String.format("Tiles in zoom level %s deleted!", z);
    }

    @RequestMapping(value = "tile", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllTiles() throws IOException {
        mbtiles.deleteTiles();
        return String.format("All Tiles deleted!");
    }

    @RequestMapping(value = "tile/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public int countAllTiles() throws IOException {
        return mbtiles.countTiles();
    }

    @RequestMapping(value = "tile/count/{z}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public int countTiles(@PathVariable int z) throws IOException {
        return mbtiles.countTiles(z);
    }

    @RequestMapping(value = "tile/minzoom", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public int minZoom() throws IOException {
        return mbtiles.getMinZoom();
    }

    @RequestMapping(value = "tile/maxzoom", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public int maxZoom() throws IOException {
        return mbtiles.getMaxZoom();
    }

    protected HttpEntity createHttpEntity(byte[] bytes, String type) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Empty Data!");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type.equalsIgnoreCase("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG);
        headers.setContentLength(bytes.length);
        return new HttpEntity<byte[]>(bytes, headers);
    }

}
