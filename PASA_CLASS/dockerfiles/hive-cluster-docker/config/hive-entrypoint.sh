#!/bin/bash
startHadoop(){
	$HADOOP_HOME/sbin/start-dfs.sh
    $HADOOP_HOME/sbin/start-yarn.sh
}
startHbaseAndHive(){
	${HBASE_HOME}/bin/start-hbase.sh
	sleep 5
	hdfs dfs -mkdir /tmp
	hdfs dfs -mkdir -p /user/hive/warehouse
	hdfs dfs -chmod g+w /tmp
	hdfs dfs -chmod g+w /user/hive/warehouse
	sleep 6
	schematool --dbType mysql --initSchema
	# hiveserver2 --hiveconf hive.server2.enable.doAs=false
	sleep 10
	hive --service metastore
	# hive --service hiveserver2
}

function main(){
	service sshd restart
	echo "${ZK_ID}" >${ZOOKEEPER_HOME}/data/myid
	hdfs namenode -format
	sleep 5
	if [ ${ROLE} == "master" ]
	then
		startHadoop
		sleep 8
	fi
	sleep 5
	${ZOOKEEPER_HOME}/bin/zkServer.sh start
	if [ ${ROLE} == "master" ]
	then
		startHbaseAndHive
	fi
}

main
