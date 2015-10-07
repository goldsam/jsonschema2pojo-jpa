package com.samgoldmann.jsonschema2pojo.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.jsonschema2pojo.NoopAnnotator;


/**
 *
 * @author <a href="mailto:samuel.goldmann@edocsol.com">Sam Goldmann</a>
 */
public class JpaAnnotator extends NoopAnnotator {
    
    private static final String PERSISTENCE_NODE_PROPERTY = "persistence";
    
    private static final String MAPPED_SUPERCLASS_PROPERTY = "mappedSuperclass";
    
    private static final String ENTITY_NODE_PROPERTY = "entity";
    
    private static final String NAME_PROPERTY = "name";
    
    private static final String ENTITY_NAME_PARAM = "name";
    
    
    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        JsonNode persistenceNode = schema.get(PERSISTENCE_NODE_PROPERTY);
        if (persistenceNode != null) {
            JsonNode mappedSuperclassNode = persistenceNode.get(MAPPED_SUPERCLASS_PROPERTY);
            if(mappedSuperclassNode != null && mappedSuperclassNode.asBoolean()) {
                clazz.annotate(MappedSuperclass.class);
            } else {
                JAnnotationUse annotationUse = clazz.annotate(Entity.class);
                
                JsonNode entityNode = persistenceNode.get(ENTITY_NODE_PROPERTY);
                JsonNode entityNameNode = (entityNode != null) ? entityNode.get(NAME_PROPERTY) : null;
                String entityName = (entityNameNode != null) ? entityNameNode.asText() : null;
                if (entityName != null && !entityName.isEmpty()) {
                    annotationUse.param(ENTITY_NAME_PARAM, entityName);
                }
            }
        }
    }

    @Override
    public void additionalPropertiesField(JFieldVar field, JDefinedClass clazz, String propertyName) {
        field.annotate(Transient.class);
    }
}