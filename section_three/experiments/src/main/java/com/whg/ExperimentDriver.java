package com.whg;

import org.apache.hadoop.util.ProgramDriver;

/**
 * InvertedIndex class
 *
 * @author wanghaogang
 * @date 2018/7/10
 */

public class ExperimentDriver {

    public static void main(String[] args) {
        int exitCode = -1;
        ProgramDriver pgd = new ProgramDriver();
        try {
            pgd.addClass("invertedindex", InvertedIndex.class, "A map/reduce program that implements inverted index");
            pgd.addClass("invertedindexwithhbase", InvertedIndexWithHbase.class, "A map/reduce program that implements inverted index");
            pgd.addClass("Wuxiascan", WuxiaScan.class, "Scan the table Wuxia and save all in a file");
            pgd.addClass("hbasetest", HBaseTest.class, "A Test");
            exitCode = pgd.run(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.exit(exitCode);
    }
}
