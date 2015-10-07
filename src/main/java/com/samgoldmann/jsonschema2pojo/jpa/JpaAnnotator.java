package com.samgoldmann.jsonschema2pojo.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsonschema2pojo.NoopAnnotator;


/**
 *
 * @author <a href="mailto:samuel.goldmann@edocsol.com">Sam Goldmann</a>
 */
public class JpaAnnotator extends NoopAnnotator {
    
    private static final String IS_ABSTRACT_PROPERTY = "isAbstract";
    
    private static final String TITLE_PROPERTY = "title";
    
    private static final Log LOG = LogFactory.getLog(JpaAnnotator.class);

    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        
        String schemaTitle = getTitle(schema);
        if (schemaTitle != null) {
            LOG.info("Annotating schema: " + schemaTitle);
        } else {
            LOG.info("Annotating untitled schema.");
        }
        
        if (isAbstract(schema)) {
            clazz.annotate(MappedSuperclass.class);
        } else {
            clazz.annotate(Entity.class);
        }
    }
    
    private String getTitle(JsonNode schema) {
        JsonNode titleNode = schema.get(TITLE_PROPERTY);
        return (titleNode != null) ? titleNode.asText() : null;
    }
    
    private boolean isAbstract(JsonNode schema) {
        JsonNode titleNode = schema.get(IS_ABSTRACT_PROPERTY);
        return (titleNode != null) ? titleNode.asBoolean() : false;
    }

}