**TEMPLATE JAVA**

Template to add new microservices in the microservice chatbot architecture in Java language.

Usage:

- The interaction should be declared in bots/elena/aiml/interaction.aiml following the AIML format.
- The tasks performed by this microservice architecture should be defined in src/main/java/resource/Resources.java file.
- The certificates, the jwt/security.conf file, the pom.xml, the Dockerfile and the files/application.properties file should be configured as per the requirements.
- The Docker container should be built to add the new microservices into the architecture.

Example Docker usage:

- $ docker build -t yourDockerHub/nameMicroservice:version .
- $ docker push yourDockerHub/nameMicroservice:version

pom.xml: contains information about the project and configuration details used by Maven to build the project.

Dockerfile: is the file that creates the Docker container.

requirements.txt: list of the requirements for the Python environment.

bots: contains the AIML file.

files: contains the configuration file and the certificates used in the microservice.

jwt: contains the code necessary for the connection with the authentication server.

lib: contains the libraries that the microservice uses.

src/main/java/auth: contains the code necessary for the authentication.

src/main/java/core: contains the registration and deregistration in the API gateway.

src/main/java/objects: contains the objects used in the microservice.

src/main/java/resource: contains the tasks that this microservice is going to do.

This template works with FHIR DSTU3 server, to use it, follow this instructions:

- Download the last version of hapi-fhir-cli from https://github.com/jamesagnew/hapi-fhir/releases 
- Add the file hapi-fhir-cli.jar in lib folder with the name hapi-fhir-cli-1.0.jar

For more details please write an email to surya@unizar.es