package com.whg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.List;

public class WuxiaScan extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        FileSystem local = FileSystem.getLocal(new Configuration());
        FSDataOutputStream out = local.create(new Path("/root/experiment/count.txt"));
        HBaseUtil.init("hbase-master,hbase-slave1,hbase-slave2,hbase-slave3,hbase-slave4");
        ResultScanner scanner = HBaseUtil.get("Wuxia");
        for (Result result: scanner) {
            List<Cell> cells = result.listCells();
            for (Cell cell: cells) {
                out.write(Bytes.toString(CellUtil.cloneRow(cell)).getBytes());
                out.write("\t".getBytes());
                out.write(Bytes.toString(CellUtil.cloneValue(cell)).getBytes());
                out.write("\n".getBytes());
                out.flush();
            }
        }
        out.flush();
        out.close();
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new WuxiaScan(), args);
        System.exit(res);
    }
}
