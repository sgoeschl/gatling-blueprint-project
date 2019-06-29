#!/bin/bash -eu
#
# Helper script to run Gatling tests with a pre-installed Gatling, e.g. "/usr/share/gatling"-

export GATLING_HOME=${GATLING_HOME:=/usr/share/gatling}
export GATLING_PROJECT=$PWD
export JAVA_OPTS=

if [ -d ${GATLING_PROJECT/conf} ]; then
	export GATLING_CONF=${GATLING_PROJECT}/conf
else
	export GATLING_CONF=${GATLING_HOME}/conf
fi

SITE=local
SIMULATION=unknown

while [ "$#" -gt 0 ]; do
  case "$1" in
    "--site") JAVA_OPTS="$JAVA_OPTS -Dsite=$2"; SITE=$2; shift 2;;
    "--simulation") SIMULATION="$2"; shift 2;;
    *) die "unrecognized argument: $1"; shift 1;;
  esac
done

echo "== Starting Gatling simulation $SIMULATION ==="
echo "GATLING_CONF    : $GATLING_CONF"
echo "GATLING_HOME    : $GATLING_HOME"
echo "GATLING_PROJECT : $GATLING_PROJECT"
echo "JAVA_OPTS       : $JAVA_OPTS"
echo "SITE            : $SITE"

gatling.sh --binaries-folder ${GATLING_PROJECT}/target --run-description ${SIMULATION} --results-folder ${GATLING_PROJECT}/results/${SITE}/${SIMULATION} --simulation ${SIMULATION}