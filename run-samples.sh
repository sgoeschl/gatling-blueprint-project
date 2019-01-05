#!/bin/sh

# Run all the samples being documented 

mvn clean package

echo "Computer Database BasicSimulation"
mvn -Dgatling.simulationClass=computerdatabase.BasicSimulation gatling:test
echo "--- FINISHED ---"
sleep 5

echo "Computer Database AdvancedSimulationStep05"
mvn -Dgatling.simulationClass=computerdatabase.advanced.AdvancedSimulationStep05 gatling:test
echo "--- FINISHED ---"
sleep 5

echo "Computer Database Smoketest Blueprint"
mvn -Dgatling.simulationClass=computerdatabase.gatling.smoketest.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "GitHub API Functional Blueprint"
mvn -Dgatling.simulationClass=githubapi.github.functional.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "Postman API Blueprint"
mvn -Dgatling.simulationClass=postman.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "PostMan API using Using Minimal POM"
mvn -f pom-minimal.xml -Dgatling.simulationClass=postman.Test gatling:test
echo "--- FINISHED ---"
sleep 5
