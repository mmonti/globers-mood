package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Attribute;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface AttributeService {

    /**
     * @return
     */
    Set<Attribute> attributes();

}
