package com.whg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HBaseAssignment extends Configured implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(HBaseAssignment.class);
    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: hbaseassignment <out>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 1;
        } else {
            putData();
            Configuration conf = this.getConf();
            Job job = Job.getInstance(conf);
            job.setJobName("HBase Assignment");
            job.setJarByClass(HBaseAssignment.class);
            Scan scan = new Scan();
            scan.setCaching(500);
            scan.setCacheBlocks(false);
            TableMapReduceUtil.initTableMapperJob("Test", scan, HBaseAssignmentMapper.class, Text.class, IntWritable.class, job);
            job.setReducerClass(IntSumReducer.class);
            FileOutputFormat.setOutputPath(job, new Path(args[0]));
            job.waitForCompletion(true);
            ToolRunner.printGenericCommandUsage(System.out);
        }
        return 0;
    }

    public void putData() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hbase-master,hbase-slave1,hbase-slave2,hbase-slave3,hbase-slave4");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        Connection conn = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        String tableName = "Test";
        try {
            if (admin.tableExists(tableName)) {
                logger.warn("Table: {} exists!", tableName);
            }
            else {
                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor("cf1");
                hColumnDescriptor.setMaxVersions(1);
                tableDesc.addFamily(hColumnDescriptor);
                admin.createTable(tableDesc);
                logger.info("Table: {} create success!", tableName);
            }
        } finally {
            admin.close();
        }
        Put put;
        Random random = new Random(System.currentTimeMillis());
        List<Put> puts = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            put = new Put(Bytes.toBytes("" + i));
            put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("cq1"), Bytes.toBytes("" + random.nextInt(100)));
            puts.add(put);
        }
        HTable htable = (HTable) conn.getTable(TableName.valueOf("Test"));
        try {
            htable.put(puts);
        } finally {
            htable.close();
            conn.close();
        }
    }

    public static class HBaseAssignmentMapper extends TableMapper<Text, IntWritable> {

        private final IntWritable ONE = new IntWritable(1);

        @Override
        public void map(ImmutableBytesWritable row, Result value, Mapper<ImmutableBytesWritable, Result, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            String val = new String(value.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("cq1")));
            context.write(new Text(val), ONE);
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable val: values) {
                sum += val.get();
            }
            this.result.set(sum);
            context.write(key, this.result);
        }
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new HBaseAssignment(), args);
        System.exit(res);
    }
}
