#!/bin/bash

echo ""

echo -e "\nbuild docker alluxio image\n"
docker build -f alluxio.dockerfile -t solomonfield/alluxio:1.7.0 .

echo ""