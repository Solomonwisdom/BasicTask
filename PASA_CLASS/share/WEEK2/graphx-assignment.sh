#/bin/bash
orderId=$1
if [ $# = 0 ]
then
	orderId=1
fi
hadoop fs -rm -r -f -skipTrash /user/root/*
hadoop fs -mkdir -p /user/root/input
hadoop fs -put /root/experiment/WEEK2/*.dat input/
spark-submit --class com.whg.GraphXAssignment \
--master yarn --deploy-mode client --executor-memory 1g \
--name GraphXAssignment --conf "spark.app.id=GraphXAssignment" \
graphxassignment_2.11-0.1.jar

if [ $orderId = 1 ]
then
  if [ -f "/root/experiment/WEEK2/GraphXAssignment1.txt" ]; then
    rm /root/experiment/WEEK2/GraphXAssignment1.txt
  fi
  echo "中国境内的PageRank值最高的10个机场" >> /root/experiment/WEEK2/GraphXAssignment1.txt
  hadoop fs -cat output/top10InChina/* >> /root/experiment/WEEK2/GraphXAssignment1.txt
  echo "美国境内的PageRank值最高的10个机场" >> /root/experiment/WEEK2/GraphXAssignment1.txt
  hadoop fs -cat output/top10InUS/* >> /root/experiment/WEEK2/GraphXAssignment1.txt
  echo "南京禄口机场的排名：" >> /root/experiment/WEEK2/GraphXAssignment1.txt
  hadoop fs -cat output/NKGRank/* >> /root/experiment/WEEK2/GraphXAssignment1.txt
else
  if [ -f "/root/experiment/WEEK2/GraphXAssignment2.txt" ]; then
    rm /root/experiment/WEEK2/GraphXAssignment2.txt
  fi
  hadoop fs -cat output/commonFriendsNum/* >> /root/experiment/WEEK2/GraphXAssignment2.txt
fi

