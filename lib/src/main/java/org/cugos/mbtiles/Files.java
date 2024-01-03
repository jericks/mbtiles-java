package org.cugos.mbtiles;

import java.io.*;
import java.util.logging.Logger;

public class Files {

    private static final Logger LOGGER = Logger.getLogger(Files.class.getName());

    public static void write(byte[] data, File file) {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        } catch (IOException ex) {
            LOGGER.warning(String.format("Unable to write bytes to %s: %s", file.getAbsolutePath(), ex.getMessage()));
        }
    }

    public static byte[] getBytes(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try(InputStream inputStream = new FileInputStream(file)) {
            inputStream.read(bytes);
        } catch (IOException ex) {
            LOGGER.warning(String.format("Unable to get bytes from %s: %s", file.getAbsolutePath(), ex.getMessage()));
        }
        return bytes;
    }

}
