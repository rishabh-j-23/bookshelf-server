export $(cat .env | xargs)
./mvnw clean package