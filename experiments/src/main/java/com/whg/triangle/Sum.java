package com.whg.triangle;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Sum {

    public static class SumMapper extends Mapper<Text, Text, Text, IntWritable> {

        private IntWritable newValue = new IntWritable();
        @Override
        public void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {
            newValue.set(Integer.parseInt(value.toString()));
            context.write(key, newValue);
        }
    }

    public static class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            // 将所有的值相加
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(new Text("The number of Triangle:"), new IntWritable(sum));
        }
    }
}
