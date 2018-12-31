package website.automate.rancher.configbackup.services;

import com.fasterxml.jackson.databind.JsonNode;
import website.automate.rancher.configbackup.props.ConfigBackupProps;
import website.automate.rancher.configbackup.services.models.Entity;
import website.automate.rancher.configbackup.services.models.StackConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.text.MessageFormat.format;

@Service
public class RancherDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RancherDataService.class);

    private static final String GET_PROJECTS_TEMPLATE = "{0}/projects?{1}";

    private static final String GET_STACKS_TEMPLATE = "{0}/projects/{1}/stacks?{2}";

    private static final String POST_STACK_TEMPLATE = "{0}/projects/{1}/stacks/{2}/?{3}";

    private RestTemplate restTemplate;

    private ConfigBackupProps props;

    @Autowired
    public RancherDataService(RestTemplate restTemplate,
                              ConfigBackupProps props){
        this.restTemplate = restTemplate;
        this.props = props;
    }

    public List<Entity> getProjects(){
        String url = format(GET_PROJECTS_TEMPLATE,
                props.getRancherBaseUrl(),
                "limit=-1&sort=name");

        JsonNode response = getForObject(url);

        return responseToEntity(response);
    }

    public List<Entity> getStacks(String projectId){
        String url = format(GET_STACKS_TEMPLATE,
                props.getRancherBaseUrl(),
                projectId,
                "limit=-1&sort=name");

        JsonNode response =  getForObject(url);

        return responseToEntity(response);
    }

    public StackConfig getConfig(Entity project, Entity stack){
        String url = format(POST_STACK_TEMPLATE,
                props.getRancherBaseUrl(),
                project.getId(),
                stack.getId(),
                "action=exportconfig");

        JsonNode response = postForObject(url);
        if(response == null){
            return null;
        }

        return new StackConfig(project,
                stack,
                response.get("dockerComposeConfig").asText(),
                response.get("rancherComposeConfig").asText()
                );
    }

    private JsonNode getForObject(String url){
        return restTemplate.getForObject(
                url,
                JsonNode.class);
    }

    private JsonNode postForObject(String url){
        try {
            return restTemplate.postForObject(
                    url,
                    null,
                    JsonNode.class);
        } catch (Exception e){
            LOGGER.error("Can not retrieve stack configuration while calling {}." +
                            "Please check access rights and stack status.", url, e);
        }
        return null;
    }

    private List<Entity> responseToEntity(JsonNode response){
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(response.get("data").iterator(), Spliterator.ORDERED),
                false)
                .map(entity -> new Entity(
                        entity.get("id").asText(),
                        entity.get("name").asText())
                )
                .collect(Collectors.toList());
    }
}
