About URL Shortener
=================

URL Shortener is a webservice which can shorten URLs similar to TinyURL and bit.ly.

## Libraries used
- Spring Boot 2
- H2 Database
- Springfox Swagger 
- Hibernate
- Appache Commons
- Commons Validator
- SLF4J

# Installation

## Prerequisites 
- Installed JDK 11
- Maven 3

## Installation steps
1. Clone project from GitHub: `git clone https://github.com/lamaj-com/url-shortener.git`
2. If external property file used, it should be placed at location set by *spring.config.location* environment variable 
3. Navigate to the root directory of the project (where pom.xml is located) in CLI
3. Build the project with the following maven command: 
`mvn clean install -DskipTests=false`
Optional JVM argument skipTest could be used to enable/disable tests. To run tests only without building the project use:
`mvn test`

### Build artifact
After the above steps are executed, a new build artifact will be created under the 'target' directory with the name:
`url-shortener-${project.version}.jar`

# Starting the service
JAR artifact could be run via command line using the following command:
`java -jar url-shortener-${project.version}.jar`

## Docker
In case of using Docker follow the following instructions:
1. Navigate to the root directory of the project (where docker-compose.yml is located)
2. Build image if it does not exist and start the container by running the following command:
 `docker-compose up`

## API Documentation
Swagger API Documentation could be access on the following link:
`http://[hostname]:[port]/swagger-ui/`
If the default setup is used, it will be located at: http://localhost:8081/swagger-ui/

# Developers
@mlazic