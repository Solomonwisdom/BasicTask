#!/bin/bash
startMaster(){
	$HADOOP_HOME/sbin/start-dfs.sh
    $HADOOP_HOME/sbin/start-yarn.sh
	sleep 10
	/usr/local/spark/sbin/start-all.sh
}

main(){
	service sshd restart
	# format namenode
	/usr/local/hadoop/bin/hdfs namenode -format
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		startMaster
	fi
}

main
