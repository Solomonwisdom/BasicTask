#!/bin/bash

echo ""

echo -e "\nbuild spark image\n"
docker build -f spark.dockerfile -t solomonfield/spark-without-hive:2.1.2 .

echo ""