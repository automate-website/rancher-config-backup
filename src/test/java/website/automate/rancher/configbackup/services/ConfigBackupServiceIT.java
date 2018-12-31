package website.automate.rancher.configbackup.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConfigBackupServiceIT {

    @Autowired
    private ConfigBackupService configBackupService;

    @Test
    public void backup() {
        configBackupService.backup();
    }

}
