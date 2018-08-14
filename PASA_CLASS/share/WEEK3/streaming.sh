spark-submit --class com.whg.NetworkWordCount \
--master yarn --deploy-mode client --executor-memory 1g \
--name StreamingAssignment --conf "spark.app.id=StreamingAssignment" \
streamingassignment_2.11-0.1.jar spark-master 9999