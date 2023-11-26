#!/bin/bash

USERNAME="mariasilva"
PASSWORD="Password$123"
LOGIN_URL="http://ssbd03-tua202303.apps.okd.cti.p.lodz.pl/api/accounts/login"

TOKEN=$(curl -s -i -X POST -H "Content-Type: application/json" -d '{"username": "'$USERNAME'", "password": "'$PASSWORD'"}' $LOGIN_URL | grep -i "^Bearer:" | awk '{print $2}' | tr -d '\r\n')

execute_ab() {
      ab -n 100 -c 100 -H "Authorization: Bearer $TOKEN" http://ssbd03-tua202303.apps.okd.cti.p.lodz.pl/api/accounts/self
}
echo $TOKEN;
if [ -n "$TOKEN" ]; then
    execute_ab
else
    echo "Nie udało się uzyskać tokenu JWT. Sprawdź dane logowania lub adres logowania."
fi