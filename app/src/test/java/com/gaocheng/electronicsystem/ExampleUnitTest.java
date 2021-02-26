package com.gaocheng.electronicsystem;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        long l = System.currentTimeMillis();

        System.out.println(""+(l));
        for (int i =0;i<1000000000;i++){
        for (int j =0;j<1000000000;j++){
            Singleton.getInstance();
        }}
        long l2 = System.currentTimeMillis();

        System.out.println(""+(l2));
        System.out.println(""+(l2-l));


    }
}