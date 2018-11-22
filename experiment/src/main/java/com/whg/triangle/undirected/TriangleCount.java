package com.whg.triangle.undirected;

import com.whg.triangle.TriCounter;
import com.whg.triangle.DegreeCount;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class TriangleCount extends Configured implements Tool {


    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: Triangle_Count <in> <out> [num of reducer]");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        }
        int reducerNum;
        if (args.length > 2) {
            try {
                reducerNum = Integer.parseInt(args[2]);
            } catch (Exception e) {
                e.printStackTrace();
                reducerNum = 60;
            }
            if (reducerNum>200) {
                reducerNum = 60;
            }
        } else {
            reducerNum = 60;
        }
        Configuration conf = this.getConf();
        conf.set("mapreduce.input.fileinputformat.split.minsize", "2621440");
        conf.set("mapreduce.input.fileinputformat.split.maxsize", "12582912");
        conf.set("yarn.scheduler.minimum-allocation-mb", "1024");
        conf.set("yarn.scheduler.maximum-allocation-mb", "4096");
        conf.set("args0", args[0]);
        FileSystem fs = FileSystem.get(conf);
        fs.delete(new Path("/user/2018st21/tmp"), true);
        fs.delete(new Path(args[1]), true);

        Job job0 = Job.getInstance(conf, "Degree Counter");
        job0.setJarByClass(TriangleCount.class);
        // 设置Map、Combine和Reduce处理类
        job0.setMapperClass(DegreeCount.DegreeCountMapper.class);
        job0.setReducerClass(DegreeCount.DegreeCountReducer.class);

        // 设置输出类型
        job0.setMapOutputKeyClass(Text.class);
        job0.setMapOutputValueClass(Text.class);
        job0.setOutputKeyClass(Text.class);
        job0.setOutputValueClass(IntWritable.class);

//        job.setInputFormatClass(TextInputFormat.class);
//        job.setOutputFormatClass(TextOutputFormat.class);
        job0.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputCompressionType(job0, SequenceFile.CompressionType.NONE);
        job0.setNumReduceTasks(reducerNum);

        // 设置输入和输出目录
        FileInputFormat.addInputPath(job0, new Path(args[0]));
        FileOutputFormat.setOutputPath(job0, new Path("/user/2018st21/tmp/dc"));

        if (!job0.waitForCompletion(true)) {
            System.exit(1);
        }

        Job job1 = Job.getInstance(conf, "Rank By Degree");
        job1.setJarByClass(TriangleCount.class);
        // 设置Map、Combine和Reduce处理类
        job1.setMapperClass(DegreeCount.ReverseMapper.class);

        // 设置输出类型
        job1.setMapOutputKeyClass(IntWritable.class);
        job1.setMapOutputValueClass(Text.class);
        job1.setOutputKeyClass(IntWritable.class);
        job1.setOutputValueClass(Text.class);

//        job0.setInputFormatClass(KeyValueTextInputFormat.class);
//        job0.setOutputFormatClass(TextOutputFormat.class);
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputCompressionType(job0, SequenceFile.CompressionType.NONE);
        job1.setNumReduceTasks(1);

        // 设置输入和输出目录
        FileInputFormat.addInputPath(job1, new Path("/user/2018st21/tmp/dc"));
        FileOutputFormat.setOutputPath(job1, new Path("/user/2018st21/tmp/rank"));

        if (!job1.waitForCompletion(job0.isComplete())) {
            System.exit(1);
        }

        /*
         * 局部统计三角形作业
         * */
        Job job2 = Job.getInstance(conf, "Partial Count");
        job2.setJarByClass(TriangleCount.class);

        // 设置Map、Combine和Reduce处理类
        job2.setMapperClass(TriCounter.CountMapper.class);
        job2.setReducerClass(TriCounter.CountReducer.class);

        // 设置输出类型
        job2.setMapOutputKeyClass(IntWritable.class);
        job2.setMapOutputValueClass(IntWritable.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);

        job2.setNumReduceTasks(reducerNum);

//        job2.setInputFormatClass(KeyValueTextInputFormat.class);
//        job2.setOutputFormatClass(TextOutputFormat.class);
        job2.setInputFormatClass(TextInputFormat.class);
        job2.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputCompressionType(job2, SequenceFile.CompressionType.NONE);
        // 设置job2输入和输出目录
        FileInputFormat.addInputPath(job2, new Path(args[0]));
        FileOutputFormat.setOutputPath(job2, new Path("/user/2018st21/tmp/count"));

        if (!job2.waitForCompletion(job1.isComplete())) {
            System.exit(1);
        }

        /*
         * 相加得到全局解作业
         */
        int ans = 0;
        String path0 = "/user/2018st21/tmp/count/part-r-00000";
        for (int i=0; i < reducerNum; ++i) {
            String id = ""+i;
            Path path = new Path(path0.substring(0, path0.length()-id.length())+id);
            SequenceFile.Reader.Option fileOption = SequenceFile.Reader.file(path);
            SequenceFile.Reader reader= new SequenceFile.Reader(conf, fileOption);
            Text key = new Text();
            IntWritable value = new IntWritable();
            while (reader.next(key, value)) {
                ans += value.get();
            }
            reader.close();
        }
        FSDataOutputStream fsDataOutputStream = fs.create(new Path(args[1]+Path.SEPARATOR+"part-r-00000"));
        fsDataOutputStream.writeChars("The number of Triangle:\t"+ans);
        /*
        //设置一个job
        Job job3 = Job.getInstance(conf, "Sum");
        job3.setJarByClass(TriangleCount.class);

        // 设置Map、Combine和Reduce处理类
        job3.setMapperClass(Sum.SumMapper.class);
        job3.setReducerClass(Sum.SumReducer.class);

        // 设置输出类型
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(IntWritable.class);
        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(IntWritable.class);

//        job3.setInputFormatClass(KeyValueTextInputFormat.class);
//        job3.setOutputFormatClass(TextOutputFormat.class);
        job3.setInputFormatClass(SequenceFileInputFormat.class);
        job3.setOutputFormatClass(TextOutputFormat.class);

        job3.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job3, new Path("/user/2018st21/tmp/count"));
        FileOutputFormat.setOutputPath(job3, new Path(args[1]));

        if (!job3.waitForCompletion(job2.isComplete())) {
            System.exit(1);
        }
        */

        fs.delete(new Path("/user/2018st21/tmp"), true);
        fs.close();
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new TriangleCount(), args);
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            Path file = new Path(args[1]+Path.SEPARATOR+"part-r-00000");
            FSDataInputStream getIt = fs.open(file);
            BufferedReader d = new BufferedReader(new InputStreamReader(getIt));
            String s;
            for (int i=0; i<40; ++i) {
                System.out.print("*");
            }
            System.out.println();
            while ((s = d.readLine()) != null) {
                System.out.println(s);
            }
            d.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(res);
    }

}
