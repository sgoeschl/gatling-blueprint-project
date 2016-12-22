# Gatling Blueprint Project

## 1. Overview

* Provide out-of-the-box IDE support for writing & testing Gatling script 
* Implement pretty-printing and filtering of JSON responses
* Support multi-tenant & multi-site Gatling tests from IDE, Maven and shell script
* Create a stand-alone Gatling distribution requiring only Java 1.8 and Ant 1.9.x

## 2. Motivation

During my work at [Erste Group](https://www.erstegroup.com) I came across interesting test scenarios such as

* End-to-End performance testing covering multiple REST APIs
* Support of multiple tenants with different test scripts and test data
* Support of multiple test environments such as DEV, FAT, UAT & PROD
* Some functional testing flavour based on comparing current with expected JSON responses
* Smoke tests afer a deployment using existing performance tests

In case you are still interested here are list of links with background information

* [http://people.apache.org/~sgoeschl/presentations/2016/testworks/gatling.pdf](http://people.apache.org/~sgoeschl/presentations/2016/testworks/gatling.pdf)
* [https://huddle.eurostarsoftwaretesting.com/gatling-tales-from-a-journey/](https://huddle.eurostarsoftwaretesting.com/gatling-tales-from-a-journey/)

## 3. Scope

* Since my Scala skill are rudimentary at best I moved support code into a [seperate Java project](https://github.com/sgoeschl/gatling-blueprint-extensions)
* There is no single Gatling setup to rule them all (at least I'm not able to provide one) but this project can help with some commonly used functionality
* It is assumed that you will challenge and/or change some/many of my design decisison and you are free to change it - hence it is called blueprint. Having said tht constructive feedback is appreciated and will improve the project in the long run

## 4. The Mental Domain Model

* We define four configuration dimensions
    * `tenant`
    * `application`
    * `site`
    * `scope`
* The dimension `tenant`, `application` and `scope` map to `tenant.application.scope.Test` which is the Gatling script to be executed
* The dimension `site` is passed as system property and is used to pick up the correct configuration information from `./user-files/data`
* It is assumed that testing with different `site` parameter does not require different Gatling tests
* The configuration information is stored in `environment.properties` files in a hierarchical directory layout
* Other configuration files, e.g. CSV files, can be picked from a hierarchical directory layout

## 5. Code Samples

In order to make things understandable there are two sample project provided - both are a bit artificial but demonstrate many key points

### 5.1 Computer Database

The Gatling working horse ...

* Assuming the following coordinates `computerdatabase-tenant-${scope}-local`
    * The tests are targeting the application `computerdatabase`
    * Since we have no particular tenant we just assume the default value `tenant`
    * There are two provided scopes - `smoketest` and `functional`
    * We also assume that the site is called `local` (to be on the save side)
* Those two test map into `computerdatabase.tenant.functional.Test` and `computerdatabase.tenant.smoketest.Test`
* Each test scenario has its own `search.csv`
* Each test scenario runs different test as defined in `ComputerDatabaseChainBuilder`
* Each test scenario runs different user scenario derived from `environment.properties`

#### Running Tests From The IDE

Invoking tests with the IDE provide the following system properties

* -Dgatling.core.simulationClass=computerdatabase.tenant.smoketest.Test
* -Dgatling.core.simulationClass=computerdatabase.tenant.functional.Test

#### Running Tests From The Maven Command Line

> mvn -Dgatling.simulationClass=computerdatabase.tenant.smoketest.Test gatling:test

```
Coordinates: {application='computerdatabase', tenant='tenant', site='local', scope='smoketest'}
Environment: {simulation.users.rampup=0, simulation.users=1, simulation.loops=1, simulation.try.max=1, simulation.pause.ms=1000, computerdatabase.base.url=http://computer-database.gatling.io, simulation.duration=300}
Simulation: (users=1, duration=300 seconds, usersRampup=0 seconds, loops=1, tryMax=1)
Simulation computerdatabase.tenant.smoketest.Test started...

================================================================================
2016-12-22 19:52:40                                           2s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=7      KO=0     )
> Home                                                     (OK=2      KO=0     )
> Home Redirect 1                                          (OK=2      KO=0     )
> Search                                                   (OK=1      KO=0     )
> Select                                                   (OK=1      KO=0     )
> Page 0                                                   (OK=1      KO=0     )

---- computerdatabase-tenant-local-smoketest -----------------------------------
[##########################################################################]100%
          waiting: 0      / active: 0      / done:1     
================================================================================
```


> mvn -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test

```
Coordinates: {application='computerdatabase', tenant='tenant', site='local', scope='functional'}
Environment: {simulation.users.rampup=0, simulation.users=1, simulation.loops=1, simulation.try.max=1, simulation.pause.ms=1000, computerdatabase.base.url=http://computer-database.gatling.io, simulation.duration=300}
Simulation: (users=1, duration=300 seconds, usersRampup=0 seconds, loops=1, tryMax=1)
Simulation computerdatabase.tenant.functional.Test started...


================================================================================
2016-12-22 18:09:12                                          10s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=55     KO=0     )
> Home                                                     (OK=10     KO=0     )
> Home Redirect 1                                          (OK=10     KO=0     )
> Search                                                   (OK=5      KO=0     )
> Select                                                   (OK=5      KO=0     )
> Page 0                                                   (OK=5      KO=0     )
> Page 1                                                   (OK=5      KO=0     )
> Page 2                                                   (OK=5      KO=0     )
> Page 3                                                   (OK=5      KO=0     )
> Page 4                                                   (OK=5      KO=0     )

---- computerdatabase-tenant-local-functional ----------------------------------
[##########################################################################]100%
          waiting: 0      / active: 0      / done:5     
================================================================================
```


## Tips & Tricks

* You can debug your Gatling scenario using `Engine` with the VM options `-Dgatling.core.simulationClass=computerdatabase.BasicSimulation`
* See [http://gatling.io/docs/2.2.2/extensions/maven_plugin.html](http://gatling.io/docs/2.2.2/extensions/maven_plugin.html)

## Running Gatling With Maven

> mvn -Dgatling.simulationClass=computerdatabase.tenant.smoketest.Test clean gatling:test
> mvn -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test
> mvn -Dlogback.configurationFile=conf/logback.xml -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test

## Running the Standalone Gatling Distribution

> ./bin/gatling.sh -s computerdatabase.tenant.smoketest.Test
> ./bin/gatling.sh --simulation computerdatabase.tenant.smoketest.Test

