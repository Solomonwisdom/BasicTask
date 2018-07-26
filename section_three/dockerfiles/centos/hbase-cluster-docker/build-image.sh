#!/bin/bash

echo ""

echo -e "\nbuild docker hbase image\n"
docker build -f hbase.dockerfile -t solomonfield/hbase:2.1.0-centos .

echo ""