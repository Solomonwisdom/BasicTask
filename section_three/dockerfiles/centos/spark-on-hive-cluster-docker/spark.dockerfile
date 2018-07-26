FROM solomonfield/hive:2.3.3hb2-centos
LABEL Solomonfield <whg19961229@gmail.com>

COPY config/* /tmp/
ENV SCALA_VERSION=2.11.8

# Download first
RUN wget https://downloads.lightbend.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.rpm && \
    wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.3.1/spark-2.3.1-bin-hadoop2.7.tgz && \
	rpm -i scala-${SCALA_VERSION}.rpm && \
    rm scala-${SCALA_VERSION}.rpm && \
	tar xvf spark-2.3.1-bin-hadoop2.7.tgz && \
	mv spark-2.3.1-bin-hadoop2.7 /usr/local/spark && \
	rm -rf spark-2.3.1-bin-hadoop2.7.tgz
	
RUN echo "spark-master" >> /usr/local/spark/conf/slaves && \
	echo "spark-slave1" >> /usr/local/spark/conf/slaves && \
	echo "spark-slave2" >> /usr/local/spark/conf/slaves && \
    echo "spark-slave3" >> /usr/local/spark/conf/slaves && \
    echo "spark-slave4" >> /usr/local/spark/conf/slaves && \
	rm $HADOOP_HOME/etc/hadoop/slaves && \
	cp /usr/local/spark/conf/slaves $HADOOP_HOME/etc/hadoop/slaves && \
	mv /tmp/hive-site.xml /usr/local/hive/conf/hive-site.xml && \
	rm /usr/local/zookeeper/conf/zoo.cfg /usr/local/hbase/conf/hbase-site.xml && \
	mv /tmp/zoo.cfg /usr/local/zookeeper/conf/zoo.cfg && \
	mv /tmp/hbase-site.xml /usr/local/hbase/conf/hbase-site.xml && \
	rm /usr/local/hbase/conf/regionservers && \
	echo "spark-master" >> /usr/local/hbase/conf/regionservers && \
	echo "spark-slave1" >> /usr/local/hbase/conf/regionservers && \
	echo "spark-slave2" >> /usr/local/hbase/conf/regionservers && \
	echo "spark-slave3" >> /usr/local/hbase/conf/regionservers && \
	echo "spark-slave4" >> /usr/local/hbase/conf/regionservers && \
	echo "export SCALA_HOME=/usr/share/scala" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export JAVA_HOME=$JAVA_HOME" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export HADOOP_HOME=/usr/local/hadoop" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_MASTER_IP=spark-master" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_LOCAL_DIRS=/usr/local/spark" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_DRIVER_MEMORY=1G" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_HOME=/usr/local/spark" >> /usr/local/spark/conf/spark-env.sh && \
    mv /tmp/core-site.xml /usr/local/hadoop/etc/hadoop/core-site.xml && \
	mv /tmp/hdfs-site.xml /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
	mv /tmp/mapred-site.xml /usr/local/hadoop/etc/hadoop/mapred-site.xml && \
    mv /tmp/yarn-site.xml /usr/local/hadoop/etc/hadoop/yarn-site.xml && \
    mv /tmp/spark-entrypoint.sh /root/spark-entrypoint.sh && \
	# hive config for spark
	cp /usr/local/hive/conf/hive-site.xml /usr/local/spark/conf/hive-site.xml && \
	echo "export HIVE_HOME=/usr/local/hive" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export SPARK_CLASSPATH=$SPARK_CLASSPATH:$HIVE_HOME/lib/mysql-connector-java-5.1.46-bin.jar" >> /usr/local/spark/conf/spark-env.sh

ENV SPARK_HOME /usr/local/spark
ENV PATH $PATH:$SPARK_HOME/bin
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "/root/spark-entrypoint.sh; bash"]
