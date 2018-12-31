package website.automate.rancher.configbackup.controllers;

import website.automate.rancher.configbackup.services.ConfigBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigBackupController {

  private ConfigBackupService configBackupService;

  @Autowired
  public ConfigBackupController(ConfigBackupService configBackupService) {
    this.configBackupService = configBackupService;
  }

  @PostMapping("/backup/stack-config")
  public void backupStackConfig(){
    configBackupService.backup();
  }
}
