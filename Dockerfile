FROM adoptopenjdk/maven-openjdk11:latest

RUN apt-get update
RUN apt-get -yq clean
RUN apt-get --yes --force-yes install firefox

RUN groupadd -g 985 ccsvc && \
    useradd -m -r -u 985 -g ccsvc ccsvc

ENV M2_HOME=/home/ccsvc/.m2
RUN mkdir -p /home/ccsvc/.m2/repository
COPY . /home/ccsvc/census-cc-cucumber
RUN chown -R ccsvc:ccsvc /home/ccsvc/census-cc-cucumber
COPY .maven.settings.xml /home/ccsvc/.m2/settings.xml
WORKDIR /home/ccsvc/census-cc-cucumber
USER ccsvc
CMD [ "mvn", "verify", "-Dmaven.repo.local=m2/repository"]
