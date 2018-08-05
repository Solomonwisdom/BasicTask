#!/bin/bash

echo ""

echo -e "\nbuild spark image\n"
docker build -f py3spark.dockerfile -t solomonfield/py3spark:2.3.1 .

echo ""