# Docker Image for gatling-blueprint-project
#
# - Create a Gatling installation in "/usr/share/gatling"
# - Install additional libraries for gatling-blueprint-project
#
# > docker build -t gatling-blueprint-project:3.1.3 .
# > docker run --rm -it --entrypoint=/bin/bash gatling-blueprint-project:3.1.3
# > docker run gatling-blueprint-project:3.1.3 /bin/bash -c "cd /usr/share/gatling; ./bin/gatling.sh -s computerdatabase.BasicSimulation"; echo "$?"


FROM openjdk:8-jdk-stretch

ARG	GATLING_VERSION=3.1.2
ARG GATLING_HOME=/usr/share/gatling
ARG MAVEN_REPO_URL=https://repo1.maven.org/maven2

ENV GATLING_VERSION=$GATLING_VERSION
ENV GATLING_HOME=$GATLING_HOME

RUN apt-get update && \ 
	apt-get install -y wget mc && \
	apt-get clean && \
	mkdir $GATLING_HOME && \
	wget ${MAVEN_REPO_URL}/io/gatling/highcharts/gatling-charts-highcharts-bundle/${GATLING_VERSION}/gatling-charts-highcharts-bundle-${GATLING_VERSION}-bundle.zip -O /tmp/gatling.zip && \
	unzip /tmp/gatling.zip -d /tmp && \
	mv /tmp/gatling-charts-highcharts-bundle-${GATLING_VERSION}/* $GATLING_HOME && \
	rm /tmp/gatling.zip && \
 	mkdir $GATLING_HOME/target && \
 	chmod 555 $GATLING_HOME/bin/*.sh && \
 	chmod -R 777 $GATLING_HOME/target && \
 	chmod -R 777 $GATLING_HOME/results

RUN rm -rf ${GATLING_HOME}/conf && \
	wget ${MAVEN_REPO_URL}/io/advantageous/boon/boon-json/0.6.6/boon-json-0.6.6.jar -O $GATLING_HOME/lib/boon-json-0.6.6.jar && \
	wget ${MAVEN_REPO_URL}/io/advantageous/boon/boon-reflekt/0.6.6/boon-reflekt-0.6.6.jar -O $GATLING_HOME/lib/boon-reflekt-0.6.6.jar && \
	wget ${MAVEN_REPO_URL}/com/github/sgoeschl/gatling/gatling-blueprint-extensions/2.2.6.1/gatling-blueprint-extensions-2.2.6.1.jar -O $GATLING_HOME/lib/gatling-blueprint-extensions-2.2.6.1.jar

COPY ./conf ${GATLING_HOME}/conf
