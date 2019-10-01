#Select base image
FROM debian:stretch

#Set some environment vars
ENV LANG C.UTF-8
ENV TZ Europe/Madrid
ENV DEBIAN_FRONTEND noninteractive

# Subress Upstart errors/warnings
RUN dpkg-divert --local --rename --add /sbin/initctl
RUN ln -sf /bin/true /sbin/initctl

# Update repository
RUN apt-get update --fix-missing

# Install Java
RUN echo "deb http://http.debian.net/debian stretch-backports main" | tee --append /etc/apt/sources.list
RUN apt-get update
RUN apt-get install -y -t stretch-backports openjdk-8-jdk
RUN java -version

# Intall maven
RUN apt-get install -y maven
RUN mvn -version

# Install Python 2.7.13
RUN apt-get install -y python2.7
RUN apt-get install -y python-pip python-dev build-essential
RUN pip install --upgrade pip

# Dependencies
ADD requirements.txt .
RUN pip install -r requirements.txt

# Set directory
WORKDIR /project
ADD files /project/files
ADD pom.xml /project/pom.xml
ADD foo.crt /project/foo.crt
ADD foo.ehealthz.es.crt /project/foo.ehealthz.es.crt
ADD foo.pem /project/foo.pem

# Add certs to java cacerts
RUN  echo "yes" | keytool -import -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts -file foo.crt -alias security -storepass changeit
RUN  echo "yes" | keytool -import -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts -file foo.ehealthz.es.crt -alias chatbot -storepass changeit
RUN  echo "yes" | keytool -import -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts -file foo.pem -alias foo -storepass changeit

# Add all the project
ADD bots /project/bots
ADD jwt /project/jwt
ADD lib /project/lib
ADD src /project/src

# Compile the application
RUN mvn compile

EXPOSE 10101

# Run the application
CMD ["mvn", "exec:java"]