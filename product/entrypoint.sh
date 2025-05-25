#!/bin/sh

/wait-for-it.sh postgres:5432 --timeout=60
/wait-for-it.sh configserver:8084 --timeout=60
/wait-for-it.sh service-registry:8761 --timeout=60
/wait-for-it.sh stock-service:8083 --timeout=90

exec java -jar /app/app.jar
