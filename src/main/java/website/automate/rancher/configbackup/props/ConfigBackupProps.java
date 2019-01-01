package website.automate.rancher.configbackup.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ConfigBackupProps {

    private String gitUser;

    private String gitPassword;

    private String gitRepositoryUrl;

    private String gitRepositoryName;

    private String gitRepositoryBranch;

    private String gitFilePattern;

    private String gitAuthorName;

    private String gitAuthorEmail;

    private String rancherBaseUrl;

    private String rancherSecretKey;

    private String rancherAccountKey;

    public String getGitUser() {
        return gitUser;
    }

    public void setGitUser(String gitUser) {
        this.gitUser = gitUser;
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }

    public String getGitRepositoryUrl() {
        return gitRepositoryUrl;
    }

    public void setGitRepositoryUrl(String gitRepositoryUrl) {
        this.gitRepositoryUrl = gitRepositoryUrl;
    }

    public String getGitRepositoryName() {
        return gitRepositoryName;
    }

    public void setGitRepositoryName(String gitRepositoryName) {
        this.gitRepositoryName = gitRepositoryName;
    }

    public String getGitRepositoryBranch() {
        return gitRepositoryBranch;
    }

    public void setGitRepositoryBranch(String gitRepositoryBranch) {
        this.gitRepositoryBranch = gitRepositoryBranch;
    }

    public String getRancherBaseUrl() {
        return rancherBaseUrl;
    }

    public void setRancherBaseUrl(String rancherBaseUrl) {
        this.rancherBaseUrl = rancherBaseUrl;
    }

    public String getRancherSecretKey() {
        return rancherSecretKey;
    }

    public void setRancherSecretKey(String rancherSecretKey) {
        this.rancherSecretKey = rancherSecretKey;
    }

    public String getRancherAccountKey() {
        return rancherAccountKey;
    }

    public void setRancherAccountKey(String rancherAccountKey) {
        this.rancherAccountKey = rancherAccountKey;
    }

    public String getGitAuthorName() {
        return gitAuthorName;
    }

    public void setGitAuthorName(String gitAuthorName) {
        this.gitAuthorName = gitAuthorName;
    }

    public String getGitAuthorEmail() {
        return gitAuthorEmail;
    }

    public void setGitAuthorEmail(String gitAuthorEmail) {
        this.gitAuthorEmail = gitAuthorEmail;
    }

    public String getGitFilePattern() {
        return gitFilePattern;
    }

    public void setGitFilePattern(String gitFilePattern) {
        this.gitFilePattern = gitFilePattern;
    }
}
