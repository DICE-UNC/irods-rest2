FROM adoptopenjdk/openjdk11
LABEL organization="NIEHS"
LABEL maintainer="mike.conway@nih.gov"
LABEL description="iRODS REST V2"
ADD runit.sh /

ADD target/irods-rest2-1.0.0.jar /irods-rest2-1.0.0.jar
ENTRYPOINT ["/runit.sh"]
#CMD ["/runit.sh"]



# build: docker build -t michaelconway/irods-rest2:1.0.0 .

# run:  docker run -d --rm -p 8080:8080 -v /etc/irods-ext:/etc/irods-ext   michaelconway/irods-rest2:1.0.0
