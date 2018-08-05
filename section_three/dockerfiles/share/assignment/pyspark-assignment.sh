hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p /user/root/input
hadoop fs -put /root/assignment/bank-data.csv input/
spark-submit --master yarn --deploy-mode client --executor-memory 1g \
--name GraphXAssignment --conf "spark.app.id=pysparkAssignment" \
pyspark_assignment.py