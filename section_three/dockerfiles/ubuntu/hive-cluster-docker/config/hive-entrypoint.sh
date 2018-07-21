#!/bin/bash
startMaster(){
	$HADOOP_HOME/sbin/start-dfs.sh
    $HADOOP_HOME/sbin/start-yarn.sh
	sleep 10
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
