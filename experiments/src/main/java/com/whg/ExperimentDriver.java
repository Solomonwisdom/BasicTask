package com.whg;

import com.whg.invertedindex.InvertedIndex;
import com.whg.invertedindex.SortByAverageCount;
import com.whg.invertedindex.TFAndIDF;
import com.whg.triangle.DirectedTriangleCount;
import com.whg.triangle.TriangleCount;
import org.apache.hadoop.util.ProgramDriver;

/**
 * Experiment class
 *
 * @author wanghaogang
 * @date 2018/11/3
 */

public class ExperimentDriver {

    public static void main(String[] args) {
        int exitCode = -1;
        ProgramDriver pgd = new ProgramDriver();
        try {
            pgd.addClass("invertedindex", InvertedIndex.class, "A map/reduce program that implements inverted index");
            pgd.addClass("sort", SortByAverageCount.class, "Sort");
            pgd.addClass("TF_IDF", TFAndIDF.class, "TF-IDF");
            pgd.addClass("Triangle_Count", TriangleCount.class, "Triangle Count");
            pgd.addClass("Directed_Triangle_Count", DirectedTriangleCount.class, "Directed Triangle Count");
            exitCode = pgd.run(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.exit(exitCode);
    }
}
