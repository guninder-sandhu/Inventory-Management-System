#!/bin/sh

/wait-for-it.sh postgres:5432 --timeout=60
/wait-for-it.sh configserver:8084 --timeout=60
/wait-for-it.sh serviceregistry:8761 --timeout=60
/wait-for-it.sh stockservice:8083 --timeout=60
/wait-for-it.sh userservice:8081 --timeout=60
/wait-for-it.sh dashboard-service:8087 --timeout=60
/wait-for-it.sh product:8082 --timeout=60

exec java -jar /app/app.jar
