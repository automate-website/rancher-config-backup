package website.automate.rancher.configbackup.services;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.automate.rancher.configbackup.props.ConfigBackupProps;
import website.automate.rancher.configbackup.services.models.Entity;
import website.automate.rancher.configbackup.services.models.StackConfig;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static java.text.MessageFormat.format;
import static org.eclipse.jgit.util.StringUtils.isEmptyOrNull;

@Service
public class ConfigBackupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigBackupService.class);

    private VersionControlService versionControlService;

    private RancherDataService rancherDataService;

    private ConfigEncryptionService configEncryptionService;

    private ConfigBackupProps props;

    @Autowired
    public ConfigBackupService(VersionControlService versionControlService,
                               RancherDataService rancherDataService,
                               ConfigBackupProps props,
                               ConfigEncryptionService configEncryptionService){
        this.versionControlService = versionControlService;
        this.rancherDataService = rancherDataService;
        this.configEncryptionService = configEncryptionService;
        this.props = props;
    }

    public void backup(){
        rancherDataService.getProjects().parallelStream()
                .forEach(this::getStacksAndSaveConfig);

        if(!versionControlService.isClean()) {
            LOGGER.info("Commit and push changes to repository \"{}\".", props.getGitRepositoryUrl());
            versionControlService.addCommitAndPush("Update rancher stack configs.");
        } else {
            LOGGER.info("No changes. Skip repository commit/push.");
        }
    }

    private void getStacksAndSaveConfig(Entity project){
        LOGGER.info("Get project \"{}\" stacks.", project.getName());
        rancherDataService.getStacks(project.getId())
                .parallelStream().map(stack -> rancherDataService.getConfig(project, stack))
                .forEach(config -> saveStackConfigToDisk(config));
    }

    private void saveStackConfigToDisk(StackConfig stackConfig){
        if(stackConfig == null){
            return;
        }
        LOGGER.info("Get stack \"{}/{}\" config.",
                stackConfig.getProject().getName(),
                stackConfig.getStack().getName());

        String projectName = stackConfig.getProject().getName();
        String stackName = stackConfig.getStack().getName();

        File dockerCompose = createFilePath(projectName, stackName, "docker-compose.yaml");
        File rancherCompose = createFilePath(projectName, stackName, "rancher-compose.yaml");

        writeToFile(dockerCompose, encryptSecretsIfEncryptPasswordSet(stackConfig.getDockerCompose()));
        writeToFile(rancherCompose, encryptSecretsIfEncryptPasswordSet(stackConfig.getRancherCompose()));
    }

    private String encryptSecretsIfEncryptPasswordSet(String value){
        if(isEmptyOrNull(props.getEncryptPassword())){
            return value;
        }
        return configEncryptionService.encryptSecrets(value);
    }

    private void writeToFile(File file, String value){
        LOGGER.info("Write file \"{}\".", file.getAbsoluteFile());
        try {
            FileUtils.writeStringToFile(file, value, StandardCharsets.UTF_8);
        } catch (Exception e){
            throw new RuntimeException(format("Can not write to {0}.", file.getAbsoluteFile()), e);
        }
    }

    private File createFilePath(String projectName, String stackName, String fileName){
        return new File(versionControlService.getRepositoryDir(),
                projectName + "/" + stackName +  "/" + fileName);
    }
}
