curl -v -X POST http://devtenant1:8081/api/haccp/menu/category --header "Authorization: Bearer "$access_token --header 'Content-Type: application/json' --data-raw '{
  "name": "string",
  "menuCardPos": 0
}'