
package com.samgoldmann.jsonschema2pojo.jpa.rule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JType;
import java.util.Iterator;
import java.util.Map;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.RuleFactory;
import org.jsonschema2pojo.rules.SchemaRule;

/**
 *
 * @author <a href="mailto:samuel.goldmann@edocsol.com">Sam Goldmann</a>
 */
public class SchemaRuleExt extends SchemaRule {
    
    private final RuleFactory ruleFactory;
    
    protected SchemaRuleExt(RuleFactory ruleFactory) {
        super(ruleFactory);
        this.ruleFactory = ruleFactory;
    }

    @Override
    public JType apply(String nodeName, JsonNode schemaNode, JClassContainer generatableType, Schema schema) {
        JType type = super.apply(nodeName, schemaNode, generatableType, schema);
        
        if (schemaNode.has("definitions")) {
            ObjectNode definitions = (ObjectNode)schemaNode.get("definitions");
            Iterator<Map.Entry<String, JsonNode>> itr =  definitions.fields();
            while(itr.hasNext()) {
                Map.Entry<String, JsonNode> definition = itr.next();
                ruleFactory.getSchemaRule().apply(definition.getKey(), definition.getValue(), generatableType, schema);
            }
        }
        
        return type;
    }
}
