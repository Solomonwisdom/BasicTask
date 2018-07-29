FROM solomonfield/hadoop:2.7.4-centos
LABEL Solomonfield <whg19961229@gmail.com>

ENV HBASE_VERSION=2.1.0
COPY config/* /tmp/
# ADD ZOOKEEPER AND HBASE
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz && \
	tar -xzvf zookeeper-3.4.10.tar.gz && \
    mv zookeeper-3.4.10 /usr/local/zookeeper && \
    rm zookeeper-3.4.10.tar.gz && \
	mkdir /usr/local/zookeeper/data	&& \
	mkdir /usr/local/zookeeper/log && \
	wget https://mirrors.tuna.tsinghua.edu.cn/apache/hbase/${HBASE_VERSION}/hbase-${HBASE_VERSION}-bin.tar.gz && \
	tar -xzvf hbase-${HBASE_VERSION}-bin.tar.gz && \
    mv hbase-${HBASE_VERSION} /usr/local/hbase && \
    rm hbase-${HBASE_VERSION}-bin.tar.gz && \
	mkdir /usr/local/hbase/zookeeper && \
	mkdir /var/hbase && \
	echo "hbase-master" >> /usr/local/hbase/conf/regionservers && \
	echo "hbase-slave1" >> /usr/local/hbase/conf/regionservers && \
	echo "hbase-slave2" >> /usr/local/hbase/conf/regionservers && \
	echo "hbase-slave3" >> /usr/local/hbase/conf/regionservers && \
	echo "hbase-slave4" >> /usr/local/hbase/conf/regionservers && \
	echo "export JAVA_HOME=$JAVA_HOME" >> /usr/local/hbase/conf/hbase-env.sh && \
	echo "export CLASSPATH=.:$CLASSPATH:$JAVA_HOME/lib" >> /usr/local/hbase/conf/hbase-env.sh && \
	echo "export HADOOP_HOME=/usr/local/hadoop" >> /usr/local/hbase/conf/hbase-env.sh && \
	echo "export HBASE_HOME=/usr/local/hbase" >> /usr/local/hbase/conf/hbase-env.sh && \
	echo "export HBASE_MANAGES_ZK=false" >> /usr/local/hbase/conf/hbase-env.sh && \
	# add hbase libs
	rm ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh && \
	mv /tmp/hadoop-env.sh ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh && \
	mv /tmp/zoo.cfg /usr/local/zookeeper/conf/zoo.cfg && \
	mv /tmp/hbase-site.xml /usr/local/hbase/conf/hbase-site.xml && \
	rm $HADOOP_HOME/etc/hadoop/core-site.xml && \
    mv /tmp/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml && \
	rm $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
    mv /tmp/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
	rm $HADOOP_HOME/etc/hadoop/slaves && \
	mv /tmp/slaves $HADOOP_HOME/etc/hadoop/slaves && \
	mv /tmp/hbase-entrypoint.sh /root/hbase-entrypoint.sh  && \
	chmod a+x /root/hbase-entrypoint.sh

ENV ZOOKEEPER_HOME=/usr/local/zookeeper
ENV PATH $ZOOKEEPER_HOME/bin:$PATH
EXPOSE 2181 2888 3888

ENV HBASE_HOME=/usr/local/hbase
ENV HBASE_CLASSPATH=$HBASE_HOME/conf
ENV HBASE_LOG_DIR=$HBASE_HOME/logs
ENV PATH $HBASE_HOME/bin:$PATH

ENTRYPOINT [ "sh", "-c", "/root/hbase-entrypoint.sh; bash"]
