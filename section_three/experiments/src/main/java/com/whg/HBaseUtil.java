package com.whg;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HBase 工具类
 * Created by whg on 2018/7/20.
 */
public class HBaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(HBaseUtil.class);

    private static Configuration conf;
    private static Connection conn;

    public static void init(String quorum){
        try {
            if (conf == null) {
                conf = HBaseConfiguration.create();
                conf.set("hbase.zookeeper.quorum", quorum);
                conf.set("hbase.zookeeper.property.clientPort", "2181");
            }
        } catch (Exception e) {
            logger.error("HBase Configuration Initialization failure !");
            throw new RuntimeException(e) ;
        }
    }

    /**
     * 获得链接
     * @return
     */
    public static synchronized Connection getConnection() {
        try {
            if(conn == null || conn.isClosed()){
                conn = ConnectionFactory.createConnection(conf);
            }
        } catch (IOException e) {
            logger.error("HBase 建立链接失败 ", e);
        }
        return conn;

    }

    /**
     * 创建表
     * @param tableName
     * @throws Exception
     */
    public static void createTable(String tableName, String[] columnFamilies, boolean preBuildRegion) throws Exception {
        if(preBuildRegion){
            String[] s = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
            int partition = 16;
            byte[][] splitKeys = new byte[partition - 1][];
            for (int i = 1; i < partition; i++) {
                splitKeys[i - 1] = Bytes.toBytes(s[i - 1]);
            }
            createTable(tableName, columnFamilies, splitKeys);
        } else {
            createTable(tableName, columnFamilies);
        }
    }

    private static void createTable(String tableName, int pNum, boolean only) throws Exception {
        String[] s = RandCodeEnum.HBASE_CHAR.getHbaseKeys(pNum,2,only);
        byte[][] splitKeys = new byte[pNum][];
        for (int i = 1; i <= pNum; i++) {
            splitKeys[i - 1] = Bytes.toBytes(s[i - 1]);
        }
        createTable(tableName, new String[] { "events" }, splitKeys);
    }

    /**
     * 建表
     * @param tableName
     * @param cfs
     * @throws IOException
     */
    private static void createTable(String tableName, String[] cfs, byte[][] splitKeys) throws Exception {
        Connection conn = getConnection();
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        try {
            if (admin.tableExists(tableName)) {
                logger.warn("Table: {} is exists!", tableName);
                return;
            }
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
            for (int i = 0; i < cfs.length; i++) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cfs[i]);
                hColumnDescriptor.setMaxVersions(1);
                tableDesc.addFamily(hColumnDescriptor);
            }
            admin.createTable(tableDesc, splitKeys);
            logger.info("Table: {} create success!", tableName);
        } finally {
            admin.close();
            closeConnect(conn);
        }
    }

    /**
     * 建表
     * @param tableName
     * @param cfs
     * @throws IOException
     */
    private static void createTable(String tableName, String[] cfs) throws Exception {
        Connection conn = getConnection();
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        try {
            if (admin.tableExists(tableName)) {
                logger.warn("Table: {} is exists!", tableName);
                return;
            }
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
            for (int i = 0; i < cfs.length; i++) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cfs[i]);
                hColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
                hColumnDescriptor.setMaxVersions(1);
                tableDesc.addFamily(hColumnDescriptor);
            }
            admin.createTable(tableDesc);
            logger.info("Table: {} create success!", tableName);
        } finally {
            admin.close();
            closeConnect(conn);
        }
    }

    /**
     * 删除表
     * @param tablename
     * @throws IOException
     */
    public static void deleteTable(String tablename) throws IOException {
        Connection conn = getConnection();
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        try {
            if (!admin.tableExists(tablename)) {
                logger.warn("Table: {} is not exists!", tablename);
                return;
            }
            admin.disableTable(tablename);
            admin.deleteTable(tablename);
            logger.info("Table: {} delete success!", tablename);
        } finally {
            admin.close();
            closeConnect(conn);
        }
    }

    /**
     * 获取  Table
     * @param tableName 表名
     * @return
     * @throws IOException
     */
    public static Table getTable(String tableName){
        try {
            return getConnection().getTable(TableName.valueOf(tableName));
        } catch (Exception e) {
            logger.error("Obtain Table failure !", e);
        }
        return null;
    }

    /**
     * 给 table 创建 snapshot
     * @param snapshotName 快照名称
     * @param tableName 表名
     * @return
     * @throws IOException
     */
    public static void snapshot(String snapshotName, TableName tableName){
        try {
            Admin admin = getConnection().getAdmin();
            admin.snapshot(snapshotName, tableName);
        } catch (Exception e) {
            logger.error("Snapshot " + snapshotName + " create failed !", e);
        }
    }

    /**
     * 获得现已有的快照
     * @param snapshotNameRegex 正则过滤表达式
     * @return
     * @throws IOException
     */
    public static List<HBaseProtos.SnapshotDescription> listSnapshots(String snapshotNameRegex){
        try {
            Admin admin = getConnection().getAdmin();
            if(StringUtils.isNotBlank(snapshotNameRegex)) {
                return admin.listSnapshots(snapshotNameRegex);
            } else {
                return admin.listSnapshots();
            }
        } catch (Exception e) {
            logger.error("Snapshot " + snapshotNameRegex + " get failed !", e);
        }
        return null;
    }

    /**
     * 批量删除Snapshot
     * @param snapshotNameRegex 正则过滤表达式
     * @return
     * @throws IOException
     */
    public static void deleteSnapshots(String snapshotNameRegex){
        try {
            Admin admin = getConnection().getAdmin();
            if (StringUtils.isNotBlank(snapshotNameRegex)) {
                admin.deleteSnapshots(snapshotNameRegex);
            } else {
                logger.error("SnapshotNameRegex can't be null !");
            }
        } catch (Exception e) {
            logger.error("Snapshots " + snapshotNameRegex + " del failed !", e);
        }
    }

    /**
     * 单个删除Snapshot
     * @param snapshotName 正则过滤表达式
     * @return
     * @throws IOException
     */
    public static void deleteSnapshot(String snapshotName){
        try {
            Admin admin = getConnection().getAdmin();
            if (StringUtils.isNotBlank(snapshotName)) {
                admin.deleteSnapshot(snapshotName);
            } else {
                logger.error("SnapshotName can't be null !");
            }
        } catch (Exception e) {
            logger.error("Snapshot " + snapshotName + " del failed !", e);
        }
    }

    /**
     * 异步往指定表添加数据
     * @param tableName  	表名
     * @param rowKey	 	行关键字
     * @param family        列族
     * @param qualifier     列关键字
     * @param value         值
     * @throws IOException
     */
    public static void putData(String tableName, String rowKey, String family, String qualifier, String value) {
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        try {
            put(tableName, put);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用HTable往指定表添加数据
     * @param tableName  	表名
     * @param rowKey	 	行关键字
     * @param family        列族
     * @param qualifier     列关键字
     * @param value         值
     * @throws IOException
     */
    public static void putDataByHTable(String tableName, String rowKey, String family, String qualifier, String value) {
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        try {
            putByHTable(tableName, Arrays.asList(put));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步往指定表添加数据
     * @param tableName  	表名
     * @param puts	 			需要添加的数据
     * @throws IOException
     */
    public static void put(String tableName, List<Put> puts) throws Exception {
        Connection conn = getConnection();
        final BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            @Override
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
                for (int i = 0; i < e.getNumExceptions(); i++) {
                    System.out.println("Failed to sent put " + e.getRow(i) + ".");
                    logger.error("Failed to sent put " + e.getRow(i) + ".");
                }
            }
        };
        BufferedMutatorParams params = new BufferedMutatorParams(TableName.valueOf(tableName))
                .listener(listener);
        params.writeBufferSize(5 * 1024 * 1024);

        final BufferedMutator mutator = conn.getBufferedMutator(params);
        try {
            mutator.mutate(puts);
            mutator.flush();
        } finally {
            mutator.close();
            closeConnect(conn);
        }
    }

    /**
     * 异步往指定表添加数据
     * @param tableName  	表名
     * @param put	 			需要添加的数据
     * @return long				返回执行时间
     * @throws IOException
     */
    public static void put(String tableName, Put put) throws Exception {
        put(tableName, Arrays.asList(put));
    }

    /**
     * 往指定表添加数据
     * @param tableName  	表名
     * @param puts	 			需要添加的数据
     * @return long				返回执行时间
     * @throws IOException
     */
    public static long putByHTable(String tableName, List<?> puts) throws Exception {
        long currentTime = System.currentTimeMillis();
        Connection conn = getConnection();
        HTable htable = (HTable) conn.getTable(TableName.valueOf(tableName));
        htable.setAutoFlushTo(false);
        htable.setWriteBufferSize(5 * 1024 * 1024);
        try {
            htable.put((List<Put>)puts);
            htable.flushCommits();
        } finally {
            htable.close();
            closeConnect(conn);
        }
        return System.currentTimeMillis() - currentTime;
    }

    /**
     * 删除单条数据
     * @param tableName
     * @param row
     * @throws IOException
     */
    public static void delete(String tableName, String row) throws IOException {
        Table table = getTable(tableName);
        if(table!=null){
            try {
                Delete d = new Delete(row.getBytes());
                table.delete(d);
            } finally {
                table.close();
            }
        }
    }

    /**
     * 删除多行数据
     * @param tableName
     * @param rows
     * @throws IOException
     */
    public static void delete(String tableName, String[] rows) throws IOException {
        Table table = getTable(tableName);
        if (table != null) {
            try {
                List<Delete> list = new ArrayList<Delete>();
                for (String row : rows) {
                    Delete d = new Delete(row.getBytes());
                    list.add(d);
                }
                if (list.size() > 0) {
                    table.delete(list);
                }
            } finally {
                table.close();
            }
        }
    }

    /**
     * 关闭连接
     * @throws IOException
     */
    public static void closeConnect(Connection conn){
        if(null != conn){
            try {
				conn.close();
            } catch (Exception e) {
                logger.error("closeConnect failure !", e);
            }
        }
    }

    /**
     * 获取单条数据
     * @param tableName
     * @param row
     * @return
     * @throws IOException
     */
    public static Result getRow(String tableName, byte[] row) {
        Table table = getTable(tableName);
        Result rs = null;
        if(table!=null){
            try{
                Get g = new Get(row);
                rs = table.get(g);
            } catch (IOException e) {
                logger.error("getRow failure !", e);
            } finally{
                try {
                    table.close();
                } catch (IOException e) {
                    logger.error("getRow failure !", e);
                }
            }
        }
        return rs;
    }

    /**
     * 获取多行数据
     * @param tableName
     * @param rows
     * @return
     * @throws Exception
     */
    public static <T> Result[] getRows(String tableName, List<T> rows) {
        Table table = getTable(tableName);
        List<Get> gets = null;
        Result[] results = null;
        try {
            if (table != null) {
                gets = new ArrayList<Get>();
                for (T row : rows) {
                    if(row!=null){
                        gets.add(new Get(Bytes.toBytes(String.valueOf(row))));
                    }else{
                        throw new RuntimeException("hbase have no data");
                    }
                }
            }
            if (gets.size() > 0) {
                results = table.get(gets);
            }
        } catch (IOException e) {
            logger.error("getRows failure !", e);
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                logger.error("table.close() failure !", e);
            }
        }
        return results;
    }

    /**
     * 扫描整张表，注意使用完要释放。
     * @param tableName
     * @return
     * @throws IOException
     */
    public static ResultScanner get(String tableName) {
        Table table = getTable(tableName);
        ResultScanner results = null;
        if (table != null) {
            try {
                Scan scan = new Scan();
                scan.setCaching(1000);
                results = table.getScanner(scan);
            } catch (IOException e) {
                logger.error("getResultScanner failure !", e);
            } finally {
                try {
                    table.close();
                } catch (IOException e) {
                    logger.error("table.close() failure !", e);
                }
            }
        }
        return results;
    }

    /**
     * 格式化输出结果
     */
    public static void formatRow(KeyValue[] rs){
        for(KeyValue kv : rs){
            System.out.println(" column family  :  " + Bytes.toString(kv.getFamilyArray()));
            System.out.println(" column   :  " + Bytes.toString(kv.getQualifierArray()));
            System.out.println(" value   :  " + Bytes.toString(kv.getValueArray()));
            System.out.println(" timestamp   :  " + String.valueOf(kv.getTimestamp()));
            System.out.println("--------------------");
        }
    }

    /**
     * byte[] 类型的长整形数字转换成 long 类型
     * @param byteNum
     * @return
     */
    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

}