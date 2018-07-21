#!/bin/bash

echo ""

echo -e "\nbuild docker hive image\n"
docker build -f hive.dockerfile -t solomonfield/hive:2.3.3-centos .

echo ""