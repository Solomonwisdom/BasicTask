FROM ubuntu:16.04

LABEL Solomonfield <whg19961229@gmail.com>

WORKDIR /root

# install openssh-server, oracle-jdk and wget
RUN apt-get update && \
    apt-get install -y openssh-server wget vim language-pack-zh-hans software-properties-common && \
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update && \
    apt-get install -y oracle-java8-installer && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/oracle-jdk8-installer

# install hadoop 2.7.2
RUN wget https://archive.apache.org/dist/hadoop/common/hadoop-2.7.2/hadoop-2.7.2.tar.gz && \
    tar -xzvf hadoop-2.7.2.tar.gz && \
    mv hadoop-2.7.2 /usr/local/hadoop && \
    rm hadoop-2.7.2.tar.gz

# set environment variable
ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-8-oracle
ENV HADOOP_HOME=/usr/local/hadoop 
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin 

# ssh without key
RUN ssh-keygen -q -t rsa -N '' -f /root/.ssh/id_rsa && \
	cat /root/.ssh/id_rsa.pub > /root/.ssh/authorized_keys && \
	echo "    IdentityFile ~/.ssh/id_rsa" >> /etc/ssh/ssh_config && \
	echo "    StrictHostKeyChecking=no" >> /etc/ssh/ssh_config && \
	chmod 600 /root/.ssh/authorized_keys 

RUN mkdir -p ~/hdfs/namenode && \ 
    mkdir -p ~/hdfs/datanode && \
    mkdir $HADOOP_HOME/logs

COPY config/* /tmp/

RUN mv /tmp/hadoop-env.sh /usr/local/hadoop/etc/hadoop/hadoop-env.sh && \
    mv /tmp/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml && \ 
    mv /tmp/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml && \
    mv /tmp/mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml && \
    mv /tmp/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
    mv /tmp/slaves $HADOOP_HOME/etc/hadoop/slaves && \
    mv /tmp/hadoop-entrypoint.sh /root/hadoop-entrypoint.sh && \
    chmod +x /root/hadoop-entrypoint.sh && \
    chmod +x $HADOOP_HOME/sbin/start-dfs.sh && \
    chmod +x $HADOOP_HOME/sbin/start-yarn.sh 

# Hdfs ports
EXPOSE 50010 50020 50070 50075 50090 8020 9000 \
# Mapred ports
    10020 19888 \
#Yarn ports
    8030 8031 8032 8033 8040 8042 8088 \
#Other ports 
    49707 2122

ENTRYPOINT [ "sh", "-c", "/root/hadoop-entrypoint.sh; bash"]

