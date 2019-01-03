package website.automate.rancher.configbackup.configs;

import org.eclipse.jgit.util.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
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

    @Bean
    public TextEncryptor encryptor(ConfigBackupProps props){
        String encryptPassword = props.getEncryptPassword();
        if(StringUtils.isEmptyOrNull(encryptPassword)){
            encryptPassword = "default";
        }
        return Encryptors.text(encryptPassword, props.getEncryptSalt());
    }
}
