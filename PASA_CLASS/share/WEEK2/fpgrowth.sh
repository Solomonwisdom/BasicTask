#/bin/bash

hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p /user/root/input
hadoop fs -put /root/experiment/WEEK2/T10I4D100K.txt input/
spark-submit --class com.whg.fpgrowth \
--master yarn --deploy-mode client --executor-memory 1g \
--name fpgrowth --conf "spark.app.id=fpgrowth" \
fpgrowth_2.11-0.1.jar

if [ -f "/root/experiment/WEEK2/fpgrowth.txt" ]; then
    rm /root/experiment/WEEK2/fpgrowth.txt
fi
echo "频繁集" >> /root/experiment/WEEK2/fpgrowth.txt
hadoop fs -cat output/fpgrowth/* >> /root/experiment/WEEK2/fpgrowth.txt
echo "关联规则" >> /root/experiment/WEEK2/fpgrowth.txt
hadoop fs -cat output/AssociationRules/* >> /root/experiment/WEEK2/fpgrowth.txt
