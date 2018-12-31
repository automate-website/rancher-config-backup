package website.automate.rancher.configbackup.services;

import website.automate.rancher.configbackup.props.ConfigBackupProps;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;

import static java.text.MessageFormat.format;

@Service
public class VersionControlService {

    private ConfigBackupProps props;

    private File repositoryDir;

    private Git git;

    private CredentialsProvider credentialsProvider;

    @Autowired
    public VersionControlService(ConfigBackupProps props){
        this.props = props;
        this.credentialsProvider = new UsernamePasswordCredentialsProvider(props.getGitUserName(), props.getGitPassword());
    }

    public File getRepositoryDir(){
        return repositoryDir;
    }

    public void addCommitAndPush(String message){
        add();
        commit(message);
        push();
    }

    public boolean isClean(){

        try {
            Status status = git.status().call();
            return status.isClean();
        } catch (Exception e){
            throw new RuntimeException(format("Failed to retrieve repository {0} status.",
                    props.getGitRepositoryName(), props.getGitFilePattern()), e);
        }
    }

    private void add(){
        try {
            git.add().addFilepattern(props.getGitFilePattern()).call();
        } catch (Exception e){
            throw new RuntimeException(format("Failed to add to the repository {0} using file pattern {1}.",
                    props.getGitRepositoryName(), props.getGitFilePattern()), e);
        }
    }

    private void commit(String message){
        try {
            git.commit().setMessage(message).setAuthor(createAuthor()).call();
        } catch (Exception e){
            throw new RuntimeException(format("Failed to commit to the repository {0}.", props.getGitRepositoryName()), e);
        }
    }

    private void pull(){
        try {
            git.pull()
                    .setCredentialsProvider(credentialsProvider)
                    .setRemoteBranchName(props.getGitRepositoryBranch()).call();
        } catch (Exception e){
            throw new RuntimeException(format("Failed to pull from the repository {0}.", props.getGitRepositoryUrl()), e);
        }
    }

    private void push(){
        try {
            git.push()
                    .setCredentialsProvider(credentialsProvider)
                    .add(props.getGitRepositoryBranch()).call();
        } catch (Exception e){
            throw new RuntimeException(format("Failed to push to the repository {0}.", props.getGitRepositoryUrl()), e);
        }
    }

    private PersonIdent createAuthor(){
        return new PersonIdent(props.getGitAuthorName(), props.getGitAuthorEmail());
    }

    @PostConstruct
    public void init(){
        initRepositoryDir();
        initAndCloneRepository();
    }

    private void initAndCloneRepository(){
        try {
            git = Git.cloneRepository()
                    .setURI(props.getGitRepositoryUrl())
                    .setCredentialsProvider(credentialsProvider)
                    .setBranch(props.getGitRepositoryBranch())
                    .setDirectory(repositoryDir)
                    .call();
        } catch (Exception e) {
            throw new RuntimeException(format("Git repository {0} could not be cloned into {1}.",
                    props.getGitRepositoryUrl(),
                    repositoryDir.getAbsolutePath()), e);
        }
    }

    private void initRepositoryDir(){
        String repositoryName = props.getGitRepositoryName();
        try {
            repositoryDir = Files.createTempDirectory(repositoryName).toFile();
        } catch (Exception e){
            throw new RuntimeException(format("Failed to create directory for repository {0}.", repositoryName), e);
        }
    }
}
