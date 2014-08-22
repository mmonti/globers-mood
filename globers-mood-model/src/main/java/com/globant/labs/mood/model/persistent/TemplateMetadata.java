package com.globant.labs.mood.model.persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mmonti on 8/20/14.
 */
@Entity
public class TemplateMetadata extends BaseEntity implements Serializable {

    @Unowned
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ElementMetadata> elements = new HashSet<ElementMetadata>();

    /**
     *
     */
    public TemplateMetadata() {
        super();
    }

    /**
     *
     * @param elements
     */
    public TemplateMetadata(final Set<ElementMetadata> elements) {
        this.elements = elements;
    }

    /**
     *
     * @param elementMetadata
     */
    public void addElementMetadata(final ElementMetadata elementMetadata) {
        this.elements.add(elementMetadata);
    }

    /**
     *
     * @return
     */
    public Set<ElementMetadata> getElements() {
        if (elements.isEmpty()) {
            return null;
        }
        return elements;
    }

    public void setElements(Set<ElementMetadata> elements) {
        this.elements = elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TemplateMetadata that = (TemplateMetadata) o;

        if (!getId().equals(that.getId())) return false;
        if (!created.equals(that.created)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (getId() != null) {
            result = getId().hashCode() * 31;
        }
        return result + created.hashCode();
    }
}
