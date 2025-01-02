# Twitter-Spotify-Trend-Analyzer

##### Spring-boot application consuming spotify's REST API to show user's
* Top played artists of all time/past 6 months/past month

## Tech Stack Used
* Java 17
* Spring Boot 3.4.1
* Spring MVC
* Thymeleaf
* Lombok
* RestTemplate

---

## Local Setup

* Install Java 17
* Install Maven

Recommended way is to use [sdkman](https://sdkman.io/) for installing both maven and java

```
sudo su
```

```
sudo apt update
```

```
sudo apt install zip unzip
```

```
curl -s "https://get.sdkman.io" | bash
```

```
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Install Java 15

```
sdk install java 17.0.1-open
```

Intsall Maven

```
sdk install maven
```

Spotify App Setup

* Create A New App In Spotify Developers <a href="https://developer.spotify.com/dashboard" target="_blank">Console</a>
* Set Redirect URL to http://localhost:8080/responseFromSpotify
* Copy the client-id generated for the above app along with the client-secret and redirect-uri and configure them in application.properties file

```
spotify.client.id=<Client-id here>
spotify.client.secret=<Client-Secret here>
spotify.redirect.url=<Redirect-URI here>
```


Run the below commands in the core

```
mvn clean install
```

```
mvn spring-boot:run
```

server port is configured to 8080 which can be changed in application.properties file

Go to the below url to view application

```
http://localhost:8080
```
