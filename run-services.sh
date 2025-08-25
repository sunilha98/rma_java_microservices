#!/bin/sh
set -e

echo "Starting Postgres..."
su - postgres -c "/usr/lib/postgresql/15/bin/pg_ctl -D /var/lib/postgresql/data -l logfile start"

# Wait for DB to be ready
DB_HOST="localhost"
DB_PORT=5432

echo "Waiting for database to be ready..."
until nc -z -v -w30 $DB_HOST $DB_PORT
do
  echo "Waiting for database connection..."
  sleep 5
done

echo "Database is ready! Running init script..."
psql -U postgres -d maindb -f /docker-entrypoint-initdb.d/init-db.sql

echo "Starting Eureka Server..."
java -jar eureka-server.jar &
sleep 15

echo "Starting other microservices..."
java -jar master-resource-service.jar &
java -jar project-sow-service.jar &
java -jar reporting-integration-service.jar &
java -jar resource-allocation-service.jar &
java -jar user-mgmt-service.jar &
java -jar api-gateway.jar &

wait
