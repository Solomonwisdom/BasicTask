#!/bin/bash
startHadoop() {
	$HADOOP_HOME/sbin/start-dfs.sh
    $HADOOP_HOME/sbin/start-yarn.sh
}

startMaster() {
	${HBASE_HOME}/bin/start-hbase.sh
	sleep 10
	/usr/local/spark/sbin/start-all.sh
}

main() {
	service sshd restart
	# format namenode
	/usr/local/hadoop/bin/hdfs namenode -format
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		startHadoop
	fi
	sleep 10
	echo "${ZK_ID}" >${ZOOKEEPER_HOME}/data/myid
	${ZOOKEEPER_HOME}/bin/zkServer.sh start
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		startMaster
	fi
}

main
