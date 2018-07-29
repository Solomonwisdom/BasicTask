#!/bin/bash

echo ""

echo -e "\nbuild docker hive image\n"
docker build -f hive.dockerfile -t solomonfield/hive:2.0.0 .

echo ""