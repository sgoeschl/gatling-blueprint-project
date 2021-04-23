#!/bin/sh

# Run all the samples being documented 

mvn clean package

echo "================================================================================"
echo "Computer Database BasicSimulation"
echo "================================================================================"
mvn -Dgatling.simulationClass=computerdatabase.BasicSimulation gatling:test
echo "--- FINISHED ---"
sleep 5

echo "================================================================================"
echo "Computer Database AdvancedSimulationStep05"
echo "================================================================================"
mvn -Dgatling.simulationClass=computerdatabase.advanced.AdvancedSimulationStep05 gatling:test
echo "--- FINISHED ---"
sleep 5

echo "================================================================================"
echo "Computer Database Smoketest Blueprint"
echo "================================================================================"
mvn -Dgatling.simulationClass=computerdatabase.gatling.smoketest.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "================================================================================"
echo "GitHub API Functional Blueprint"
echo "================================================================================"
mvn -Dgatling.simulationClass=githubapi.github.functional.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "================================================================================"
echo "Postman API Blueprint"
echo "================================================================================"
mvn -Dgatling.simulationClass=postman.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "================================================================================"
echo "PostMan API using Using Minimal POM"
echo "================================================================================"
mvn -f pom-minimal.xml -Dgatling.simulationClass=postman.Test gatling:test
echo "--- FINISHED ---"
sleep 5
