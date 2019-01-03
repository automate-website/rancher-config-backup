package website.automate.rancher.configbackup.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import website.automate.rancher.configbackup.props.ConfigBackupProps;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

@Service
public class ConfigEncryptionService {

    private TextEncryptor encryptor;

    private ConfigBackupProps props;

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Autowired
    public ConfigEncryptionService(TextEncryptor encryptor,
                                   ConfigBackupProps props){
        this.encryptor = encryptor;
        this.props = props;
    }

    public String encryptSecrets(String yamlStr){
       JsonNode document = deserializeFromYaml(yamlStr);

       encryptSecrets(document);

       return serializeToYaml(document);
    }

    private void encryptSecrets(JsonNode node){
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();

        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            JsonNode value = field.getValue();

            if (value.isContainerNode()) {
                encryptSecrets(value);
            } else {
                String key = field.getKey();
                if(key.matches(props.getEncryptKeyPattern())){
                    field.setValue(TextNode.valueOf(encryptor.encrypt(value.asText())));
                }
            }
        }
    }

    private String serializeToYaml(JsonNode document){
        try {
            return mapper.writeValueAsString(document);
        } catch(Exception e){
            throw new RuntimeException(MessageFormat.format("Can not serialize to yaml.", e));
        }
    }

    private JsonNode deserializeFromYaml(String yamlStr){
        try {
            return mapper.readValue(yamlStr, JsonNode.class);
        } catch(Exception e){
            throw new RuntimeException(MessageFormat.format("Can not deserialize from yaml.", e));
        }
    }
}
