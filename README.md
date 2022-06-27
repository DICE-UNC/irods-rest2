<h1 align="center">iRODS REST API V2</h1> <br>

<p align="center">
 iRODS REST API V2
</p>


## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Building](#building)
- [Testing](#testing)
- [API](#requirements)



## Introduction

iDRS provide an implementation of the GA4GH Data Repository Service for iRODS (http://www.irods.org)

This release implements the 1.2.0 DRS specification (https://ga4gh.github.io/data-repository-service-schemas/preview/release/drs-1.2.0/openapi.yaml)


## Features


## Requirements

The application can be run locally or in a docker container, the requirements for each setup are listed below.


### Local


### Docker

* [Docker](https://www.docker.com/get-docker)

### Run Local



### Run Docker Compose

## Building

This is a Spring Boot application written in the Java language. iDRS utilizes the Jargon libraries (https://github.com/DICE-UNC/jargon) as a 'zero install' client. Maven is used as the build environment.

In order to build with Maven, the build process must be able to download maven artifacts from the GitHub package repository. In order to do this, a public, read-only token is currently required (a github limitation). 

The ./docker/settings.xml provides necessary profiles and other configuration that can be added to your local maven settings that will allow building of the iDRS packages. This is a good option when doing development.

* Copy docker/settings.xml information into your local ~/.m2/settings.xml or activate it as a profile on the command line using the --global-settings option
* from the top directory of the repository, issue the command '''mvn install -DskipTests''' to build the packages, skipping the unit tests. See (#testing) for instructions on running the unit tests.

Optionally, the included maven docker image may be used for the Maven build process. See the instructions below in (#testing) for information on starting the maven container. Once the maven container is running, it mounts the source directory and provides the necessary configuration for accomplishing a build.


## Testing


JUnit testing of the components of the DRS iRODS implementation follow the approach of the underlying Jargon libraries. See this general guide: https://github.com/DICE-UNC/jargon/blob/master/docker-test-framework/DOCKERTEST.md

Maven is used for building, and has a capability to generate testing.properties files that are used by the unit testing framework. There is a standard settings.xml profile that can be activated and will work with the dockerized iRODS testing framework. There is a self-contained iRODS and build/test Docker image in the docker subdirectory, or the unit tests can work with the same framework under [Jargon](https://github.com/DICE-UNC/jargon/tree/master/docker-test-framework). We ill focus on the self-contained iRODS and build/test container.

In the docker subdirectory is an iRODS docker image and build/test image that can be used to build and test without any further requirements on the local environment.

* cd to the 'docker' subdirectory
* issue '''docker-compose build''' to build the iRODS and build/test containers
* issue '''docker-compose up -V''' to start the iRODS and build/test images
* execute the command '''docker exec -it maven bash''' to enter a session in the build/test image
* inside the build/test image, issue '''cd /usr/src/irods-data-repository-service''' to get to the mounted source directory
* execute '''mvn install''' to build and test your source code (which is mounted into the docker image, so it can be re-run interactively) against the running iRODS Docker image


Here a docker 'ps' command shows the framework running

```
conwaymc@ALMBP02246093 ~ % docker ps
CONTAINER ID   IMAGE                           COMMAND                  CREATED         STATUS         PORTS                              NAMES
5a0232682c14   docker_maven                    "/usr/local/bin/mvn-…"   9 minutes ago   Up 9 minutes                                      maven
a1701b2454a0   docker_irods-catalog-provider   "./start_provider.sh"    9 minutes ago   Up 9 minutes   0.0.0.0:1247->1247/tcp, 1248/tcp   irods-catalog-provider
```

Here a bash session is established in the maven container

```
conwaymc@ALMBP02246093 ~ % docker exec -it maven bash
root@5a0232682c14:/#

```

Here we cd into the mounted source directory, ready to issue maven commands

```
# pwd
/usr/src/irods-data-repository-service
# ls
LICENSE.txt  README.md	docker	eclipse-formatting.xml	etc  ga4gh-bundle-management-service  ga4gh-common  ga4gh-console  ga4gh-drs-service  pom.xml  sample-settings.xml  swagger
#
```






