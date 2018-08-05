#!/bin/bash

echo ""

echo -e "\nbuild spark image\n"
docker build -f spark.dockerfile -t solomonfield/spark-on-hive:2.1.2 .

echo ""