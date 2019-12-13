FROM adoptopenjdk/maven-openjdk11:latest

RUN apt-get update
RUN apt-get -yq clean
RUN apt-get --yes --force-yes install firefox

RUN groupadd -g 985 rhsvc && \
    useradd -m -r -u 985 -g rhsvc rhsvc

ENV M2_HOME=/home/rhsvc/.m2
RUN mkdir -p /home/rhsvc/.m2/repository
COPY . /home/rhsvc/census-cc-cucumber
RUN chown -R rhsvc:rhsvc /home/rhsvc/census-cc-cucumber
COPY .maven.settings.xml /home/rhsvc/.m2/settings.xml
WORKDIR /home/rhsvc/census-cc-cucumber
USER rhsvc
CMD [ "mvn", "install", "-Dmaven.repo.local=m2/repository -DskipTests"]
