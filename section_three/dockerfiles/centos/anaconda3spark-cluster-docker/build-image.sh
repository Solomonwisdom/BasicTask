#!/bin/bash

echo ""

echo -e "\nbuild spark image\n"
docker build -f anaconda3spark.dockerfile -t solomonfield/anaconda3spark:2.3.1 .

echo ""