#!/bin/bash

echo ""

echo -e "\nbuild docker hbase image\n"
docker build -f hbase.dockerfile -t solomonfield/hbase:1.2.8-centos .

echo ""