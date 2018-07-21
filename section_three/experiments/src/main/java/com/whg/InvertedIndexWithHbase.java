package com.whg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


import java.io.*;
import java.net.URI;
import java.util.*;

/**
 * InvertedIndex class
 *
 * @author wanghaogang
 * @date 2018/7/19
 */

public class InvertedIndexWithHbase extends Configured implements Tool {

    private static final Log log = LogFactory.getLog(InvertedIndexWithHbase.class);
    private static final String zkHost = "hbase-master,hbase-slave1,hbase-slave2,hbase-slave3,hbase-slave4";

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: invertedindexwithhbase <in> [<in>...] <out>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        } else {
            // create table 'Wuxia'
            HBaseUtil.init(zkHost);
            try {
                HBaseUtil.deleteTable("Wuxia");
                HBaseUtil.createTable("Wuxia", new String[]{"cf"}, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Configuration conf = this.getConf();
            Job job = Job.getInstance(conf);
            job.setJobName("inverted index with hbase");
            job.setJarByClass(InvertedIndexWithHbase.class);
            job.addCacheFile(URI.create("hdfs://hbase-master:9000/user/root/StopWords/Stop_words.txt"));
            for (int i=0; i<args.length-1; ++i) {
                FileInputFormat.addInputPath(job, new Path(args[i]));
            }
            job.setMapperClass(InvertedIndexMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setCombinerClass(IntSumCombiner.class);
            job.setPartitionerClass(InvertedIndexPartitioner.class);
            job.setReducerClass(InvertedIndexReducer.class);
            TableMapReduceUtil.initTableReducerJob("Wuxia", InvertedIndexReducer.class, job);
            // set OutputPath and TableName
            FileOutputFormat.setOutputPath(job, new Path(args[args.length-1]));
            job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, "Wuxia");
            MultipleOutputs.addNamedOutput(job, "hdfs", TextOutputFormat.class,
                    Text.class, Text.class);
            MultipleOutputs.addNamedOutput(job,"hbase", TableOutputFormat.class,
                    ImmutableBytesWritable.class, Put.class);
            LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
            job.waitForCompletion(true);
            ToolRunner.printGenericCommandUsage(System.out);
            return 0;
        }
    }

    public static class InvertedIndexMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final IntWritable ONE = new IntWritable(1);
        private Text word = new Text();
        private static Set<String> stopWords = new HashSet<>();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            BufferedReader br = null;
            String line;
            Path[] paths = context.getLocalCacheFiles();
            String path = paths[0].toString();
            log.warn(path);
            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                line = line.trim();
                stopWords.add(line);
            }
        }

        @Override
        public void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
                throws IOException, InterruptedException {
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            String fileName = fileSplit.getPath().getName();
            fileName = fileName.substring(0, fileName.indexOf("."));
            StringTokenizer itr = new StringTokenizer(value.toString());
            while(itr.hasMoreTokens()) {
                String curWord = itr.nextToken();
                if (!stopWords.contains(curWord)) {
                    this.word.set(curWord + "#" + fileName);
                    context.write(this.word, ONE);
                }
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

    public static class InvertedIndexReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {
        private static List<Map.Entry<String,Integer>> wordFrequencyList = new LinkedList<>();
        private static Text currentWord = new Text("");
        private static Integer totalSum = 0;
        private MultipleOutputs mos;

        @Override
        public void setup(Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs(context);
        }

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
            mos.close();
        }

        private void write(Context context) throws IOException, InterruptedException {
            StringBuilder output = new StringBuilder();
            String averageOccurrence = String.format("%.2f", (double) totalSum / wordFrequencyList.size());
            boolean flag = false;
            for (Map.Entry<String, Integer> entry : wordFrequencyList) {
                if (flag) {
                    output.append(";");
                } else {
                    flag = true;
                }
                output.append(entry.getKey());
                output.append(":");
                output.append(entry.getValue());
            }
            Put put = new Put(currentWord.toString().getBytes());
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("count"), Bytes.toBytes(averageOccurrence));
            mos.write("hbase", new ImmutableBytesWritable(Bytes.toBytes(currentWord.toString())), put);
            mos.write("hdfs", currentWord, new Text(output.toString()));
            totalSum = 0;
            wordFrequencyList = new LinkedList<>();
        }
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new InvertedIndexWithHbase(), args);
        if (res != 0) {
            System.exit(res);
        } else {
            FileSystem local = FileSystem.getLocal(new Configuration());
            FSDataOutputStream out = local.create(new Path("/root/experiment/count.txt"));
            HBaseUtil.init(zkHost);
            ResultScanner scanner = HBaseUtil.get("Wuxia");
            for (Result result: scanner) {
                List<Cell> cells = result.listCells();
                for (Cell cell: cells) {
                    out.write(Bytes.toString(CellUtil.cloneRow(cell)).getBytes());
                    out.write("\t".getBytes());
                    out.write(Bytes.toString(CellUtil.cloneValue(cell)).getBytes());
                    out.write("\n".getBytes());
                }
            }
            out.flush();
            out.close();
        }
    }
}
