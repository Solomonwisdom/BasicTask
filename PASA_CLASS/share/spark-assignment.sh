#/bin/bash
hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p /user/root/input/
hadoop fs -put /root/experiment/letter /user/root/input/
spark-submit --class com.whg.SparkWordCount \
--master yarn --deploy-mode client --executor-memory 1g \
--name wordcount --conf "spark.app.id=wordcount" \
sparkassignment_2.11-0.1.jar