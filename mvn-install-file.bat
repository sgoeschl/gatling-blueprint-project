# Helper script to import libraries not found in the global Maven repo 

call mvn install:install-file -Dfile=./lib/gatling-blueprint-extensions-2.2.3.7.jar -Dversion=2.2.3.7 -DgroupId=org.github.sgoeschl.gatling -DartifactId=gatling-blueprint-extensions -Dpackaging=jar -DcreateChecksum=true -DpomFile=./lib/gatling-blueprint-extensions-2.2.3.7.pom
