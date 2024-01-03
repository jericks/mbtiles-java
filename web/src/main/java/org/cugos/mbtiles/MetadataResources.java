package org.cugos.mbtiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MetadataResources {

    @Autowired
    private MBTiles mbtiles;

    @RequestMapping(value = "metadata", method = RequestMethod.GET)
    public List<Metadatum> list() {
        List<Metadatum> metadata = mbtiles.getMetadata();
        return metadata;
    }

    @RequestMapping(value = "metadata/{name}", method = RequestMethod.GET)
    public Metadatum get(@PathVariable String name) {
        Optional<Metadatum> metadatum = mbtiles.getMetadatum(name);
        if (metadatum.isPresent()) {
            return metadatum.get();
        } else {
            throw new IllegalArgumentException("Can't find metadata for " + name);
        }
    }

    @RequestMapping(value = "metadata/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable String name) {
        Optional<Metadatum> metadatum = mbtiles.getMetadatum(name);
        if (metadatum.isPresent()) {
            mbtiles.deleteMetadatum(metadatum.get());
            return String.format("Metadatum %s deleted.", name);
        } else {
            throw new IllegalArgumentException("Can't find metadata for " + name);
        }
    }

    @RequestMapping(value = "metadata", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String deleteAll() {
        mbtiles.deleteMetadata();
        return String.format("Metadata deleted.");
    }

}
