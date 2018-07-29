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
	# hive --service hiveserver2
}
startSpark(){
	/usr/local/spark/sbin/start-all.sh
	sleep 10
	hive --service metastore
}

main(){
	service sshd restart
	echo "${ZK_ID}" >${ZOOKEEPER_HOME}/data/myid
	/usr/local/hadoop/bin/hdfs namenode -format
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
		sleep 7
		startSpark
	fi
}

main
