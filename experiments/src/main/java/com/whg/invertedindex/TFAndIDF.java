package com.whg.invertedindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.*;

/**
 * TFAndIDF class
 *
 * @author wanghaogang
 * @date 2018/11/3
 */

public class TFAndIDF extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: TFIDF <in> <out>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        } else {
            Configuration conf = this.getConf();
            FileSystem fs = FileSystem.get(conf);
            Path path = new Path(args[0]);
            RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(path, false);
            int txtCnt = 0;
            while (listFiles.hasNext()) {
                txtCnt++;
                listFiles.next();
            }
            conf.setInt("txtCnt", txtCnt);
            Job job = Job.getInstance(conf);
            job.setJobName("TF-IDF");
            job.setJarByClass(TFAndIDF.class);
            job.setInputFormatClass(KeyValueTextInputFormat.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            job.setMapperClass(TFAndIDFMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.waitForCompletion(true);
            return 0;
        }
    }

    public static class TFAndIDFMapper extends Mapper<Text, Text, Text, Text> {
        private static Text nothing = new Text("");
        private static Text outKey = new Text();

        @Override
        public void map(Text key, Text value, Mapper<Text, Text, Text, Text>.Context context)
                throws IOException, InterruptedException {
            String[] novelAndCounts = value.toString().split(",")[1].split(";");
            double txtCnt = context.getConfiguration().getInt("txtCnt", 0);
            if (txtCnt == 0) {
                System.exit(1);
            }
            double IDF = Math.log(txtCnt / (1 + novelAndCounts.length));
            List<Map.Entry<String, Integer>> authorAndCounts = new LinkedList<>();
            for (int i = 0; i < novelAndCounts.length; ++i) {
                String[] novelAndCount = novelAndCounts[i].split(":");
                String author = novelAndCount[0].split("\\d{2}")[0];
                authorAndCounts.add(new AbstractMap.SimpleEntry<>(author,
                        Integer.parseInt(novelAndCount[1])));
            }
            String preAuthor = "";
            Integer TF = 0;
            for (Map.Entry<String, Integer> entry : authorAndCounts) {
                if (!"".equals(preAuthor) && !preAuthor.equals(entry.getKey())) {
                    outKey.set(preAuthor + "," + key.toString() + "," + TF + "-" + IDF);
                    context.write(outKey, nothing);
                    TF = 0;
                }
                TF += entry.getValue();
                preAuthor = entry.getKey();
            }
            if (!"".equals(preAuthor)) {
                outKey.set(preAuthor + "," + key.toString() + "," + TF + "-" + IDF);
                context.write(outKey, nothing);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new TFAndIDF(), args);
        System.exit(res);
    }
}
