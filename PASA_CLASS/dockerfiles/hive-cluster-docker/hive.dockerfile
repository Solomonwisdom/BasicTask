FROM solomonfield/spark-without-hive:2.1.2
LABEL Solomonfield <whg19961229@gmail.com>

ENV HIVE_VERSION=2.0.0

# ADD HIVE AND MYSQL DRIVER
RUN wget http://archive.apache.org/dist/hive/hive-${HIVE_VERSION}/apache-hive-${HIVE_VERSION}-bin.tar.gz && \
    tar -xzvf apache-hive-${HIVE_VERSION}-bin.tar.gz && \
    mv apache-hive-${HIVE_VERSION}-bin /usr/local/hive && \
    rm apache-hive-${HIVE_VERSION}-bin.tar.gz && \
	wget http://ftp.ntu.edu.tw/MySQL/Downloads/Connector-J/mysql-connector-java-5.1.46.tar.gz && \
	tar zxvf mysql-connector-java-5.1.46.tar.gz && \
	# ADD NECESSARY JARS
	mv mysql-connector-java-5.1.46/mysql-connector-java-5.1.46-bin.jar /usr/local/hive/lib/ && \
	rm mysql-connector-java-5.1.46.tar.gz && \
	rm -rf mysql-connector-java-5.1.46

COPY config/* /tmp/


RUN	rm ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh && \
	mv /tmp/hadoop-env.sh ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh && \
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
	rm /usr/local/spark/conf/slaves && \
	cp /usr/local/hbase/conf/regionservers /usr/local/spark/conf/slaves && \
	rm $HADOOP_HOME/etc/hadoop/slaves && \
	cp /usr/local/spark/conf/slaves $HADOOP_HOME/etc/hadoop/slaves && \
	mv /tmp/hive-env.sh /usr/local/hive/conf/hive-env.sh && \
	rm $HADOOP_HOME/etc/hadoop/core-site.xml && \
    mv /tmp/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml && \
	rm $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
	rm /root/*.sh && \
    mv /tmp/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
	mv /tmp/hive-entrypoint.sh /root/hive-entrypoint.sh && \
	chmod a+x /root/hive-entrypoint.sh

ENV HIVE_HOME /usr/local/hive
ENV HIVE_SKIP_SPARK_ASSEMBLY true
ENV PATH $PATH:$HIVE_HOME/bin
EXPOSE 10000 10002

ENTRYPOINT [ "sh", "-c", "/root/hive-entrypoint.sh; bash"]
