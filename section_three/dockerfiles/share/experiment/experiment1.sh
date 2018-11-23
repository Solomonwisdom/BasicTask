#/bin/bash
# hadoop fs -rm -r -f -skipTrash /user/root/*
# hadoop fs -mkdir -p input
# hadoop fs -put ~/experiment/exp2_sample_data/* input
# hadoop jar ~/experiment/experiment-1.0.jar invertedindex input output 
# rm /root/experiment/result1.txt
# hadoop fs -cat output/* >> /root/experiment/result1.txt
hadoop fs -mkdir -p /data/graphTriangleCount
hadoop fs -put ~/experiment/twitter_graph_v2.txt  ~/experiment/gplus_combined.unique.txt /data/graphTriangleCount
hadoop jar ~/experiment/experiment-2.0.jar Triangle_Count /data/graphTriangleCount/twitter_graph_v2.txt output/twitter_ud 20
hadoop jar ~/experiment/experiment-2.0.jar Directed_Triangle_Count_old /data/graphTriangleCount/twitter_graph_v2.txt output/twitter_d 40
hadoop jar ~/experiment/experiment-2.0.jar Triangle_Count /data/graphTriangleCount/gplus_combined.unique.txt output/gplus_ud 40
hadoop jar ~/experiment/experiment-2.0.jar Directed_Triangle_Count /data/graphTriangleCount/gplus_combined.unique.txt output/gplus_d 40

# 13082506 1073677742
# 1818304  27018510

# 185-187 twitter_ud 20 77s
# 246-248 gplus_ud 40 191s
# 257-58   twitter_d_old 40 46s
# 236-240 gplus_d 40 104s