package com.whg.triangle;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class GraphBuilder {

    public static class GraphMapper extends Mapper<Text, Text, Text, Text> {

        @Override
        public void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {
            context.write(key, value);
        }
    }


    public static class GraphReducer extends Reducer<Text, Text, Text, Text> {

        private Text newValue = new Text();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            boolean first = true;
            StringBuilder output = new StringBuilder();
            for (Text val: values) {
                if (!first) {
                    output.append(";");
                } else {
                    first = false;
                }
                output.append(val.toString());
            }
            newValue.set(output.toString());
            context.write(key, newValue);
        }
    }

}
