FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /2023ssbd03
COPY ../pom.xml /2023ssbd03/pom.xml
COPY ../src /2023ssbd03/src
RUN mvn clean package -DskipTests=true

#FROM payara/server-full:6.2023.3-jdk17
#COPY --from=builder /2023ssbd03/target/ssbd03-1.0.0.war /opt/payara/deployments/ssbd03-1.0.0.war

FROM jboss/wildfly
EXPOSE 8080 9090
COPY --from=builder /2023ssbd03/target/ssbd03-1.0.0.war /opt/jboss/wildfly/standalone/deployments/ssbd03-1.0.0.war
RUN /bin/bash -c '/opt/jboss/wildfly/bin/standalone.sh -b=0.0.0.0 -bmanagement=0.0.0.0 &'
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

