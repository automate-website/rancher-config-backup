package website.automate.rancher.configbackup.services;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import website.automate.rancher.configbackup.props.ConfigBackupProps;
import website.automate.rancher.configbackup.services.models.Entity;
import website.automate.rancher.configbackup.services.models.StackConfig;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigBackupServiceTest {

    private static final String GIT_REPOSITORY_URL = "https://git.local";

    private static final String PROJECT_ID = "p1";

    private static final String PROJECT_NAME = "project";

    private static final String STACK_NAME = "stack";

    private static final String DOCKER_COMPOSE_CONTENT = "foo";

    private static final String RANCHER_COMPOSE_CONTENT = "bar";

    private static final String DOCKER_COMPOSE_CONTENT_ENCRYPTED = "<foo>";

    private static final String RANCHER_COMPOSE_CONTENT_ENCRYPTED = "<bar>";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private Entity project;

    @Mock
    private Entity stack;

    @Mock
    private StackConfig config;

    @Mock
    private RancherDataService rancherDataService;

    @Mock
    private VersionControlService versionControlService;

    @Mock
    private ConfigBackupProps props;

    @Mock
    private ConfigEncryptionService configEncryptionService;

    @InjectMocks
    private ConfigBackupService configBackupService;

    @Test
    public void backup() {
        when(props.getGitRepositoryUrl()).thenReturn(GIT_REPOSITORY_URL);
        when(versionControlService.getRepositoryDir()).thenReturn(temporaryFolder.getRoot().getAbsoluteFile());
        when(project.getId()).thenReturn(PROJECT_ID);
        when(project.getName()).thenReturn(PROJECT_NAME);
        when(stack.getName()).thenReturn(STACK_NAME);
        when(config.getProject()).thenReturn(project);
        when(config.getStack()).thenReturn(stack);
        when(rancherDataService.getProjects()).thenReturn(singletonList(project));
        when(rancherDataService.getStacks(PROJECT_ID)).thenReturn(singletonList(stack));
        when(rancherDataService.getConfig(project, stack)).thenReturn(config);
        when(versionControlService.isClean()).thenReturn(false);
        when(configEncryptionService.encryptSecrets(DOCKER_COMPOSE_CONTENT)).thenReturn(DOCKER_COMPOSE_CONTENT_ENCRYPTED);
        when(configEncryptionService.encryptSecrets(RANCHER_COMPOSE_CONTENT)).thenReturn(RANCHER_COMPOSE_CONTENT_ENCRYPTED);

        configBackupService.backup();

        verify(versionControlService).addCommitAndPush("Update rancher stack configs.");
    }

}
