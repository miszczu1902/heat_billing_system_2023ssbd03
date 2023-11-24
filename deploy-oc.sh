#!/bin/bash

# Sprawdzenie czy podano argument
if [ $# -eq 0 ]; then
    echo "Nie podano tokenu."
    exit 1
fi

# Ustawienie zmiennej środowiskowej na podstawie przekazanego argumentu
export TOKEN_ARG=$1

# Ustawienie zmiennych środowiskowych
export MAVEN_OPTS="-Dquarkus.kubernetes-client.api-server-url=https://api.okd.cti.p.lodz.pl:6443 \
-Dquarkus.kubernetes-client.token=${TOKEN_ARG} \
-Dquarkus.kubernetes.deploy=true \
-Dquarkus.kubernetes-client.trust-certs=true \
-Dquarkus.openshift.route.expose=true \
-Dquarkus.container-image.group=tua202303 \
-Dquarkus.kubernetes.deployment-target=openshift"

# Wykonanie polecenia Maven
mvn clean install -DskipTests=true