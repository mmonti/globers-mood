package com.globant.labs.mood.model.persistent;

import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mmonti on 8/20/14.
 */
@Entity
public class TemplateMetadata extends BaseEntity implements Serializable {

    private final static String[] REQUIRED_FIELDS = {"campaignId", "token", "email"};

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
     * @param elements
     */
    public TemplateMetadata(final Set<ElementMetadata> elements) {
        this.elements = elements;
    }

    /**
     * @param elementMetadata
     */
    public void addElementMetadata(final ElementMetadata elementMetadata) {
        this.elements.add(elementMetadata);
    }

    public Set<ElementType> getElementTypes() {
        return Sets.newHashSet(Iterables.transform(elements, new Function<ElementMetadata, ElementType>() {
            @Override
            public ElementType apply(final ElementMetadata elementMetadata) {
                return elementMetadata.getElementType();
            }
        }));
    }

    public boolean isStatic() {
        return getElementTypes().isEmpty();
    }

    public boolean isSurvey() {
        final Set<ElementType> types = getElementTypes();
        return (
                (!types.isEmpty() && types.contains(ElementType.FORM) && hasRequiredSurveyFields()) &&
                        (types.contains(ElementType.CHOICE) || types.contains(ElementType.TEXT) || types.contains(ElementType.MULTILINE_TEXT))
        );
    }

    public boolean isValid() {
        return (isStatic() || isSurvey());
    }

    public boolean hasRequiredSurveyFields() {
        final List<String> requiredFields = Lists.newArrayList(REQUIRED_FIELDS);
        final Set<ElementMetadata> requiredElements = Sets.newHashSet(Iterables.filter(elements, new Predicate<ElementMetadata>() {
            @Override
            public boolean apply(ElementMetadata elementMetadata) {
                return (elementMetadata.getElementType().equals(ElementType.HIDDEN) || elementMetadata.getElementType().equals(ElementType.TEXT)) && (requiredFields.contains(elementMetadata.getKey()));
            }
        }));
        return (requiredElements.size() == requiredFields.size());
    }

    /**
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
