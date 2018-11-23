package com.whg;

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
            pgd.addClass("Triangle_Count", com.whg.triangle.undirected.TriangleCount.class, "Triangle Count");
            pgd.addClass("Directed_Triangle_Count", com.whg.triangle.directed.TriangleCount.class, "Directed Triangle Count");
            pgd.addClass("Directed_Triangle_Count_old", com.whg.triangle_old.DirectedTriangleCount.class, "Directed Triangle Count");
            exitCode = pgd.run(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.exit(exitCode);
    }
}
