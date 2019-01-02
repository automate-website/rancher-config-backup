# Rancher Config Backup

[![Build Status](https://travis-ci.org/automate-website/rancher-config-backup.svg?branch=master)](https://travis-ci.org/automate-website/rancher-config-backup)
[![codecov.io](https://codecov.io/github/automate-website/rancher-config-backup/coverage.svg?branch=master)](https://codecov.io/github/automate-website/rancher-config-backup?branch=master)
![Docker Automated build](https://img.shields.io/docker/automated/automatewebsite/rancher-config-backup.svg)
[![Docker Hub](https://img.shields.io/docker/pulls/automatewebsite/rancher-config-backup.svg)](https://hub.docker.com/r/automatewebsite/rancher-config-backup) 

Creates a backup of all rancher stack configs for accessible environments to a git repository.

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

## Created Repository Structure

A project `foo` that contains a stack `bar` will result in following structure:

```
foo/
    bar/
        docker-compose.yaml
        rancher-compose.yaml
```

## Configuration

|Parameter|Description|Default Value| 
|---|---|---|
|RANCHER_BASE_URL|Rancher master base url.|`null`|
|RANCHER_ACCOUNT_KEY|Rancher token account key.|`null`|
|RANCHER_SECRET_KEY|Rancher token secret key.|`null`|
|GIT_REPOSITORY_URL|HTTPS backed Git repository url.|`null`|
|GIT_USER|Git username.|`null`|
|GIT_PASSWORD|Git user password.|`null`|
|GIT_FILE_PATTERN|Git file pattern used to add files to index. Defaults to all files.|`"."`|
|GIT_AUTHOR_NAME|Git author name..|`rancher-config-backup`|
|GIT_AUTHOR_EMAIL|Git author email.|`noreply@automate.website`|
|GIT_REPOSITORY_NAME|Git repository name, utilized for logging only.|`default`|


## License

See the [LICENSE](https://github.com/automate-website/rancher-config-backup/LICENSE) file for details.
