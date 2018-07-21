#!/bin/bash

docker network create --driver=bridge hadoop
# the default node number is 5
N=${1:-5}

# start hadoop master container
docker rm -f hadoop-master &> /dev/null
echo "start hadoop-master container..."
docker run -itd \
                --net=hadoop \
                -p 50070:50070 \
                -p 8088:8088 \
				-p 9000:9000 \
				-v ~/Downloads/Resources/experiment/:/root/experiment/ \
                --name hadoop-master \
                --hostname hadoop-master \
                solomonfield/hadoop:2.7.2 &> /dev/null


# start hadoop slave container
i=1
while [ $i -lt $N ]
do
	docker rm -f hadoop-slave$i &> /dev/null
	echo "start hadoop-slave$i container..."
	docker run -itd \
	            --net=hadoop \
	            --name hadoop-slave$i \
	            --hostname hadoop-slave$i \
	            solomonfield/hadoop:2.7.2 &> /dev/null
	i=$(( $i + 1 ))
done 

# get into hadoop master container
docker exec -it hadoop-master bash
