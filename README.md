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

A partial REST API implementation to support iDRS - GA4GH Data Repository Service for iRODS (https://github.com/NIEHS/irods-data-repository-service)


## Features


## Requirements

The application can be run locally or in a docker container, the requirements for each setup are listed below.


### Local


### Docker

* [Docker](https://www.docker.com/get-docker)

### Run Local



### Run Docker

http://localhost:8888/swagger-ui.html#/auth


## Building

This is a Spring Boot application written in the Java language. It uses the Jargon libraries (https://github.com/DICE-UNC/jargon) as a 'zero install' client. Maven is used as the build environment.

In order to build with Maven, the build process must be able to download maven artifacts from the GitHub package repository. In order to do this, a public, read-only token is currently required (a github limitation). 


## Testing
