package com.whg;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.List;

public class HBaseTest extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        HBaseUtil.init("hbase-master,hbase-slave1,hbase-slave2,hbase-slave3,hbase-slave4");
        HBaseUtil.deleteTable("test");
        HBaseUtil.createTable("test", new String[]{"cf"}, true);
        for (int i = 0; i < 100; ++i) {
            HBaseUtil.putData("test", "江湖"+i, "cf", "count", "74.57");
        }
        ResultScanner scanner = HBaseUtil.get("test");
        for (Result result : scanner) {
            List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                System.out.print(new String(CellUtil.cloneRow(cell)));
                System.out.print("\t");
                System.out.println(new String(CellUtil.cloneValue(cell)));
            }
        }
        HBaseUtil.deleteTable("test");
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new HBaseTest(), args);
        System.exit(res);
    }
}
