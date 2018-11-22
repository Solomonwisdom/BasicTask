package com.whg.triangle;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TriCounter {

    public static class CountMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {

        private Map<String, Integer> vecIds;
        private IntWritable newKey;
        private IntWritable newValue;

        @Override
        public void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            vecIds = new HashMap<>();
            Configuration conf = context.getConfiguration();
            Path path = new Path("/user/2018st21/tmp/rank/part-r-00000");
            SequenceFile.Reader.Option fileOption = SequenceFile.Reader.file(path);
            SequenceFile.Reader reader= new SequenceFile.Reader(conf, fileOption);
            IntWritable key = new IntWritable();
            Text value = new Text();
            int id = 0;
            while (reader.next(key, value)) {
                vecIds.put(value.toString(), id);
                id++;
            }
            reader.close();
            newKey = new IntWritable();
            newValue = new IntWritable();
        }

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            if (itr.countTokens()!=2) {
                return;
            }
            int a = vecIds.get(itr.nextToken()), b = vecIds.get(itr.nextToken());
            if (a == b) {
                return;
            }
            else if (a < b) {
                newKey.set(a);
                newValue.set(b);
            } else {
                newKey.set(b);
                newValue.set(a);
            }
            context.write(newKey, newValue);
        }
    }

    public static class CountReducer extends Reducer<IntWritable, IntWritable, Text, IntWritable> {

        private int count = 0;
        private List<Integer>[] adjList;
        private Map<String, Integer> vecIds;

        @Override
        public void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            vecIds = new HashMap<>();
            Configuration conf = context.getConfiguration();
            Path path = new Path("/user/2018st21/tmp/rank/part-r-00000");
            SequenceFile.Reader.Option fileOption = SequenceFile.Reader.file(path);
            SequenceFile.Reader reader= new SequenceFile.Reader(conf, fileOption);
            IntWritable key = new IntWritable();
            Text value = new Text();
            int id = 0;
            while (reader.next(key, value)) {
                vecIds.put(value.toString(), id);
                id++;
            }
            reader.close();
            this.count = 0;
            adjList = new List[id];
            Set<Integer>[] adjSet = new Set[id];
            for (int i=0; i<adjList.length; ++i) {
                adjList[i] = new ArrayList<>();
                adjSet[i] = new TreeSet<>();
            }
            FileSystem fs = FileSystem.get(conf);
            Path file = new Path(conf.get("args0"));
            FSDataInputStream getIt = fs.open(file);
            BufferedReader d = new BufferedReader(new InputStreamReader(getIt));
            String s;
            while ((s = d.readLine()) != null) {
                StringTokenizer itr = new StringTokenizer(s);
                if (itr.countTokens()!=2) {
                    return;
                }
                int a = vecIds.get(itr.nextToken()), b = vecIds.get(itr.nextToken());
                if (a == b) {
                    return;
                }
                else if (a < b) {
                    adjSet[a].add(b);
                } else {
                   adjSet[b].add(a);
                }

            }
            for (int i=0; i < adjList.length; ++i) {
                for (int x: adjSet[i]) {
                    adjList[i].add(x);
                }
            }
            d.close();
        }

        @Override
        public void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text("c"), new IntWritable(count));
        }

        @Override
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) {
            int u = key.get();
            Set<Integer> mark = new HashSet<>();
            for (IntWritable val: values) {
                int v = val.get();
                if (mark.contains(v)) {
                    continue;
                }
                mark.add(v);
                count += this.getSizeOfIntersect(adjList[u], adjList[v]);
            }
        }

        private int getSizeOfIntersect(List<Integer> a, List<Integer> b) {
            int ans = 0;
            int j = 0;
            for (int val: a) {
                while (j<b.size() &&b.get(j) < val) {
                    ++j;
                }
                if (j>=b.size()) {
                    break;
                }
                if (b.get(j) == val) {
                    ++ans;
                    ++j;
                }
            }
            return ans;
        }
    }
}
