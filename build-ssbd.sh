#!/usr/bin/bash
cd front/ssbd03front &&
npm install &&
npm run build &&
cd ../.. &&
mvn clean package -DskipTests=true &&
docker-compose up