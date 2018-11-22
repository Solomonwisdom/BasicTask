#/bin/bash
# hadoop fs -rm -r -f -skipTrash /user/root/*
# hadoop fs -mkdir -p input
# hadoop fs -put ~/experiment/exp2_sample_data/* input
# hadoop jar ~/experiment/experiment-1.0.jar invertedindex input output 
# rm /root/experiment/result1.txt
# hadoop fs -cat output/* >> /root/experiment/result1.txt
hadoop fs -mkdir -p /data/graphTriangleCount
hadoop fs -put ~/experiment/twitter_graph_v2.txt  ~/experiment/gplus_combined.unique.txt /data/graphTriangleCount
hadoop jar ~/experiment/experiment-2.0.jar Triangle_Count /data/graphTriangleCount/twitter_graph_v2.txt output/twitter_ud 18
hadoop jar ~/experiment/experiment-2.0.jar Directed_Triangle_Count /data/graphTriangleCount/twitter_graph_v2.txt output/twitter_d 18
hadoop jar ~/experiment/experiment-2.0.jar Triangle_Count /data/graphTriangleCount/gplus_combined.unique.txt output/gplus_ud 25
hadoop jar ~/experiment/experiment-2.0.jar Directed_Triangle_Count /data/graphTriangleCount/gplus_combined.unique.txt output/gplus_d 30

# 13082506 1073677742
# 1818304  27018510

# 103-105 twitter_ud 30 85s
# 116-118 gplus_ud 30 379s
# 151-153 gplus_ud 20 218s
# 127-129 gplus_ud 25 369s 
# 132-135 twitter_ud 30 141s
# 137-139 gplus_d  25 311s