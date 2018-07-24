FROM solomonfield/hadoop:2.7.6-centos
LABEL Solomonfield <whg19961229@gmail.com>

COPY config/* /tmp/

# Download first
RUN wget https://downloads.lightbend.com/scala/2.12.6/scala-2.12.6.rpm && \
    wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.3.1/spark-2.3.1-bin-hadoop2.7.tgz

RUN rpm -i scala-2.12.6.rpm && \
    rm scala-2.12.6.rpm && \
    tar xvf spark-2.3.1-bin-hadoop2.7.tgz && \
    mv spark-2.3.1-bin-hadoop2.7 /usr/local/spark && \
    rm -rf spark-2.3.1-bin-hadoop2.7.tgz && \
	echo "spark-master" >> /usr/local/spark/conf/slaves && \
	echo "spark-slave1" >> /usr/local/spark/conf/slaves && \
	echo "spark-slave2" >> /usr/local/spark/conf/slaves && \
    echo "spark-slave3" >> /usr/local/spark/conf/slaves && \
    echo "spark-slave4" >> /usr/local/spark/conf/slaves && \
	rm $HADOOP_HOME/etc/hadoop/slaves && \
	cp /usr/local/spark/conf/slaves $HADOOP_HOME/etc/hadoop/slaves && \
	echo "export SCALA_HOME=/usr/share/scala" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export JAVA_HOME=$JAVA_HOME" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export HADOOP_HOME=$HADOOP_HOME" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_MASTER_IP=spark-master" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_LOCAL_DIRS=/usr/local/spark" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_DRIVER_MEMORY=1G" >> /usr/local/spark/conf/spark-env.sh && \
    mv /tmp/core-site.xml /usr/local/hadoop/etc/hadoop/core-site.xml && \
	mv /tmp/hdfs-site.xml /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
	mv /tmp/mapred-site.xml /usr/local/hadoop/etc/hadoop/mapred-site.xml && \
    mv /tmp/yarn-site.xml /usr/local/hadoop/etc/hadoop/yarn-site.xml && \
    mv /tmp/slaves /usr/local/hadoop/etc/hadoop/slaves && \
    mv /tmp/spark-entrypoint.sh /root/spark-entrypoint.sh

ENV SPARK_HOME /usr/local/spark
ENV PATH $PATH:$SPARK_HOME/bin
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "/root/spark-entrypoint.sh; bash"]
