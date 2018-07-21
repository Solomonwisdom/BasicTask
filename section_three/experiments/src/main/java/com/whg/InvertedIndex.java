package com.whg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.*;

/**
 * InvertedIndex class
 *
 * @author wanghaogang
 * @date 2018/7/10
 */

public class InvertedIndex extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: invertedindex <in> [<in>...] <out>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        } else {
            Configuration conf = this.getConf();
            Job job = Job.getInstance(conf);
            job.setJobName("inverted index");
            job.setJarByClass(InvertedIndex.class);
            for (int i=0; i<args.length-1; ++i) {
                FileInputFormat.addInputPath(job, new Path(args[i]));
            }
            job.setMapperClass(InvertedIndexMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setCombinerClass(IntSumCombiner.class);
            job.setPartitionerClass(InvertedIndexPartitioner.class);
            job.setReducerClass(InvertedIndexReducer.class);
            FileOutputFormat.setOutputPath(job, new Path(args[args.length-1]));
            job.waitForCompletion(true);
            return 0;
        }
    }

    public static class InvertedIndexMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final IntWritable ONE = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
                throws IOException, InterruptedException {
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            String fileName = fileSplit.getPath().getName();
            fileName = fileName.substring(0, fileName.indexOf("."));
            StringTokenizer itr = new StringTokenizer(value.toString());
            while(itr.hasMoreTokens()) {
                this.word.set(itr.nextToken()+"#"+fileName);
                context.write(this.word, ONE);
            }
        }
    }

    public static class IntSumCombiner extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable val: values) {
                sum += val.get();
            }
            this.result.set(sum);
            context.write(key, this.result);
        }
    }

    public static class InvertedIndexPartitioner extends HashPartitioner<Text, IntWritable> {
        @Override
        public int getPartition(Text key, IntWritable value, int numReduceTasks) {
            String word = key.toString().split("#")[0];
            return super.getPartition(new Text(word), value, numReduceTasks);
        }
    }

    public static class InvertedIndexReducer extends Reducer<Text, IntWritable, Text, Text> {
        private static List<Map.Entry<String,Integer>> wordFrequencyList = new LinkedList<>();
        private static Text currentWord = new Text("");
        private static Integer totalSum = 0;

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
            int sum = 0;
            String[] wordAndFileName = key.toString().split("#");
            Text word = new Text(wordAndFileName[0]);
            String fileName = wordAndFileName[1];
            for (IntWritable val: values) {
                sum += val.get();
            }
            if (!currentWord.equals(word) && !"".equals(currentWord.toString()) && totalSum > 0) {
                this.write(context);
            }
            currentWord = word;
            wordFrequencyList.add(new AbstractMap.SimpleEntry<>(fileName, sum));
            totalSum += sum;
        }

        @Override
        public void cleanup(Context context) throws IOException, InterruptedException {
            if (totalSum > 0) {
                this.write(context);
            }
        }

        private void write(Context context) throws IOException, InterruptedException {
            StringBuilder output = new StringBuilder();
            output.append(String.format("%.2f", (double) totalSum / wordFrequencyList.size()));
            boolean flag = false;
            for (Map.Entry<String, Integer> entry : wordFrequencyList) {
                if (flag) {
                    output.append(";");
                } else {
                    output.append(",");
                    flag = true;
                }
                output.append(entry.getKey());
                output.append(":");
                output.append(entry.getValue());
            }
            context.write(currentWord, new Text(output.toString()));
            totalSum = 0;
            wordFrequencyList = new LinkedList<>();
        }
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new InvertedIndex(), args);
        System.exit(res);
    }
}
