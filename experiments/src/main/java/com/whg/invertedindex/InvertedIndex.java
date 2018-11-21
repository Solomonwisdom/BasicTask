package com.whg.invertedindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
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
 * @date 2018/11/3
 */

public class InvertedIndex extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: invertedindex <in> <out>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        } else {
            Configuration conf = this.getConf();
            Job job = Job.getInstance(conf);
            job.setJobName("Inverted Index");
            job.setJarByClass(InvertedIndex.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            job.setMapperClass(InvertedIndexMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setCombinerClass(IntSumCombiner.class);
            job.setPartitionerClass(InvertedIndexPartitioner.class);
            job.setReducerClass(InvertedIndexReducer.class);
            job.setNumReduceTasks(5);
            Path outputPath = new Path(args[1] + Path.SEPARATOR + "inverted_index");
            FileOutputFormat.setOutputPath(job, outputPath);
            if (job.waitForCompletion(true)) {
                Job sortJob = Job.getInstance(conf);
                sortJob.setJobName("Sort By Average Count");
                sortJob.setJarByClass(SortByAverageCount.class);
                sortJob.setInputFormatClass(KeyValueTextInputFormat.class);
                FileInputFormat.addInputPath(sortJob, outputPath);
                sortJob.setMapperClass(SortByAverageCount.SortMapper.class);
                sortJob.setOutputKeyClass(DoubleWritable.class);
                sortJob.setOutputValueClass(Text.class);
                sortJob.setSortComparatorClass(SortByAverageCount.DoubleWritableDecreasingComparator.class);
                sortJob.setNumReduceTasks(1);
                FileOutputFormat.setOutputPath(sortJob, new Path(args[1] + Path.SEPARATOR + "sort"));
                sortJob.waitForCompletion(true);
                conf.set("fs.defaultFS", "hdfs://master01:9000");
                FileSystem fs = FileSystem.get(conf);
                Path path = new Path(args[0]);
                RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(path, false);
                int txtCnt = 0;
                while (listFiles.hasNext()) {
                    txtCnt++;
                    listFiles.next();
                }
                conf.setInt("txtCnt", txtCnt);
                Job tfJob = Job.getInstance(conf);
                tfJob.setJobName("TF-IDF");
                tfJob.setJarByClass(TFAndIDF.class);
                tfJob.setInputFormatClass(KeyValueTextInputFormat.class);
                FileInputFormat.addInputPath(tfJob, outputPath);
                tfJob.setMapperClass(TFAndIDF.TFAndIDFMapper.class);
                tfJob.setMapOutputKeyClass(Text.class);
                tfJob.setMapOutputValueClass(Text.class);
                FileOutputFormat.setOutputPath(tfJob, new Path(args[1] + Path.SEPARATOR + "tf_idf"));
                tfJob.waitForCompletion(true);
            }
            return 0;
        }
    }

    public static class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable ONE = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
                throws IOException, InterruptedException {
            // 获取当前数据块
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            // 获得文件名
            String fileName = fileSplit.getPath().getName();
            // 删去.txt.segment后缀
            fileName = fileName.substring(0, fileName.length() - 14);
            // 对每一个词产生一个以词加文件名为键，1为值的键值对
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                this.word.set(itr.nextToken() + "#" + fileName);
                context.write(this.word, ONE);
            }
        }
    }

    public static class IntSumCombiner extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int sum = 0;
            // 将所有的值相加
            for (IntWritable val : values) {
                sum += val.get();
            }
            this.result.set(sum);
            context.write(key, this.result);
        }
    }

    public static class InvertedIndexPartitioner extends HashPartitioner<Text, IntWritable> {
        @Override
        public int getPartition(Text key, IntWritable value, int numReduceTasks) {
            // 从键中取出词的部分
            String word = key.toString().split("#")[0];
            // 调用父类的getPartition方法，将分区依据改为词
            return super.getPartition(new Text(word), value, numReduceTasks);
        }
    }

    public static class InvertedIndexReducer extends Reducer<Text, IntWritable, Text, Text> {
        // 记录当前词出现的文档和在文档中的出现次数
        private static List<Map.Entry<String, Integer>> wordFrequencyList = new LinkedList<>();
        private static Text currentWord = new Text("");
        // 记录当前词出现的总次数
        private static Integer totalSum = 0;

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            // 从键中得到正在处理的词和它所在的文档
            String[] wordAndFileName = key.toString().split("#");
            Text word = new Text(wordAndFileName[0]);
            String fileName = wordAndFileName[1];
            // 将值相加得到这个词在这个文档中出现的次数
            for (IntWritable val : values) {
                sum += val.get();
            }
            // 如果一个词相关的键值对全部处理完成，就输出一次处理结果
            if (!currentWord.equals(word) && !"".equals(currentWord.toString()) && totalSum > 0) {
                this.write(context);
            }
            currentWord = word;
            wordFrequencyList.add(new AbstractMap.SimpleEntry<>(fileName, sum));
            totalSum += sum;
        }

        @Override
        public void cleanup(Context context) throws IOException, InterruptedException {
            // 处理最后一个单词
            if (totalSum > 0) {
                this.write(context);
            }
        }

        private void write(Context context) throws IOException, InterruptedException {
            StringBuilder output = new StringBuilder();
            // 总出现次数除以包含这个词的文档数得到平均出现次数。
            output.append(String.format("%.2f", (double) totalSum / wordFrequencyList.size()));
            boolean flag = false;
            // 按格式一次输出每个文档和这个词在其中的出现次数
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
