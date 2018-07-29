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
    wget https://archive.apache.org/dist/spark/spark-2.0.0/spark-2.0.0.tgz && \
	tar xvf spark-2.0.0.tgz &&  rm spark-2.0.0.tgz

ENV OLDPATH $PATH
ENV PATH /usr/local/maven/bin/:$PATH

WORKDIR /root/spark-2.0.0/
# compile spark from source code
RUN	./dev/make-distribution.sh --name "hadoop2.7-without-hive" --tgz "-Pyarn,hadoop-provided,hadoop-2.7,parquet-provided" && \
	tar xvf spark-2.0.0-bin-hadoop2.7-without-hive.tgz -C /usr/local/ && \
	mv /usr/local/spark-2.0.0-bin-hadoop2.7-without-hive /usr/local/spark && \
	rm -rf /root/spark-2.0.0 /usr/local/maven /root/.mvn

WORKDIR /root

ENV SPARK_HOME /usr/local/spark
ENV PATH $OLDPATH:$SPARK_HOME/bin

ENV HIVE_VERSION=2.3.3

# ADD HIVE AND MYSQL DRIVER
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-${HIVE_VERSION}/apache-hive-${HIVE_VERSION}-bin.tar.gz && \
    tar -xzvf apache-hive-${HIVE_VERSION}-bin.tar.gz && \
    mv apache-hive-${HIVE_VERSION}-bin /usr/local/hive && \
    rm apache-hive-${HIVE_VERSION}-bin.tar.gz && \
	wget http://ftp.ntu.edu.tw/MySQL/Downloads/Connector-J/mysql-connector-java-5.1.46.tar.gz && \
	tar zxvf mysql-connector-java-5.1.46.tar.gz && \
	# ADD NECESSARY JARS
	mv mysql-connector-java-5.1.46/mysql-connector-java-5.1.46-bin.jar /usr/local/hive/lib/ && \
	cp /usr/local/spark/jars/scala-library-2.11.8.jar /usr/local/hive/lib && \
	cp /usr/local/spark/jars/spark-network-common_2.11-2.0.0.jar /usr/local/hive/lib && \
	cp /usr/local/spark/jars/spark-core_2.11-2.0.0.jar /usr/local/hive/lib && \
	rm mysql-connector-java-5.1.46.tar.gz && \
	rm -rf mysql-connector-java-5.1.46

COPY config/* /tmp/

RUN	echo "export SCALA_HOME=/usr/share/scala" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export JAVA_HOME=$JAVA_HOME" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export HADOOP_HOME=$HADOOP_HOME" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_MASTER_IP=spark-master" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_LOCAL_DIRS=/usr/local/spark" >> /usr/local/spark/conf/spark-env.sh && \
	echo "SPARK_DRIVER_MEMORY=1G" >> /usr/local/spark/conf/spark-env.sh && \
	echo "export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)" >> /usr/local/spark/conf/spark-env.sh && \
	rm ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh && \
	mv /tmp/hadoop-env.sh ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh && \
	mv /tmp/mapred-site.xml ${HADOOP_HOME}/etc/hadoop/mapred-site.xml && \
	mv /tmp/hive-site.xml /usr/local/hive/conf/hive-site.xml && \
	rm /usr/local/zookeeper/conf/zoo.cfg /usr/local/hbase/conf/hbase-site.xml && \
	mv /tmp/zoo.cfg /usr/local/zookeeper/conf/zoo.cfg && \
	mv /tmp/hbase-site.xml /usr/local/hbase/conf/hbase-site.xml && \
	rm /usr/local/hbase/conf/regionservers && \
	echo "hive-master" >> /usr/local/hbase/conf/regionservers && \
	echo "hive-slave1" >> /usr/local/hbase/conf/regionservers && \
	echo "hive-slave2" >> /usr/local/hbase/conf/regionservers && \
	echo "hive-slave3" >> /usr/local/hbase/conf/regionservers && \
	echo "hive-slave4" >> /usr/local/hbase/conf/regionservers && \
	rm /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
	mv /tmp/hdfs-site.xml /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
	cp /usr/local/hbase/conf/regionservers /usr/local/spark/conf/slaves && \
	rm $HADOOP_HOME/etc/hadoop/slaves && \
	cp /usr/local/spark/conf/slaves $HADOOP_HOME/etc/hadoop/slaves && \
	mv /tmp/hive-env.sh /usr/local/hive/conf/hive-env.sh && \
	rm $HADOOP_HOME/etc/hadoop/core-site.xml && \
    mv /tmp/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml && \
	rm $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
    mv /tmp/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
	mv /tmp/spark-entrypoint.sh /root/spark-entrypoint.sh && \
	chmod a+x /root/spark-entrypoint.sh && \
	mv /tmp/start-hive.sh /root/start-hive.sh && \
	chmod a+x /root/start-hive.sh

ENV HIVE_HOME /usr/local/hive
ENV PATH $PATH:$HIVE_HOME/bin
EXPOSE 10000 10002 8080 4040

ENTRYPOINT [ "sh", "-c", "/root/spark-entrypoint.sh; bash"]
