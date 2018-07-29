FROM solomonfield/hadoop:2.7.4-centos
LABEL Solomonfield <whg19961229@gmail.com>

ENV ALLUXIO_VERSION=1.7.0

# DOWNLOAD
RUN wget http://downloads.alluxio.org/downloads/files/${ALLUXIO_VERSION}/alluxio-${ALLUXIO_VERSION}-hadoop-2.7-bin.tar.gz && \
    tar zxvf alluxio-${ALLUXIO_VERSION}-hadoop-2.7-bin.tar.gz && \
    mv alluxio-${ALLUXIO_VERSION}-hadoop-2.7 /usr/local/alluxio && \
    rm alluxio-${ALLUXIO_VERSION}-hadoop-2.7-bin.tar.gz

# CONFIG
RUN echo "hadoop-master" >> /usr/local/alluxio/conf/masters && \
    echo "hadoop-master" >> /usr/local/alluxio/conf/workers && \
	echo "hadoop-slave1" >> /usr/local/alluxio/conf/workers && \
	echo "hadoop-slave2" >> /usr/local/alluxio/conf/workers && \
	echo "hadoop-slave3" >> /usr/local/alluxio/conf/workers && \
	echo "hadoop-slave4" >> /usr/local/alluxio/conf/workers && \
    cp /usr/local/alluxio/conf/alluxio-site.properties.template /usr/local/alluxio/conf/alluxio-site.properties && \
    echo "alluxio.master.hostname=hadoop-master" >> /usr/local/alluxio/conf/alluxio-site.properties && \
    echo "alluxio.underfs.address=hdfs://hadoop-master:9000/alluxio/data" >> /usr/local/alluxio/conf/alluxio-site.properties && \
    echo "alluxio.master.hostname=hadoop-master" >> /usr/local/alluxio/conf/alluxio-site.properties && \
    echo "alluxio.underfs.hdfs.configuration=/usr/local/hadoop/etc/hadoop/core-site.xml:/usr/local/hadoop/etc/hadoop/hdfs-site.xml" >> /usr/local/alluxio/conf/alluxio-site.properties && \
    cp /usr/local/alluxio/client/alluxio-1.7.0-client.jar /usr/local/hadoop/lib/alluxio-1.7.0-client.jar && \
    rm /usr/local/hadoop/etc/hadoop/core-site.xml

COPY alluxio-entrypoint.sh /root/alluxio-entrypoint.sh
COPY core-site.xml /usr/local/hadoop/etc/hadoop/core-site.xml
ENV ALLUXIO_HOME=/usr/local/alluxio
ENV PATH=${ALLUXIO_HOME}/bin:$PATH
ENV ENABLE_FUSE true
EXPOSE 19999

ENTRYPOINT ["sh", "-c", "/root/alluxio-entrypoint.sh; bash"]
