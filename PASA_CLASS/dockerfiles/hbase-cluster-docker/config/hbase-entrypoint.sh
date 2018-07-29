#!/bin/bash

function main(){
	service sshd restart
	echo "${ZK_ID}" >${ZOOKEEPER_HOME}/data/myid
	hdfs namenode -format
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		$HADOOP_HOME/sbin/start-dfs.sh
    	$HADOOP_HOME/sbin/start-yarn.sh
	fi
	sleep 10
	${ZOOKEEPER_HOME}/bin/zkServer.sh start
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		${HBASE_HOME}/bin/start-hbase.sh
	fi
}

main

