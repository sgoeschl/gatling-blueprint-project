# Gatling Blueprint Project

## Build Status

Thanks to Travis for the [build status](https://travis-ci.org/sgoeschl/gatling-blueprint-project): 
[![Build Status](https://travis-ci.org/sgoeschl/gatling-blueprint-project.svg?branch=master)](https://travis-ci.org/sgoeschl/gatling-blueprint-project)

## 1. Overview

* Provide out-of-the-box IDE support for writing & debugging Gatling script 
* Run multi-tenant & multi-site Gatling tests from IDE, Apache Maven and shell script
* Create a stand-alone Gatling distribution requiring only Java 1.8 and optionally Apache Ant 1.9.x 
* Implement pretty-printing and custom filtering of JSON responses

### 1.1 Words Of Caution

* Please note that this project depends on [sgoeschl/gatling-blueprint-extensions](https://github.com/sgoeschl/gatling-blueprint-extensions) so you need to install the libraries first
    * In `./lib there is a `mvn-install-file.bat` and `mvn-install-file.sh` to import the library manually
    * Clone the Git repository and runnning `mvn clean install`
* The code is currently re-factored frequently :-)

## 2. Motivation

During my work at [Erste Group](https://www.erstegroup.com) I came across interesting test scenarios such as

* End-to-End performance testing spanning multiple REST APIs
* Support of multiple tenants with different test scripts and test data
* Support of multiple test environments such as DEV, FAT, UAT & PROD with different configuration data
* Some functional testing flavor comparing current with expected JSON responses
* Smoke tests after a deployment using existing performance tests

In case you are still interested here are list of links with background information

* [http://people.apache.org/~sgoeschl/presentations/2016/testworks/gatling.pdf](http://people.apache.org/~sgoeschl/presentations/2016/testworks/gatling.pdf)
* [https://huddle.eurostarsoftwaretesting.com/gatling-tales-from-a-journey/](https://huddle.eurostarsoftwaretesting.com/gatling-tales-from-a-journey/)

## 3. Scope

* Since my Scala skill are rudimentary at best I moved support code into a [seperate Java project](https://github.com/sgoeschl/gatling-blueprint-extensions)
* There is no single Gatling setup to rule them all (at least I'm not able to provide one) but this project can help with some commonly used functionality
* It is assumed that you will challenge and/or change some/many of my design decision and you are free to do so - hence it is called blueprint. Having said that constructive feedback is highly appreciated and will improve my pet project in the long run.

## 4. The Mental Domain Model

* We define four configuration dimensions
    * `tenant`
    * `application`
    * `site`
    * `scope`
* The dimension `tenant`, `application` and `scope` map to `application.tenant.scope.Test` which is the Gatling script to be executed
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

Invoking the `Engine` class within the IDE with one of the following system properties

* -Dgatling.core.simulationClass=computerdatabase.tenant.smoketest.Test
* -Dgatling.core.simulationClass=computerdatabase.tenant.functional.Test

A sample screenshot from the IntelliJ Community Edition

![](./src/site/image/start-gatling.in-intellij.png)

#### Running Tests From The Maven Command Line

Fist we start the smoke test using

> mvn -Dgatling.simulationClass=computerdatabase.tenant.smoketest.Test gatling:test

```
Coordinates: {application='computerdatabase', tenant='tenant', site='local', scope='smoketest'}
Environment: {simulation.pause.ms=100, computerdatabase.base.url=http://computer-database.gatling.io}
Simulation: (usersAtOnce=1, users=1, usersRampup=0 seconds, duration=300 seconds, loops=1, tryMax=1, pause=100 milliseconds)
Data Directory: /Users/sgoeschl/work/github/sgoeschl/gatling-blueprint-project/user-files/data
Result Directory: /Users/sgoeschl/work/github/sgoeschl/gatling-blueprint-project/target/gatling
Resolve file 'search.csv' to 'tenant/local/computerdatabase/smoketest/search.csv'

================================================================================
2017-02-09 07:48:02                                           1s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=10     KO=0     )
> Home                                                     (OK=2      KO=0     )
> Home Redirect 1                                          (OK=2      KO=0     )
> Search                                                   (OK=2      KO=0     )
> Select                                                   (OK=2      KO=0     )
> Page 0                                                   (OK=2      KO=0     )

---- computerdatabase-tenant-local-smoketest -----------------------------------
[##########################################################################]100%
          waiting: 0      / active: 0      / done:2     
================================================================================
```

Afterwards we run the functional test flavor using

> mvn -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test

```
Coordinates: {application='computerdatabase', tenant='tenant', site='local', scope='functional'}
Environment: {simulation.pause.ms=1000, computerdatabase.base.url=http://computer-database.gatling.io, simulation.users.atonce=0}
Simulation: (usersAtOnce=0, users=1, usersRampup=0 seconds, duration=300 seconds, loops=1, tryMax=1, pause=1000 milliseconds)
Data Directory: /Users/sgoeschl/work/github/sgoeschl/gatling-blueprint-project/user-files/data
Result Directory: /Users/sgoeschl/work/github/sgoeschl/gatling-blueprint-project/target/gatling
Resolve file 'search.csv' to 'tenant/local/computerdatabase/functional/search.csv'

================================================================================
2017-02-09 07:51:43                                          49s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=45     KO=0     )
> Home                                                     (OK=5      KO=0     )
> Home Redirect 1                                          (OK=5      KO=0     )
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

### 5.2 GitHub REST API

A REST API example showing JSON response handling

* Assuming the following coordinates `github-tenant-functional-local`
* For a non-performance tests the JSON responses are pretty-printed and saved

```scala
object GitHubApi {

  val home: ChainBuilder = exec(http("Home")
    .get("/")
    .check(
      jsonPath("$").ofType[Any].find.saveAs("lastResponse")))
    .exec(session => {
      JsonResponseTool.saveToFile(session, "lastResponse", "github", "home")
      session
    })
}    
```    

> mvn -Dgatling.simulationClass=github.tenant.functional.Test clean gatling:test

```
Coordinates: {application='github', tenant='tenant', site='local', scope='functional'}
Environment: {simulation.pause.ms=1000, github.base.url=https://api.github.com}
Simulation: (usersAtOnce=1, users=1, usersRampup=0 seconds, duration=300 seconds, loops=1, tryMax=1, pause=1000 milliseconds)
Data Directory: /Users/sgoeschl/work/github/sgoeschl/gatling-blueprint-project/user-files/data
Result Directory: /Users/sgoeschl/work/github/sgoeschl/gatling-blueprint-project/target/gatling

================================================================================
2017-02-09 07:56:34                                           5s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=6      KO=0     )
> Home                                                     (OK=2      KO=0     )
> Users                                                    (OK=2      KO=0     )
> Events                                                   (OK=2      KO=0     )

---- github-tenant-local-functional --------------------------------------------
[##########################################################################]100%
          waiting: 0      / active: 0      / done:2     
================================================================================
```

After the test run you will see the following directory content

![GitHub JSON Response File](./src/site/image/github-json-reponse-files.png)

## 6. Tips And Tricks

### 6.1 General

* You can debug your Gatling scenario using `Engine` with the VM options `-Dgatling.core.simulationClass=computerdatabase.BasicSimulation`
* See [http://gatling.io/docs/2.2.2/extensions/maven_plugin.html](http://gatling.io/docs/2.2.2/extensions/maven_plugin.html)
* You can change the `logback` configuration using the `-Dlogback.configurationFile` system property

### 6.2 Running Gatling With Maven

Using the Maven integration is the preferred way when you run Gatling tests from a CI server such as Jenkins

```text
mvn -Dgatling.simulationClass=computerdatabase.tenant.smoketest.Test clean gatling:test
mvn -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test
mvn -Dlogback.configurationFile=conf/logback-debug.xml -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test
```

### 6.3 Running the Standalone Gatling Distribution Using Shell Scripts

On Linux/Unix you can easily execute tests with the existing shell scripts

```text
./bin/gatling.sh -s computerdatabase.tenant.smoketest.Test
./bin/gatling.sh --simulation computerdatabase.tenant.smoketest.Test
```

Please note that doing that on Windows has issues - you start a batch file which starts a Java process. But when you terminate the test run using `CTRL-C` you are actually killing the Windows Command Processor but the JVM - Windows does not terminate child processed.

### 6.4 Running the Standalone Gatling Distribution Using Apache Ant

Before you can use the Ant integration you need to create the stand-alone distribution as shown below

```text
mvn clean install
cd target/distributable/gatling-charts-highcharts-bundle-2.2.3/
ant -p
```

```text
ant info
ant -Dapplication=computerdatabase -Dscope=smoketest clean info test
ant -Dapplication=computerdatabase -Dscope=functional clean info test
ant -Dapplication=github -Dscope=functional clean info test
ant -Dapplication=github -Dscope=functional clean info record
ant -Dapplication=github -Dscope=functional clean info verify
```

In case of a failed Gatling tests the Ant script just stops- sometimes it is useful to fail later, e.g.

```
ant -Dapplication=computerdatabase -Dscope=smoketest info run archive copy-report fail-on-error

archive:
      [zip] Building zip: user-files/archive/computerdatabase-tenant-local-smoketest-20170330T224945.zip

copy-report:

gatling:copy-report:
     [copy] Copying 42 files to results/computerdatabase/tenant/local/smoketest/report

fail-on-error:

gatling:fail-on-error:

BUILD SUCCESSFUL
```

This Ant invocation

* Runs a Gatling tests
* Copy the Gatling report to an additional directory, e.g. to serve it from an web server
* Archive the Gatling report as ZIP file

## 7. Additional Information

### 7.1 More Online Resources

A must-read for all Scala/Gatling starters is stuff on [http://automationrhapsody.com/performance-testing-with-gatling](http://automationrhapsody.com/performance-testing-with-gatling)

### 7.2 Gatling Blueprint Configuration Properties

Commonly used configuration settings found in `environment.properties

| Property                  | Description                                                 |
|---------------------------|-------------------------------------------------------------|
| simulation.duration       | Duration of the simulation in seconds                       |
| simulation.pause.ms       | Milliseconds to pause                                       |
| simulation.try.max        | Number of retries before reporting an error                 |
| simulation.users          | Number of users of virtual users                            |
| simulation.users.atonce   | Number of users to start at once                            |
| simulation.users.rampup   | Rampup time in seconds                                      |
| simulation.loops          | Number of loops                                             |

Please note that not all of the properties might have an effect on your simulation.
