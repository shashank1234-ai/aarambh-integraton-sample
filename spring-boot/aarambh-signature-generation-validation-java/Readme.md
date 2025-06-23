### Java Util for Subscribing and Key Generation

- Clone the repo
- You are required to have Java 17 and Maven.
- Run ```./mvnw spring-boot:run```

### Create Auth Header 
To generate the auth header kindly use the following curl request:
```
curl --location 'localhost:8080/create-header' \
--header 'Content-Type: application/json' \
--header 'Cookie: connect.sid=s%3AASiu2zTqhIjkxj8OGpBcEk9MUjWPKWhy.i%2FMc29ueVdeXM96cLCESAVB5ul2yfVrZviJDEKHKVA0' \
--data-raw '{"value":{"test":"test"}},
"subscriber_id" : "abc.com",
"unique_key_id" : "ukid",
"private_key":"private_key"
}'
```

To Verify Auth Header
```
curl --location 'localhost:8080/verify-header' \
--header 'Content-Type: application/json' \
--header 'Cookie: connect.sid=s%3AASiu2zTqhIjkxj8OGpBcEk9MUjWPKWhy.i%2FMc29ueVdeXM96cLCESAVB5ul2yfVrZviJDEKHKVA0' \
--data-raw '{"value":{"test":"test"}},"public_key":"public_key","header":"Signature keyId=\"abc.com|ukid|ed25519\",algorithm=\"ed25519\",created=\"1712239689\",expires=\"1712539689\",headers=\"(created) (expires) digest\",signature=\"Gy5wiiJYGeNOBsiXJKo4OF7fSKR65zkxa/FJjgBgenmRplhq9vNewz/ivXDFegSnrdQK9U9T19Ta55J7Aa6RBw==\""
}'
```
