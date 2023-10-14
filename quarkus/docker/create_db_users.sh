#!/bin/bash
# Tworzenie użytkowników
mysql -u ssbd03 -pssbd03 -e "CREATE USER 'ssbd03admin'@'%' IDENTIFIED BY '9LUoYTSMH';"
mysql -u ssbd03 -pssbd03 -e "CREATE USER 'ssbd03auth'@'%' IDENTIFIED BY 'KHgXydJUv';"
mysql -u ssbd03 -pssbd03 -e "CREATE USER 'ssbd03mok'@'%' IDENTIFIED BY 'CHqZxv5R1';"
mysql -u ssbd03 -pssbd03 -e "CREATE USER 'ssbd03mow'@'%' IDENTIFIED BY 'obSjEBGaX';"

# Nadawanie uprawnień SUPERUSER (MariaDB nie ma dokładnie tej samej funkcji co PostgreSQL)
mysql -u ssbd03 -pssbd03 -e "GRANT ALL PRIVILEGES ON *.* TO 'ssbd03admin'@'%';"
mysql -u ssbd03 -pssbd03 -e "GRANT ALL PRIVILEGES ON *.* TO 'ssbd03auth'@'%';"
mysql -u ssbd03 -pssbd03 -e "GRANT ALL PRIVILEGES ON *.* TO 'ssbd03mok'@'%';"
mysql -u ssbd03 -pssbd03 -e "GRANT ALL PRIVILEGES ON *.* TO 'ssbd03mow'@'%';"

# Tworzenie bazy danych i importowanie danych
mysql -u ssbd03 -pssbd03 -e "CREATE DATABASE IF NOT EXISTS ssbd03"


##!/bin/bash
#psql -c "CREATE USER ssbd03admin IDENTIFIED BY '9LUoYTSMH';ALTER USER ssbd03admin WITH SUPERUSER;"
#psql -c "CREATE USER ssbd03auth IDENTIFIED BY 'KHgXydJUv';ALTER USER ssbd03auth WITH SUPERUSER;"
#psql -c "CREATE USER ssbd03mok IDENTIFIED BY 'CHqZxv5R1';ALTER USER ssbd03mok WITH SUPERUSER"
#psql -c "CREATE USER ssbd03mow IDENTIFIED BY 'obSjEBGaX';ALTER USER ssbd03mow WITH SUPERUSER;"
#psql -d ssbd03 -f /createDB.sql
#psql -d ssbd03 -f /init.sql