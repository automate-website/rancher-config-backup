# Rancher Config Backup

[![Build Status](https://travis-ci.org/automate-website/rancher-config-backup.svg?branch=master)](https://travis-ci.org/automate-website/rancher-config-backup)
[![codecov.io](https://codecov.io/github/automate-website/rancher-config-backup/coverage.svg?branch=master)](https://codecov.io/github/automate-website/rancher-config-backup?branch=master)
![Docker Automated build](https://img.shields.io/docker/automated/automatewebsite/rancher-config-backup.svg)
[![Docker Hub](https://img.shields.io/docker/pulls/automatewebsite/rancher-config-backup.svg)](https://hub.docker.com/r/automatewebsite/rancher-config-backup) 

Retrieves and stores rancher stack configurations within a git repository for any accessible environments. May be used to backup the stack configurations periodically. 

## Usage

### On Demand
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

### Periodical

Below is a GitLab-CI configuration (`.gitlab-ci.yml`) to perform scheduled backups:

```
image: docker:latest

services:
  - docker:dind

build:
  stage: build
  script:
    - |
      docker run --rm \
        -e "RANCHER_BASE_URL=$RANCHER_BASE_URL" \
        -e "RANCHER_ACCOUNT_KEY=$RANCHER_ACCOUNT_KEY" \
        -e "RANCHER_SECRET_KEY=$RANCHER_SECRET_KEY" \
        -e "GIT_REPOSITORY_URL=$GIT_REPOSITORY_URL" \
        -e "GIT_USER=$GIT_USER" \
        -e "GIT_PASSWORD=$GIT_PASSWORD" \
        automatewebsite/rancher-config-backup:latest
  only:
    - schedules
```

Note: Related environment variables must be configured at the project/group level as well as the desired schedule
(e.g. once a day).

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

See the [LICENSE](https://github.com/automate-website/rancher-config-backup/blob/master/LICENSE) file for details.
