FROM java:8
MAINTAINER Corey Beres <cberes@cberes.com>

RUN apt-get update && apt-get install -y \
    wget \
    git-core

WORKDIR /opt/softwarebears
RUN git clone https://cberes@bitbucket.org/cberes/softwarebears-blog.git blog
COPY target/softwarebears-0.1.0-SNAPSHOT-standalone.jar softwarebears.jar

ENV PORT=3000
ENV SB_BLOG=/opt/softwarebears/blog
ENV SB_EMAIL_HOST=smtp.zoho.com
ENV SB_EMAIL_PORT=465
ENV SB_EMAIL_USER=cberes@cberes.com
ENV SB_EMAIL_PASS=

EXPOSE 80
CMD ["java", "-jar", "softwarebears.jar"]

