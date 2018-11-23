package com.whg.triangle;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class DegreeCount {

    public static class DegreeCountMapper extends Mapper<LongWritable, Text, Text, Text> {

        private Text newKey = new Text();
        private Text newValue = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            if (itr.countTokens()!=2) {
                return;
            }
            String a = itr.nextToken();
            String b = itr.nextToken();
            if (a.equals(b)) {
                return;
            }
            newKey.set(a);
            newValue.set(b);
            // 统计一个点所有的邻接点
            context.write(newKey, newValue);
            context.write(newValue, newKey);
        }

    }

    public static class DegreeCountReducer extends Reducer<Text, Text, Text, IntWritable> {

        private IntWritable newValue = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            // 去重后得到点度
            Set<String> set = new HashSet<>();
            for (Text val: values) {
                set.add(val.toString());
            }
            newValue.set(set.size());
            context.write(key, newValue);
        }
    }

    public static class ReverseMapper extends Mapper<Text, IntWritable, IntWritable, Text> {

        @Override
        public void map(Text key, IntWritable value, Context context)
                throws IOException, InterruptedException {
            // 仅仅将key和value交换，依赖mapreduce完成按照点度升序r排序
            context.write(value, key);
        }
    }
}
