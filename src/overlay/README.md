# Gatling Blueprint Project

## Overview

Blue-print project to provide IDE support for writing & testing Gatling script with the option of creating a
stand-alone Gatling distribution.

What are the benefits of this project

* You and run & debug your Gatling tests in the IDE of your choice (in my case IntelliJ)
* You can run the Gatling tests on the Maven command line
* You can package your Gatling tests in a distributable which requires only Java 1.8 and Ant 1.9.x to run

## Tips & Tricks

* You can debug your Gatling scenario using `Engine` with the VM options `-Dgatling.core.simulationClass=computerdatabase.BasicSimulation`
* See [http://gatling.io/docs/2.2.2/extensions/maven_plugin.html](http://gatling.io/docs/2.2.2/extensions/maven_plugin.html)

## Running Gatling With Maven

> mvn -Dgatling.simulationClass=computerdatabase.tenant.smoketest.Test clean gatling:test
> mvn -Dgatling.simulationClass=computerdatabase.tenant.functional.Test clean gatling:test

## Running the Standalone Gatling Distribution

> ./bin/gatling.sh -s computerdatabase.tenant.smoketest.Test
> ./bin/gatling.sh --simulation computerdatabase.tenant.smoketest.Test

