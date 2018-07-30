#/bin/bash
# echo 'export HADOOP_CLASSPATH=/usr/local/hadoop/lib/:/usr/local/hbase/lib/*' >> /usr/local/hadoop/etc/hadoop/hadoop-env.sh
hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p input
hadoop fs -mkdir -p StopWords
hadoop fs -put ~/experiment/exp2_sample_data/* input
hadoop fs -put ~/experiment/Stop_words.txt StopWords
hadoop jar ~/experiment/experiment-1.0.jar invertedindexwithhbase input output 
rm /root/experiment/result2.txt
hadoop fs -cat output/* >> /root/experiment/result2.txt