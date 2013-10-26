package com.globant.labs.mood.service;

import com.globant.labs.mood.model.setup.ImportInformation;

import java.io.InputStream;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface ImporterService {

    /**
     *
     */
    Map<String, Object> importData(final ImportInformation importInformation);

    /**
     *
     * @param inputStream
     * @return
     */
    Map<String, Object> importData(final InputStream inputStream);

}
