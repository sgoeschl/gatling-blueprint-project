@ECHO OFF

REM Run all the samples being documented

CALL mvn clean package

ECHO "================================================================================"
ECHO "Computer Database BasicSimulation"
ECHO "================================================================================"
CALL mvn -Dgatling.simulationClass=computerdatabase.BasicSimulation gatling:test
ECHO "--- FINISHED ---"
timeout /T 5

ECHO "================================================================================"
ECHO "Computer Database AdvancedSimulationStep05"
ECHO "================================================================================"
CALL mvn -Dgatling.simulationClass=computerdatabase.advanced.AdvancedSimulationStep05 gatling:test
ECHO "--- FINISHED ---"
timeout /T 5

ECHO "================================================================================"
ECHO "Computer Database Smoketest Blueprint"
ECHO "================================================================================"
CALL mvn -Dgatling.simulationClass=computerdatabase.gatling.smoketest.Test gatling:test
ECHO "--- FINISHED ---"
timeout /T 5

ECHO "================================================================================"
ECHO "GitHub API Functional Blueprint"
ECHO "================================================================================"
CALL mvn -Dgatling.simulationClass=githubapi.github.functional.Test gatling:test
ECHO "--- FINISHED ---"
timeout /T 5

ECHO "================================================================================"
ECHO "Postman API Blueprint"
ECHO "================================================================================"
CALL mvn -Dgatling.simulationClass=postman.Test gatling:test
ECHO "--- FINISHED ---"
timeout /T 5

ECHO "================================================================================"
ECHO "PostMan API using Using Minimal POM"
ECHO "================================================================================"
CALL mvn -Dgatling.simulationClass=postman.Test gatling:test
ECHO "--- FINISHED ---"
timeout /T 5
