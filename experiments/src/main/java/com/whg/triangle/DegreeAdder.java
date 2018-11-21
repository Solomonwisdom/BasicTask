package com.whg.triangle;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class DegreeAdder {

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
            context.write(newKey, newValue);
            context.write(newValue, newKey);
        }

    }

    public static class DegreeCountReducer extends Reducer<Text, Text, Text, Text> {

        private Text newKey = new Text();
        private Text newValue = new Text();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            Set<String> set = new HashSet<>();
            for (Text val: values) {
                set.add(val.toString());
            }
            String keyString = key.toString();
            String degree2 = "s"+set.size();
            String degree1 = "f"+set.size();
            for (String vertex: set) {
                if (vertex.compareTo(keyString) < 0) {
                    newKey.set(vertex+"#"+keyString);
                    newValue.set(degree2);
                } else {
                    newKey.set(keyString+"#"+vertex);
                    newValue.set(degree1);
                }
                context.write(newKey,newValue);
            }
        }
    }

    public static class DegreeAddMapper extends Mapper<Text, Text, Text, Text> {

        @Override
        public void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {
            context.write(key, value);
        }

    }

    public static class DegreeAddReducer extends Reducer<Text, Text, Text, Text> {

        private Text newKey = new Text();
        private Text newValue = new Text();

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            String[] vertice = key.toString().split("#");
            String vertex1="",vertex2="";
            int degree0=0,degree1=0;
            for (Text val: values) {
                String vertex = val.toString();
                if (vertex.charAt(0)=='f') {
                    degree0 = Integer.parseInt(vertex.substring(1));
                    vertex1=vertice[0]+"@"+degree0;
                } else {
                    degree1 = Integer.parseInt(vertex.substring(1));
                    vertex2=vertice[1]+"@"+degree1;
                }
            }
            if (degree0 < degree1) {
                newKey.set(vertex1);
                newValue.set(vertex2);
            } else if (degree0 > degree1){
                newKey.set(vertex2);
                newValue.set(vertex1);
            } else if (vertice[0].compareTo(vertice[1])<0) {
                newKey.set(vertex1);
                newValue.set(vertex2);
            } else {
                newKey.set(vertex2);
                newValue.set(vertex1);
            }
            context.write(newKey, newValue);
        }
    }
}
