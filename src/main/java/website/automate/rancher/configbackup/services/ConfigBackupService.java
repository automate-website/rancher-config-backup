package website.automate.rancher.configbackup.services;

import website.automate.rancher.configbackup.services.models.Entity;
import website.automate.rancher.configbackup.services.models.StackConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static java.text.MessageFormat.format;

@Service
public class ConfigBackupService {

    private VersionControlService versionControlService;

    private RancherDataService rancherDataService;

    @Autowired
    public ConfigBackupService(VersionControlService versionControlService,
                               RancherDataService rancherDataService){
        this.versionControlService = versionControlService;
        this.rancherDataService = rancherDataService;
    }

    public void backup(){
        rancherDataService.getProjects().parallelStream()
                .forEach(this::getStacksAndSaveConfig);

        if(!versionControlService.isClean()) {
            versionControlService.addCommitAndPush("Update rancher stack configs.");
        }
    }

    private void getStacksAndSaveConfig(Entity project){
        rancherDataService.getStacks(project.getId())
                .parallelStream().map(stack -> rancherDataService.getConfig(project, stack))
                .forEach(config -> saveStackConfigToDisk(config));
    }

    private void saveStackConfigToDisk(StackConfig stackConfig){
        if(stackConfig == null){
            return;
        }

        String projectName = stackConfig.getProject().getName();
        String stackName = stackConfig.getStack().getName();

        File dockerCompose = createFilePath(projectName, stackName, "docker-compose.yaml");
        File rancherCompose = createFilePath(projectName, stackName, "rancher-compose.yaml");

        writeToFile(dockerCompose, stackConfig.getDockerCompose());
        writeToFile(rancherCompose, stackConfig.getRancherCompose());
    }

    private void writeToFile(File file, String value){
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
