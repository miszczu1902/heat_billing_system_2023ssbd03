#!/bin/bash
if [ $# -eq 0 ]; then
    echo "Nie podano tokenu."
    exit 1
fi
export TOKEN_ARG=$1
export MAVEN_OPTS="-Dquarkus.kubernetes-client.token=${TOKEN_ARG} -Dquarkus.kubernetes.deploy=true"
mvn clean install -DskipTests=true