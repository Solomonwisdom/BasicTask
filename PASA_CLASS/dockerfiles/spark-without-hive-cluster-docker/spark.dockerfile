FROM solomonfield/hbase:2.1.0
LABEL Solomonfield <whg19961229@gmail.com>

# Download scala and maven
RUN wget https://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.rpm && \
    wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz && \
	tar zxvf apache-maven-3.5.4-bin.tar.gz && \
	rm apache-maven-3.5.4-bin.tar.gz && \
	mv apache-maven-3.5.4 /usr/local/maven && \
	rpm -i scala-2.11.8.rpm && \
    rm scala-2.11.8.rpm && \
    wget https://archive.apache.org/dist/spark/spark-2.1.2/spark-2.1.2.tgz && \
	tar xvf spark-2.1.2.tgz &&  rm spark-2.1.2.tgz

ENV OLDPATH $PATH
ENV PATH /usr/local/maven/bin/:$PATH

WORKDIR /root/spark-2.1.2/
# compile spark from source code
RUN	./dev/make-distribution.sh --name "hadoop2.7-without-hive" --tgz "-Pyarn,hadoop-provided,hadoop-2.7,parquet-provided" && \
	tar xvf spark-2.1.2-bin-hadoop2.7-without-hive.tgz -C /usr/local/ && \
	mv /usr/local/spark-2.1.2-bin-hadoop2.7-without-hive /usr/local/spark && \
	rm -rf /root/spark-2.1.2 /usr/local/maven /root/.mvn

WORKDIR /root
COPY config/* /tmp/

RUN	echo "spark-master" >> /usr/local/spark/conf/slaves && \
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
	echo "export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)" >> /usr/local/spark/conf/spark-env.sh && \
    mv /tmp/core-site.xml /usr/local/hadoop/etc/hadoop/core-site.xml && \
	mv /tmp/hdfs-site.xml /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
	mv /tmp/mapred-site.xml /usr/local/hadoop/etc/hadoop/mapred-site.xml && \
    mv /tmp/yarn-site.xml /usr/local/hadoop/etc/hadoop/yarn-site.xml && \
    cp /usr/local/spark/conf/slaves /usr/local/hadoop/etc/hadoop/slaves && \
	rm /usr/local/zookeeper/conf/zoo.cfg /usr/local/hbase/conf/hbase-site.xml && \
	mv /tmp/zoo.cfg /usr/local/zookeeper/conf/zoo.cfg && \
	mv /tmp/hbase-site.xml /usr/local/hbase/conf/hbase-site.xml && \
	rm /usr/local/hbase/conf/regionservers && \
	cp /usr/local/spark/conf/slaves /usr/local/hbase/conf/regionservers && \
    mv /tmp/spark-entrypoint.sh /root/spark-entrypoint.sh

ENV SPARK_HOME /usr/local/spark
ENV PATH $OLDPATH:$SPARK_HOME/bin
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "/root/spark-entrypoint.sh; bash"]
