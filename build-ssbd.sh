#!/usr/bin/bash
cd front/ssbd03front &&
npm install &&
npm run build &&
docker-compose -f ../../quarkus/docker-compose.yml up