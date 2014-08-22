package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.service.FileStorageService;
import com.google.appengine.api.files.*;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by mmonti on 8/14/14.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private FileService fileService;

    /**
     *
     */
    public FileStorageServiceImpl() {
        this.fileService = FileServiceFactory.getFileService();
    }

    /**
     * @param filename
     * @param is
     * @return
     */
    @Override
    public String store(final String filename, final InputStream is) {
        Preconditions.checkNotNull(is, "byte array is null");

        try {
            return store(filename, ByteStreams.toByteArray(is));

        } catch (IOException e) {
            logger.debug("store - error trying to store file", e);
            return null;
        }
    }

    /**
     * @param filename
     * @param bytes
     * @return
     */
    @Override
    public String store(final String filename, final byte[] bytes) {
        Preconditions.checkNotNull(filename, "filename is null");
        Preconditions.checkNotNull(bytes, "bytes is null");

        try {
            final AppEngineFile appEngineFile = fileService.createNewBlobFile(filename);

            final FileWriteChannel fileWriteChannel = fileService.openWriteChannel(appEngineFile, true);
            fileWriteChannel.write(ByteBuffer.wrap(bytes));
            fileWriteChannel.closeFinally();

            return appEngineFile.getFullPath();

        } catch (IOException e) {
            logger.debug("store - error trying to store file", e);
            return null;
        }
    }

    /**
     * @param filePath
     * @return
     */
    @Override
    public byte[] getFile(final String filePath) {
        Preconditions.checkNotNull(filePath, "filePath is null");

        byte[] output = new byte[0];
        try {
            final FileReadChannel fileReadChannel = fileService.openReadChannel(new AppEngineFile(filePath), true);
            fileReadChannel.read(ByteBuffer.wrap(output));
            fileReadChannel.close();


            return output;

        } catch (IOException e) {
            logger.debug("get-file - error reading file from the storage", e);
            return null;
        }
    }
}
