#/bin/bash
hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p hdfs://hbase-master:9000/user/root/lib/
hadoop fs -put lib/* hdfs://hbase-master:9000/user/root/lib/
hadoop jar ~/experiment/hbaseassignment-1.0-SNAPSHOT.jar hbaseassignment output -libjars hdfs://hbase-master:9000/user/root/lib/*.jar
rm /root/experiment/hbase-assignment.txt
hadoop fs -cat output/* >> /root/experiment/hbase-assignment.txt