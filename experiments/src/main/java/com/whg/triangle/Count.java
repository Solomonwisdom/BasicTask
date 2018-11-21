package com.whg.triangle;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Count {

    public static class CountMapper extends Mapper<Text, Text, Text, Text> {

        private Text newKey = new Text();
        private Text newValue1 = new Text("+");
        private Text newValue2 = new Text("-");

        @Override
        public void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] toVertice = value.toString().split(";");
            Integer[] degrees = new Integer[toVertice.length];
            for (int i=0; i < toVertice.length; ++i) {
                degrees[i] = Integer.parseInt(key.toString().split("@")[1]);
                toVertice[i] = toVertice[i].split("@")[0];
            }
            int degree1 = Integer.parseInt(key.toString().split("@")[1]);
            String vertex1 = key.toString().split("@")[0];
            for (int i=0; i < toVertice.length; ++i) {
                if (degree1 < degrees[i]) {
                    newKey.set(vertex1 + "#" + toVertice[i]);
                } else if (degree1 > degrees[i]){
                    newKey.set(toVertice[i] + "#" + vertex1);
                } else if (vertex1.compareTo(toVertice[i])<0) {
                    newKey.set(vertex1 + "#" + toVertice[i]);
                } else {
                    newKey.set(toVertice[i] + "#" + vertex1);
                }
                context.write(newKey, newValue1);
                for (int j=i+1; j < toVertice.length; ++j) {
                    if (degrees[j] < degrees[i]) {
                        newKey.set(toVertice[j] + "#" + toVertice[i]);
                    } else if (degrees[j] > degrees[i]){
                        newKey.set(toVertice[i] + "#" + toVertice[j]);
                    } else if (toVertice[j].compareTo(toVertice[i])<0) {
                        newKey.set(toVertice[j] + "#" + toVertice[i]);
                    } else {
                        newKey.set(toVertice[i] + "#" + toVertice[j]);
                    }
                    context.write(newKey, newValue2);
                }
            }
        }
    }

    public static class DirectedCountMapper extends Mapper<Text, Text, Text, Text> {

        private Text newKey = new Text();
        private Text newValue1 = new Text("+");
        private Text newValue2 = new Text("-");

        @Override
        public void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] toVertice = value.toString().split(";");
            String keyString = key.toString();
            for (int i=0; i < toVertice.length; ++i) {
                newKey.set(keyString+"#"+toVertice[i]);
                context.write(newKey, newValue1);
                for (int j=i+1; j < toVertice.length; ++j) {
                    newKey.set(toVertice[i] + "#" + toVertice[j]);
                    context.write(newKey, newValue2);
                }
            }
        }
    }

    public static class CountReducer extends Reducer<Text, Text, Text, Text> {

        private int count = 0;
        private Text newValue1 = new Text("+");
        private Text newValue2 = new Text("-");

        @Override
        public void setup(Context context) {
            count = 0;
        }

        @Override
        public void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text("c"), new Text(""+count));
        }

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) {
            boolean provided = false;
            int need = 0;
            for (Text val: values) {
                if ((!provided) && newValue1.equals(val)) {
                    provided = true;
                } else if (newValue2.equals(val)) {
                    ++need;
                }
            }
            if (provided) {
                count += need;
            }
        }
    }
}
