#!/bin/bash
startMaster(){
	$HADOOP_HOME/sbin/start-dfs.sh
    $HADOOP_HOME/sbin/start-yarn.sh
	sleep 10
	${HBASE_HOME}/bin/start-hbase.sh
}

function main(){
	service ssh restart
	echo "${ZK_ID}" >${ZOOKEEPER_HOME}/data/myid
	hdfs namenode -format
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		startMaster
	fi
}

main

