package com.globant.labs.mood.service;

import java.io.InputStream;

/**
 * Created by mmonti on 8/14/14.
 */
public interface FileStorageService {

    /**
     * @param filename
     * @param is
     * @return
     */
    String store(final String filename, final InputStream is);

    /**
     * @param filename
     * @param bytes
     * @return
     */
    String store(final String filename, final byte[] bytes);

    /**
     * @param filePath
     * @return
     */
    byte[] getFile(final String filePath);

}
