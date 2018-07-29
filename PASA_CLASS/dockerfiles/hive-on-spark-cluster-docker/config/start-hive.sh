hdfs dfs -mkdir /tmp
hdfs dfs -mkdir -p /user/hive/warehouse
hdfs dfs -chmod g+w /tmp
hdfs dfs -chmod g+w /user/hive/warehouse
hdfs dfs -put /usr/local/spark/jars /spark-jars
sleep 6
schematool --dbType mysql --initSchema
# hiveserver2 --hiveconf hive.server2.enable.doAs=false
sleep 10
hive --service metastore
	# hive --service hiveserver2