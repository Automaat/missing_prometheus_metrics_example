FROM openjdk:8-jdk-alpine

# working directory for gatling
WORKDIR /opt

# gating version
ENV GATLING_VERSION 3.2.1

# create directory for gatling install
RUN mkdir -p gatling

# install gatling
RUN apk add --update wget bash libc6-compat && \
  mkdir -p /tmp/downloads && \
  wget -q -O /tmp/downloads/gatling-$GATLING_VERSION.zip \
  https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/$GATLING_VERSION/gatling-charts-highcharts-bundle-$GATLING_VERSION-bundle.zip && \
  mkdir -p /tmp/archive && cd /tmp/archive && \
  unzip /tmp/downloads/gatling-$GATLING_VERSION.zip && \
  mv /tmp/archive/gatling-charts-highcharts-bundle-$GATLING_VERSION/* /opt/gatling/ && \
  rm -rf /tmp/* && \
  rm -rf /opt/gatling/conf && \
  rm -rf /opt/gatling/user-files/simulations

# change context to gatling directory
WORKDIR  /opt/gatling

# set directories below to be mountable from host
COPY configuration ./conf
COPY simulations ./user-files/simulations
VOLUME ["/opt/gatling/results"]

# set environment variables
ENV PATH /opt/gatling/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
ENV GATLING_HOME /opt/gatling

CMD ["gatling.sh", "-rd", "simple run"]