# Rancher Config Backup

[![Build Status](https://travis-ci.org/automate-website/rancher-config-backup.svg?branch=master)](https://travis-ci.org/automate-website/rancher-config-backup)
[![codecov.io](https://codecov.io/github/automate-website/rancher-config-backup/coverage.svg?branch=master)](https://codecov.io/github/automate-website/rancher-config-backup?branch=master)
![Docker Automated build](https://img.shields.io/docker/automated/automatewebsite/rancher-config-backup.svg)
[![Docker Hub](https://img.shields.io/docker/pulls/automatewebsite/rancher-config-backup.svg)](https://hub.docker.com/r/automatewebsite/rancher-config-backup) 

Backups all rancher stack configs to a git repository.

## Usage

```
docker run --rm \
-e "RANCHER_BASE_URL=https://rancher.local" \
-e "RANCHER_ACCOUNT_KEY=FOO" \
-e "RANCHER_SECRET_KEY=BAR" \
-e "GIT_REPOSITORY_URL=https://git.local" \
-e "GIT_USER=foo" \
-e "GIT_PASSWORD=bar" \
automatewebsite/rancher-config-backup
```

Note: Rancher user for which the token is issued must have at least `restricted` (`read` is not enough) access to the desired project a.k.a. environment. 

## License

See the [LICENSE](https://github.com/automate-website/rancher-config-backup/LICENSE) file for details.
