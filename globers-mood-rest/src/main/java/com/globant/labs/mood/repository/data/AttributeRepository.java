package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Attribute;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface AttributeRepository extends GenericRepository<Attribute, Long> {

    /**
     * @param key
     * @return
     */
    @Query("select attribute from Attribute attribute where attribute.key = ?1")
    Attribute attributeByKey(final String key);

}