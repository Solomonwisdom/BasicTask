package com.whg.triangle_old;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class DirectedGraphBuilder {
    public static class DirectedGraphMapper extends Mapper<LongWritable, Text, Text, Text> {

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
            int cmp = a.compareTo(b);
            if (cmp == 0) {
                return;
            } else if(cmp > 0) {
                newKey.set(b);
                newValue.set("-"+a);
            } else {
                newKey.set(a);
                newValue.set("+"+b);
            }
            context.write(newKey, newValue);
        }
    }

    public static class DirectedGraphReducer extends Reducer<Text, Text, Text, Text> {

        private Text newValue = new Text();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            Set<String> inSet = new TreeSet<>();
            Set<String> outSet = new HashSet<>();
            StringBuilder output = new StringBuilder();
            for (Text val: values) {
                if (val.toString().startsWith("+")) {
                    outSet.add(val.toString().substring(1));
                } else {
                    inSet.add(val.toString().substring(1));
                }
            }
            boolean first = true;
            for (String vertex: inSet) {
                if (outSet.contains(vertex)) {
                    if (first) {
                        first = false;
                    } else {
                        output.append(";");
                    }
                    output.append(vertex);
                }
            }
            newValue.set(output.toString());
            context.write(key, newValue);
        }
    }
}
