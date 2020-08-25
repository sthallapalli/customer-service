#!/bin/sh

JQ_INSTALLED=$(jq --version)

RESPONSE=$(curl -s -X POST 'http://localhost:8080/api/v1/authenticate' -H 'Accept: application/json'  -H  'content-type: application/json' -d '{  "firstName": "test",  "password": "test"}')
TOKEN=$(echo $RESPONSE | jq '.jwt'| sed 's/"//g')

if [ '$JQ_INSTALLED' != '' ]; then
    echo '\n'
    echo 1.Listing the first 3 customers order by date of birth desc
    echo
    curl -s -X GET 'http://localhost:8080/api/v1/customer/search?sort=dateOfBirth,desc&page=0&size=3' -H 'Authorization: Bearer '$TOKEN'' | jq '.content'

    echo '\n'
    echo 2.Updating the test customer last name to : testupdated
    echo
    curl -s -X PUT 'http://localhost:8080/api/v1/customer' -H 'Authorization: Bearer '$TOKEN'' -H 'Content-Type: application/json' -H 'Accept: application/json' -d '{  "id": 1, "firstName": "test",  "lastName": "testupdated"}' | jq .

    echo '\n'
    echo 3.Fetching the updated customer by firstname : test
    echo
    curl -s -X GET 'http://localhost:8080/api/v1/customer/test' -H 'Authorization: Bearer '$TOKEN'' -H 'Content-Type: application/json' -H 'Accept: application/json' | jq .
else
    echo '\n'
    echo Please install json parsing tool JQ for using this script.
    echo '\n'
fi
