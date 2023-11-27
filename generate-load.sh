#!/bin/bash
execute_ab() {
    local token=$1
    local url="https://ssbd03-tua202303.apps.okd.cti.p.lodz.pl/api/accounts/self"
    if [ -z "$token" ]; then
        echo "Nie podano tokenu."
        exit 1
    fi
    while true; do
        ab -n 100 -c 100 -H "Authorization: Bearer $token" "$url"
        sleep 1
    done
}
TOKEN="$1"
execute_ab "$TOKEN"