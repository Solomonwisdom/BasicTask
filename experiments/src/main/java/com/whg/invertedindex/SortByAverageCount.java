package com.whg.invertedindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * SortByAverageCount class
 *
 * @author wanghaogang
 * @date 2018/11/3
 */
public class SortByAverageCount extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: sort <in> <out>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        } else {
            Configuration conf = this.getConf();
            Job job = Job.getInstance(conf);
            job.setJobName("Sort By Average Count");
            job.setJarByClass(SortByAverageCount.class);
            job.setInputFormatClass(KeyValueTextInputFormat.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            job.setMapperClass(SortMapper.class);
            job.setOutputKeyClass(DoubleWritable.class);
            job.setOutputValueClass(Text.class);
            job.setSortComparatorClass(DoubleWritableDecreasingComparator.class);
            job.setNumReduceTasks(1);
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.waitForCompletion(true);
            return 0;
        }
    }

    public static class SortMapper extends Mapper<Text, Text, DoubleWritable, Text> {
        private Text word = new Text();

        @Override
        public void map(Text key, Text value, Mapper<Text, Text, DoubleWritable, Text>.Context context)
                throws IOException, InterruptedException {
            DoubleWritable averageCount = new DoubleWritable(Double.parseDouble(value.toString().split(",")[0]));
            context.write(averageCount, key);
        }
    }

    public static class DoubleWritableDecreasingComparator extends DoubleWritable.Comparator {

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            return -super.compare(a, b);
        }

        @Override
        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            return -super.compare(b1, s1, l1, b2, s2, l2);
        }
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new SortByAverageCount(), args);
        System.exit(res);
    }
}
