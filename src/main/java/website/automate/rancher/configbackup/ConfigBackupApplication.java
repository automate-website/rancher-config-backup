package website.automate.rancher.configbackup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import website.automate.rancher.configbackup.services.ConfigBackupService;

@SpringBootApplication
public class ConfigBackupApplication implements CommandLineRunner {

    private ConfigBackupService configBackupService;

    @Autowired
    public ConfigBackupApplication(ConfigBackupService configBackupService){
        this.configBackupService = configBackupService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.configBackupService.backup();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigBackupApplication.class, args);
    }
}
