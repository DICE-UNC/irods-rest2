FROM tomcat:jre8-alpine
LABEL organization="NIEHS"
LABEL maintainer="mike.conway@nih.gov"
LABEL description="iRODS REST V2"
ADD runit.sh /

ADD target/irods-rest2-1.0.0.jar /irods-rest2-1.0.0.jar
ENTRYPOINT ["/runit.sh"]
#CMD ["/runit.sh"]



# build: docker build -t diceunc/ga4gh-dos:0.0.1 .

# run:  docker run -d --rm -p 8080:8080 -v /etc/irods-ext:/etc/irods-ext  --add-host irods420.irodslocal:172.16.250.101 diceunc/ga4gh-dos:0.0.1
