#/bin/bash
hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop jar ~/experiment/experiment-1.0.jar hbaseassignment output
rm /root/experiment/hbase-assignment.txt
hadoop fs -cat output/* >> /root/experiment/hbase-assignment.txt