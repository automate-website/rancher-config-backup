package website.automate.rancher.configbackup.services;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.mockito.Mock;
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

    @ClassRule
    public static WireMockClassRule RANCHER_MASTER = new WireMockClassRule();

    @TestConfiguration
    public static class RancherDataServiceITConfig {

        @MockBean
        private VersionControlService versionControlService;

        @MockBean
        private ConfigBackupService configBackupService;

    }

    @Autowired
    private RancherDataService rancherDataService;

    @Test
    public void getProjects() {
        RANCHER_MASTER.stubFor(get(urlEqualTo("/v2/projects?limit=-1&sort=name")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("get-projects.json")));

        List<Entity> projects = rancherDataService.getProjects();

        Entity project = projects.get(0);
        assertThat(project.getName(), is("project"));
        assertThat(project.getId(), is("p1"));
    }

    @Test
    public void getStacks(){
        RANCHER_MASTER.stubFor(get(urlEqualTo("/v2/projects/p1/stacks?limit=-1&sort=name")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("get-stacks-by-project-id.json")));

        List<Entity> stacks = rancherDataService.getStacks("p1");

        Entity stack = stacks.get(0);
        assertThat(stack.getName(), is("stack"));
        assertThat(stack.getId(), is("s1"));
    }

    @Test
    public void getStackConfig(){
        RANCHER_MASTER.stubFor(post(urlEqualTo("/v2/projects/p1/stacks/s1/?action=exportconfig")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBodyFile("get-stack-config-by-stack-id.json")));

        Entity project = new Entity("p1", "project");
        Entity stack = new Entity("s1", "stack");

        StackConfig stackConfig = rancherDataService.getConfig(project, stack);

        assertNotNull(stackConfig.getDockerCompose());
    }
}
