#!/bin/bash

startHadoop(){
	$HADOOP_HOME/sbin/start-dfs.sh
    $HADOOP_HOME/sbin/start-yarn.sh
}

main(){
	service sshd restart
	# format namenode
	/usr/local/hadoop/bin/hdfs namenode -format
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		startHadoop
	fi
	sleep 6
	$ALLUXIO_HOME/bin/alluxio format
	if [ ${ROLE} == "master" ]
	then
		$ALLUXIO_HOME/bin/alluxio-start.sh all NoMount
	else
		$ALLUXIO_HOME/bin/alluxio-start.sh worker NoMount
	fi
}

main