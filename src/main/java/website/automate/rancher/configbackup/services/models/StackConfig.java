package website.automate.rancher.configbackup.services.models;

public class StackConfig {

    private Entity project;

    private Entity stack;

    private String dockerCompose;

    private String rancherCompose;

    public StackConfig(Entity project,
                       Entity stack,
                       String dockerCompose,
                       String rancherCompose) {
        this.project = project;
        this.stack = stack;
        this.dockerCompose = dockerCompose;
        this.rancherCompose = rancherCompose;
    }

    public String getRancherCompose() {
        return rancherCompose;
    }

    public String getDockerCompose() {
        return dockerCompose;
    }

    public Entity getProject() {
        return project;
    }

    public Entity getStack() {
        return stack;
    }
}
