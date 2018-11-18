#/bin/bash
hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p input
hadoop fs -put ~/experiment/exp2_sample_data/* input
hadoop jar ~/experiment/experiment-1.0.jar invertedindex input output 
rm /root/experiment/result1.txt
# hadoop fs -cat output/* >> /root/experiment/result1.txt