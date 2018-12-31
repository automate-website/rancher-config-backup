package website.automate.rancher.configbackup.services;

import website.automate.rancher.configbackup.services.models.Entity;
import website.automate.rancher.configbackup.services.models.StackConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RancherDataServiceIT {

    @TestConfiguration
    public static class RancherDataServiceITConfig {

        @MockBean
        private VersionControlService versionControlService;
    }

    @Autowired
    private RancherDataService rancherDataService;

    @Test
    public void getProjectIds() {
        List<Entity> projects = rancherDataService.getProjects();

        assertThat(projects.isEmpty(), is(false));
    }

    @Test
    public void getStackIds(){
        String projectId = "1a67266";
        List<Entity> stacks = rancherDataService.getStacks(projectId);

        assertThat(stacks.isEmpty(), is(false));
    }

    @Test
    public void getStackConfig(){
        Entity project = new Entity("1a67266", "foo");
        Entity stack = new Entity("1st763", "bar");

        StackConfig stackConfig = rancherDataService.getConfig(project, stack);

        assertNotNull(stackConfig.getDockerCompose());
    }
}
