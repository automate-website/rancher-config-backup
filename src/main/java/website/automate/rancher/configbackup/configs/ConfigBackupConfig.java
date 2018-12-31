package website.automate.rancher.configbackup.configs;

import website.automate.rancher.configbackup.props.ConfigBackupProps;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBackupConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, ConfigBackupProps props){
            return restTemplateBuilder.basicAuthorization(
                    props.getRancherAccountKey(),
                    props.getRancherSecretKey()).build();
    }
}
