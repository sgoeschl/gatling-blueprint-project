#!/bin/sh

# Run all the samples being documented 

mvn clean package

echo "\n================================================================================"
echo "Computer Database BasicSimulation"
echo "================================================================================\n"
mvn -Dgatling.simulationClass=computerdatabase.BasicSimulation gatling:test
echo "--- FINISHED ---"
sleep 5

echo "\n================================================================================"
echo "Computer Database AdvancedSimulationStep05"
echo "================================================================================\n"
mvn -Dgatling.simulationClass=computerdatabase.advanced.AdvancedSimulationStep05 gatling:test
echo "--- FINISHED ---"
sleep 5

echo "\n================================================================================"
echo "Computer Database Smoketest Blueprint"
echo "================================================================================\n"
mvn -Dgatling.simulationClass=computerdatabase.gatling.smoketest.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "\n================================================================================"
echo "GitHub API Functional Blueprint"
echo "================================================================================\n"
mvn -Dgatling.simulationClass=githubapi.github.functional.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "\n================================================================================"
echo "Postman API Blueprint"
echo "================================================================================\n"
mvn -Dgatling.simulationClass=postman.Test gatling:test
echo "--- FINISHED ---"
sleep 5

echo "\n================================================================================"
echo "PostMan API using Using Minimal POM"
echo "================================================================================\n"
mvn -Dgatling.simulationClass=postman.Test gatling:test
echo "--- FINISHED ---"
sleep 5
