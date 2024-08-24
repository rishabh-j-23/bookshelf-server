export $(cat .env | xargs)
java -jar './target/bookshelf-server-0.0.1-SNAPSHOT.jar'